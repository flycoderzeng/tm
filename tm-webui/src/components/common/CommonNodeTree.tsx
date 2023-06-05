import React, { useState, useEffect } from 'react';
import {Tree, Tooltip, message, Menu, Modal, Form, Input} from 'antd';
import { Radio } from 'antd';
import axios from "axios";
import {AntDataNode} from "../../entities/AntDataNode";
import {FormInstance} from "antd/lib/form";
import {DataTypeEnum} from "../../entities/DataTypeEnum";
import {ApiUrlConfig} from "../../config/api.url";
import {WindowTopUtils} from "../../utils/WindowTopUtils";

interface IState {
    projectId ?: number|null;
    dataTypeId ?: number|null;
    initTreeData : AntDataNode[];
    setRenderRightFlag: any;
    setNodeId: any;
}

const defaultExpandedKeys = ['1-1'];
const rightMenuInitKeys = [
    {key: '1', title: '新建', disabled: false},
    {key: '2', title: '修改', disabled: false},
    {key: '3', title: '删除', disabled: false},
    {key: '4', title: '复制', disabled: false},
    {key: '5', title: '粘贴', disabled: false},
    {key: '99', title: '刷新', disabled: false}
    ];
const rightMenuInitStyle = {
    width: 200,
    display: 'none',
    position: 'absolute',
    top: '10000px',
    left: '10000px',
    borderRadius: '3px',
    zIndex: 20
};

// It's just a simple demo. You can use tree map to optimize update perf.
function updateTreeData(list: AntDataNode[], key: React.Key, children: AntDataNode[]): AntDataNode[] {
    return list.map(node => {
        if (node.key === key) {
            return {
                ...node,
                children,
            };
        } else if (node.children) {
            return {
                ...node,
                children: updateTreeData(node.children, key, children),
            };
        }
        return node;
    });
}

const CommonNodeTree: React.FC<IState> = (props) => {
    const {setRenderRightFlag} = props;
    const {setNodeId} = props;
    const [treeData, setTreeData] = useState(props.initTreeData);
    const [projectId, setProjectId] = useState(props.projectId);
    const [dataTypeId, setDataTypeId] = useState(props.dataTypeId);
    const [contextMenuPosition, setContextMenuPosition] = useState(rightMenuInitStyle);
    const [rightMenuKeys, setRightMenuKeys] = useState(rightMenuInitKeys);
    const [currNode, setCurrNode] = useState({} as AntDataNode);
    const [titleNodeEdit, setTitleNodeEdit] = useState('新增节点');
    const [visibleNodeEdit, setVisibleNodeEdit] = useState(false);
    const [confirmSavingNodeEdit, setConfirmSavingNodeEdit] = useState(false);
    const [isFolderFormItemHidden, setIsFolderFormItemHidden] = useState(false);
    const [copyNode, setCopyNode] = useState({} as AntDataNode);
    const [expandedKeys, setExpandedKeys] = useState(['1-1']);
    const [selectedKeys, setSelectedKeys] = useState(['1-1']);
    const [initialValues, setInitialValues] = useState({isFolder: 0, name: '', description: ''});
    if(projectId !== props.projectId) {
        setProjectId(props.projectId);
    }
    if(dataTypeId !== props.dataTypeId) {
        setDataTypeId(props.dataTypeId);
    }
    if(projectId !== props.projectId || dataTypeId !== props.dataTypeId) {
        setTreeData(props.initTreeData);
        onLoadData({key: '1-1', children: undefined}).then(() => {});
    }
    const [ref] = useState(React.createRef<FormInstance>());

    if(!WindowTopUtils.getWindowTopObject(WindowTopUtils.object_setExpandedKeys)) {
        WindowTopUtils.setWindowTopObject(WindowTopUtils.object_setExpandedKeys, setExpandedKeys);
    }

    function onLoadData(node: any) {
        return new Promise<void>(resolve => {
            if(!projectId || !dataTypeId) {
                resolve();
                return ;
            }
            if (node.children) {
                resolve();
                return;
            }
            axios.post(ApiUrlConfig.GET_NODES_TREE_URL, {projectId: props.projectId, dataTypeId: props.dataTypeId, parentId: node.key.split('-')[0]}).then(resp => {
                if (resp.status !== 200) {
                    message.error('获取数据失败');
                } else {
                    const ret = resp.data;
                    if (ret.code !== 0) {
                        message.error(ret.message);
                    } else {
                        const children = ret.data.map(v => {
                            const n: AntDataNode = {title: null, dataNode: undefined, isLeaf: false, key: ''};
                            n.title = <Tooltip overlayClassName="small-font-size" title={v.name} color="#2db7f5" placement="rightTop">
                                <span>{v.name}</span>
                            </Tooltip>;
                            n.key = v.id + '-' + v.dataTypeId;
                            n.isLeaf = v.isFolder === 1 ? false : true;
                            n.dataNode = v;
                            n.parentNode = node;
                            return n;
                        });
                        setTreeData(origin =>
                            updateTreeData(origin, node.key, [...children]),
                        );
                        if(expandedKeys.indexOf(node.key) < 0) {
                            setExpandedKeys([...expandedKeys, node.key]);
                        }
                        resolve();
                    }
                }
            });
        });
    }

    function onDragEnter (info) {
        //console.log(info);
        // expandedKeys 需要受控时设置
        // this.setState({
        //   expandedKeys: info.expandedKeys,
        // });
    };

    function dropAfter(info, newParentId) {
        const dropKey = info.node.key;
        const dragKey = info.dragNode.key;
        const dropPos = info.node.pos.split('-');
        const dropPosition = info.dropPosition - Number(dropPos[dropPos.length - 1]);
        const loop = (data, key, callback) => {
            for (let i = 0; i < data.length; i++) {
                if (data[i].key === key) {
                    return callback(data[i], i, data);
                }
                if (data[i].children) {
                    loop(data[i].children, key, callback);
                }
            }
        };
        const data = [...treeData];

        // Find dragObject
        let dragObj;
        loop(data, dragKey, (item, index, arr) => {
            arr.splice(index, 1);
            dragObj = item;
            dragObj.dataNode.parentId = newParentId;
        });

        if (!info.dropToGap) {
            // Drop on the content
            loop(data, dropKey, item => {
                item.children = item.children || [];
                // where to insert 示例添加到头部，可以是随意位置
                item.children.unshift(dragObj);
            });
        } else if (
            (info.node.props.children || []).length > 0 && // Has children
            info.node.props.expanded && // Is expanded
            dropPosition === 1 // On the bottom gap
        ) {
            loop(data, dropKey, item => {
                item.children = item.children || [];
                // where to insert 示例添加到头部，可以是随意位置
                item.children.unshift(dragObj);
                // in previous version, we use item.children.push(dragObj) to insert the
                // item to the tail of the children
            });
        } else {
            let ar;
            let i;
            loop(data, dropKey, (item, index, arr) => {
                ar = arr;
                i = index;
            });
            if (dropPosition === -1) {
                ar.splice(i, 0, dragObj);
            } else {
                ar.splice(i + 1, 0, dragObj);
            }
        }

        setTreeData(data);
    }

    function allowDrop({ dropNode, dropPosition }) {
        //console.log(dropNode);
        //console.log(dropPosition);
        if(dropNode.isLeaf && dropPosition === 0) {
            return false;
        }
        return true;
    }

    function onSelect(selectedKeys, e:{selected: boolean, selectedNodes, node, event}) {
        if(e.node.isLeaf) {
            setRenderRightFlag(Number(dataTypeId));
            setNodeId(e.node.dataNode.id);
        } else {
            setRenderRightFlag(DataTypeEnum.ALL);
        }
        if(e.node.dataNode) {
            setSelectedKeys([e.node.dataNode.id + '-' + Number(dataTypeId)]);
        }
        WindowTopUtils.setWindowTopObject(WindowTopUtils.object_expandedKeys, expandedKeys);
    }

    function onDrop(info) {
        //console.log(info);
        const dropKey = info.node.key;
        const moveInfo = {prevId: 0,
            id: info.dragNode.dataNode.id,
            parentId: 0,
            projectId: projectId,
            dataTypeId: dataTypeId
        };
        if(!info.dropToGap) {
            if(dropKey === '1-1') {
                moveInfo.parentId = 1;
            }else{
                moveInfo.parentId = info.node.dataNode.id;
            }
        }else{
            moveInfo.prevId = info.node.dataNode.id;
            moveInfo.parentId = info.node.dataNode.parentId;
        }
        axios.post(ApiUrlConfig.MOVE_NODE_URL, moveInfo).then(resp => {
            if (resp.status !== 200) {
                message.error('移动节点失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    dropAfter(info, moveInfo.parentId);
                }
            }
        });
    }

    function hideRightMenu(e) {
        setContextMenuPosition({
            width: 170,
            display: 'none',
            position: 'absolute',
            top: '10000px', left: '10000px',
            borderRadius: '3px',
            zIndex: 20 });
        e.stopPropagation();
    }

    useEffect(() => {
        document.addEventListener('click', hideRightMenu);
        return () => {
            document.removeEventListener('click', ()=>{});
        }
    }, []);

    function handleAddNode() {
        setTitleNodeEdit('新增节点');
        setIsFolderFormItemHidden(false);
        setVisibleNodeEdit(true);
        if(ref.current) {
            ref.current.setFieldsValue({isFolder: 0, name: '', description: ''});
        }
    }

    function handleModifyNode() {
        setTitleNodeEdit('修改节点');
        setIsFolderFormItemHidden(true);
        const values = {
            isFolder: currNode.dataNode?.isFolder || 0,
            name: currNode.dataNode?.name || '',
            description: currNode.dataNode?.description || ''
        };
        setInitialValues(values);
        if(ref.current) {
            ref.current.setFieldsValue(values);
        }
        setVisibleNodeEdit(true);
    }

    function handleDeleteNode() {
        if(!window.confirm('确认删除吗？')) {
            return ;
        }
        axios.post(ApiUrlConfig.DELETE_NODE_URL,
            {dataTypeId: dataTypeId, projectId: projectId, id: currNode.dataNode?.id}).then(resp => {
            if (resp.status !== 200) {
                message.error('操作失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    onLoadData({key: currNode.parentNode.key || currNode.key, children: undefined}).then(() => {
                    });
                }
            }
        });
    }

    function handleCopyNode() {
        setCopyNode(currNode);
        message.success('复制[' + currNode.dataNode?.name + ']成功');
    }

    function handlePasteNode() {
        let prevId: any = currNode.dataNode?.id;
        let parentId: any = currNode.dataNode?.parentId;
        let key: any = (currNode.parentNode && currNode.parentNode.key) || '1-1';
        if(!currNode.isLeaf) {
            prevId = null;
            parentId = currNode.dataNode?.id;
            key = currNode.key;
        }
        axios.post(ApiUrlConfig.COPY_NODE_URL,
            {
                dataTypeId: dataTypeId,
                projectId: projectId,
                id: copyNode.dataNode?.id,
                parentId: parentId,
                prevId: prevId
            }).then(resp => {
            if (resp.status !== 200) {
                message.error('操作失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    onLoadData({key: key || currNode.key, children: undefined}).then(() => {
                    });
                }
            }
        });
    }

    function handleRefreshNode() {
        onLoadData({key: currNode.key, children: undefined}).then(() => {
        });
    }

    function onClickRightMenuItem({ item, key, keyPath, domEvent }) {
        switch (key) {
            case '1':
                handleAddNode();
                break;
            case '2':
                handleModifyNode();
                break;
            case '3':
                handleDeleteNode();
                break;
            case '4':
                handleCopyNode();
                break;
            case '5':
                handlePasteNode();
                break;
            case '99':
                handleRefreshNode();
                break;
            default:
                break;
        }
    }

    function renderRightMenu() {
        const list = rightMenuKeys.map(v => {
            return {label: v.title, key: v.key, disabled: v.disabled};
        });
        return list;
    }

    function setRightMenuStatus(node) {
        rightMenuKeys.map(v => {
            v.disabled = false;
            return null;
        });
        if(node.isLeaf) {
            rightMenuKeys.map(v => {
                if(v.key === '1' || v.key === '99') {
                    v.disabled = true;
                }
                return null;
            });
        }
        if(!node.isLeaf) {
            rightMenuKeys.map(v => {
                if(v.key === '1' || v.key === '99') {
                    v.disabled = false;
                }
                if(node.key === '1-1' && (v.key === '2' || v.key === '3' || v.key === '4')) {
                    v.disabled = true;
                }else{
                    v.disabled = false;
                }
                return null;
            });
        }
        setRightMenuKeys(rightMenuKeys);
    }

    function onExpand(expandedKeys, {expanded: bool, node}) {
        //console.log(expandedKeys);
        //console.log(bool);
        //console.log(node);
        if(!bool) {
            const ind = expandedKeys.indexOf(node.key);
            if(ind > -1) {
                expandedKeys.shift(ind, 1);
            }
        }
        setExpandedKeys(expandedKeys);
    }

    function onRightClick({event, node}: any) {
        //console.log(event);
        //console.log(node);
        setCurrNode(node);

        setRightMenuStatus(node);
        const x = event.clientX + 'px';
        const y = event.clientY + 'px';
        setTimeout(function () {
            setContextMenuPosition({
                width: 170,
                display: 'block',
                position: 'absolute',
                top: y, left: x,
                borderRadius: '3px',
                zIndex: 20
            });
        }, 100);
    }

    const layout = {
        labelCol: { span: 6 },
        wrapperCol: { span: 18 },
    };

    function handleOkNodeEdit() {
        if(ref.current != null) {
            const values = ref.current.getFieldsValue();
            if(!values.name || values.name.trim() === '') {
                message.info('名称不能为空');
                return;
            }
            if(!values.isFolder) {
                values.isFolder = 0;
            }
            values.dataTypeId = dataTypeId;
            values.projectId = projectId;
            let url = ApiUrlConfig.ADD_NODE_URL;
            if(titleNodeEdit === '新增节点') {
                values.parentId = currNode.dataNode?.id || 1;
                values.id = 0;
            }
            if(titleNodeEdit === '修改节点') {
                url = ApiUrlConfig.UPDATE_NODE_URL;
                values.id = currNode.dataNode?.id;
                values.isFolder = currNode.dataNode?.isFolder;
                values.parentId = currNode.dataNode?.parentId || 1;
                values.prevId = -1;
            }
            setConfirmSavingNodeEdit(true);
            axios.post(url, values).then(resp => {
                if (resp.status !== 200) {
                    message.error('操作失败');
                } else {
                    const ret = resp.data;
                    if (ret.code !== 0) {
                        message.error(ret.message);
                    } else {
                        if(titleNodeEdit === '新增节点') {
                            onLoadData({key: currNode.key, children: undefined}).then(() => {
                            });
                        }
                        if(titleNodeEdit === '修改节点') {
                            onLoadData({key: currNode.parentNode.key || currNode.key, children: undefined}).then(() => {
                            });
                        }
                        setVisibleNodeEdit(false);
                        message.success('操作成功');
                    }
                }
            }).finally(() => {
                setConfirmSavingNodeEdit(false);
            });
        }
    }

    function handleCancelNodeEdit() {
        setVisibleNodeEdit(false);
    }

    return (
        <div>
            <Tree
                className="draggable-tree"
                loadData={onLoadData}
                onRightClick={onRightClick}
                onExpand={onExpand}
                defaultExpandedKeys={defaultExpandedKeys}
                draggable
                blockNode
                expandedKeys={expandedKeys}
                showLine={false}
                onDragEnter={onDragEnter}
                onDrop={onDrop}
                onSelect={onSelect}
                selectedKeys={selectedKeys}
                allowDrop={allowDrop}
                treeData={treeData}
            />
            <Menu onClick={onClickRightMenuItem} className="node-tree-context-menu" style={contextMenuPosition as any} mode="vertical" items={renderRightMenu()}>
            </Menu>
            <Modal
                title={titleNodeEdit}
                open={visibleNodeEdit}
                onOk={handleOkNodeEdit}
                confirmLoading={confirmSavingNodeEdit}
                onCancel={handleCancelNodeEdit}
            >
                <Form
                    {...layout}
                    name="nodeForm"
                    initialValues={initialValues}
                    ref={ref}
                >
                    <Form.Item
                        label="节点类型"
                        name="isFolder"
                        hidden={isFolderFormItemHidden}
                        rules={[{required: true}]}
                    >
                        <Radio.Group defaultValue={0}>
                            <Radio value={0}>非目录</Radio>
                            <Radio value={1}>目录</Radio>
                        </Radio.Group>
                    </Form.Item>

                    <Form.Item
                        label="名称"
                        name="name"
                        rules={[{required: true, message: '请输入名称!' }]}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        label="描述"
                        name="description"
                        rules={[{required: true, message: '请输入描述!' }]}
                    >
                        <Input.TextArea />
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
};
export {CommonNodeTree};
