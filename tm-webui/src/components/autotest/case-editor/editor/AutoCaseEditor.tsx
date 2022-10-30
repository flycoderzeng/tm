import React, {useState, useEffect} from 'react';
import {Button, Menu, message, Tree} from "antd";
import {RootNodeEditor} from "./RootNodeEditor";
import {
    SettingOutlined,
    PlusOutlined,
    DeleteOutlined,
    CopyOutlined,
    PlayCircleOutlined,
    PauseOutlined
} from '@ant-design/icons';
import axios from "axios";
import {ApiUrlConfig} from "../../../../config/api.url";
import {CommonNameComments} from "./CommonNameComments";
import {StepNode} from "../entities/StepNode";
import {IfControllerEditor} from "../logic/IfControllerEditor";
import {WhileControllerEditor} from '../logic/WhileControllerEditor';
import {LoopControllerEditor} from "../logic/LoopControllerEditor";
import {ActionType} from "../entities/ActionType";
import {HttpEditor} from "./HttpEditor";
import {PlatformApiEditor} from "./PlatformApiEditor";
import {RunEnvSelect} from "../../../testmanage/runenv/RunEnvSelect";

import {RandomUtils} from "../../../../utils/RandomUtils";
import {JDBCRequestEditor} from "./JDBCRequestEditor";
import {LocalStorageUtils} from "../../../../utils/LocalStorageUtils";

const {SubMenu} = Menu;

interface IState {
    id?: number | null,
}

interface MenuItem {
    key: string,
    title: string,
    disabled: boolean,
    id?: number,
    dataId?: number,
    dataType?: number,
    icon?: any,
    children?: MenuItem[]
}

const defaultExpandedKeys = ['1'];
const rightMenuInitStyle = {
    width: 200,
    display: 'none',
    position: 'absolute',
    top: '10000px',
    left: '10000px',
    borderRadius: '3px',
    zIndex: 20
};

const MenuKey = {
    'AddResource': '100',
    'AddRecent': '200',
    'AddMostCommonlyUsed': '201',
    'AddPlatformApi': '10000',
    'AddPlatformApiGetRandom': '10001',
    'AddHttpRequest': '202',
    'AddJDBCRequest': '203',
    'Remove': '300',
    'Copy': '400',
    'Paste': '500',
    'Enable': '600',
    'Disable': '700',
    'AddLogic': '800',
    'If': '801',
    'While': '802',
    'Loop': '803'
};

const platformApiTree: MenuItem[] = [];

const rightMenuInitKeys: MenuItem[] = [
    {key: MenuKey.AddResource, title: '添加资源', icon: <PlusOutlined/>, disabled: false, children: []},
    {key: MenuKey.AddRecent, title: '添加最近', icon: <PlusOutlined/>, disabled: false, children: []},
    {
        key: MenuKey.AddMostCommonlyUsed, title: '添加常用', icon: <PlusOutlined/>, disabled: false, children: [
            {key: MenuKey.AddHttpRequest, title: 'HTTP请求', disabled: false},
            {key: MenuKey.AddJDBCRequest, title: 'JDBC请求', disabled: false},
        ]
    },
    {key: MenuKey.AddPlatformApi, title: '添加API', icon: <PlusOutlined/>, disabled: false, children: platformApiTree},
    {key: MenuKey.Remove, title: '删除', icon: <DeleteOutlined/>, disabled: false},
    {key: MenuKey.Copy, title: '复制', icon: <CopyOutlined/>, disabled: false},
    {key: MenuKey.Paste, title: '粘贴', icon: <SettingOutlined/>, disabled: false},
    {key: MenuKey.Enable, title: '启用', icon: <PlayCircleOutlined/>, disabled: false},
    {key: MenuKey.Disable, title: '禁用', icon: <PauseOutlined/>, disabled: false},
    {
        key: MenuKey.AddLogic, title: '添加逻辑', icon: <PlusOutlined/>, disabled: false, children: [
            {key: MenuKey.If, title: 'if', icon: null, disabled: false},
            {key: MenuKey.While, title: 'while', icon: null, disabled: false},
            {key: MenuKey.Loop, title: 'loop', icon: null, disabled: false}
        ]
    }
];

const whenClickLeafStepNodeDisabledMenuItems: string[] = [MenuKey.AddResource, MenuKey.AddPlatformApi,
    MenuKey.AddRecent, MenuKey.AddMostCommonlyUsed, MenuKey.AddLogic];

const initTreeData: StepNode[] = [{
    "type": "root",
    "level": 1,
    "define": {
        "userDefinedVariables": [],
        "cookies": [],
        "groups": [],
        "name": "自动化用例",
        "comments": "",
        "enabled": true,
    },
    "title": "自动化用例",
    "key": "1",
    "isLeaf": false,
    disabled: false,
    children: [
        {
            "type": "setUp",
            "level": 2,
            "define": {
                "name": "",
                "comments": "",
                "enabled": true,
            },
            "children": [],
            "isLeaf": false,
            "title": "setUp",
            "key": "2",
            disabled: false,
        }, {
            "type": "action",
            "level": 2,
            "define": {
                "name": "",
                "comments": "",
                "enabled": true,
            },
            "children": [],
            "isLeaf": false,
            "title": "action",
            "key": "3",
            disabled: false,
        }, {
            "type": "teardown",
            "level": 2,
            "define": {
                "name": "",
                "comments": "",
                "enabled": true,
            },
            "children": [],
            "isLeaf": false,
            "title": "teardown",
            "key": "4",
            disabled: false,
        }
    ]
}];

const AutoCaseEditor: React.FC<IState> = (props) => {
    //let history = useHistory();
    const [saving, setSaving] = useState(false);
    const [running1, setRunning1] = useState(false);
    const [running2, setRunning2] = useState(false);
    const [loadedPlatformApi, setLoadedPlatformApi] = useState(false);
    const [id, setId] = useState(props.id);
    const [expandedKeys, setExpandedKeys] = useState(['1', '2', '3', '4']);
    const [selectedKeys, setSelectedKeys] = useState<string[]>([]);
    const [contextMenuPosition, setContextMenuPosition] = useState(rightMenuInitStyle);
    const [rightMenuKeys, setRightMenuKeys] = useState(rightMenuInitKeys);
    const [treeData, setTreeData] = useState<StepNode[]>(initTreeData);
    const [currStepNode, setCurrStepNode] = useState<StepNode>(treeData[0]);
    const [rootNode, setRootNode] = useState<StepNode>(treeData[0]);
    const [runEnvId, setRunEnvId] = useState('');
    const [copyNodeData, setCopyNodeData] = useState<StepNode|null>(null);

    if (id !== props.id) {
        setId(props.id);
    }

    function loadPlatformApiTree() {
        if (!loadedPlatformApi) {
            axios.get(ApiUrlConfig.GET_PLATFORM_API_TREE_URL).then(resp => {
                if (resp.status !== 200) {
                    message.error('加载平台api失败');
                } else {
                    const ret = resp.data;
                    if (ret.code !== 0) {
                        message.error(ret.message);
                    } else if (!ret.data) {

                    } else {
                        setLoadedPlatformApi(true);
                        if (platformApiTree.length > 0) {
                            return;
                        }
                        for (let i = 0; i < ret.data.length; i++) {
                            const firstLevel: MenuItem = {
                                key: 'platform_api_' + ret.data[i].id + '$' + ret.data[i].name,
                                id: ret.data[i].id,
                                title: ret.data[i].name,
                                disabled: false, children: []
                            };
                            platformApiTree.push(firstLevel);

                            const children = ret.data[i].children || [];
                            if (!children || children.length < 1) {
                                continue;
                            }
                            for (let j = 0; j < children.length; j++) {
                                firstLevel.children !== undefined && firstLevel.children.push({
                                    key: 'platform_api_' + children[j].id + '$' + children[j].name,
                                    id: children[j].id,
                                    title: children[j].name,
                                    disabled: false, children: []
                                });
                            }
                        }
                    }
                }
            });
        }
    }

    function findStepNode(dataList: StepNode[], key: string, findParent: boolean, parentNode?: StepNode): StepNode|null {
        for (let i = 0; i < dataList.length; i++) {
            if (dataList[i].key === key) {
                if(findParent) {
                    return parentNode||null;
                }
                return dataList[i];
            }
            if (dataList[i].children && dataList[i].children.length > 0) {
                const node = findStepNode(dataList[i].children, key, findParent, dataList[i]);
                if (node !== null) {
                    return node;
                }
            }
        }
        return null;
    }

    function refreshTree(actionType: ActionType, key: string, value: any) {
        const node: StepNode|null = findStepNode(treeData, key, false);
        if (node === null) {
            return;
        }
        node.title = value.toString();
        setTreeData([...treeData]);
    }

    function onExpand(expandedKeys, {expanded: bool, node}) {
        //console.log(expandedKeys);
        //console.log(bool);
        //console.log(node);
        if (!bool) {
            const ind = expandedKeys.indexOf(node.key);
            if (ind > -1) {
                expandedKeys.shift(ind, 1);
            }
        }
        setExpandedKeys(expandedKeys);
    }

    function hideRightMenu(e) {
        setContextMenuPosition({
            width: 170,
            display: 'none',
            position: 'absolute',
            top: '10000px', left: '10000px',
            borderRadius: '3px',
            zIndex: 20
        });
        e.stopPropagation();
    }

    useEffect(() => {
        document.addEventListener('click', hideRightMenu);
        return () => {
            document.removeEventListener('click', () => {
            });
        }
    }, []);

    useEffect(() => {
        load();
        loadPlatformApiTree();
    }, [id]);// eslint-disable-line react-hooks/exhaustive-deps

    function load() {
        axios.post(ApiUrlConfig.LOAD_AUTO_CASE_URL, {id: id}).then(resp => {
            if (resp.status !== 200) {
                message.error('加载用例失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    let steps;
                    if (ret.data.steps) {
                        steps = JSON.parse(ret.data.steps);
                    } else {
                        steps = JSON.parse(JSON.stringify(initTreeData));
                    }
                    setTreeData(steps);
                    setCurrStepNode(steps[0]);
                    setRootNode(steps[0]);
                    setRunEnvId(ret.data.lastRunEnvId + '');
                }
            }
        });
    }

    function onRightClick({event, node}: any) {
        setCurrStepNode(node);
        if (node.key === '1') {
            hideRightMenu(event);
            return;
        }

        if (node.isLeaf) {
            for (let i = 0; i < rightMenuKeys.length; i++) {
                if (whenClickLeafStepNodeDisabledMenuItems.indexOf(rightMenuKeys[i].key) > -1) {
                    rightMenuKeys[i].disabled = true;
                }else{
                    rightMenuKeys[i].disabled = false;
                }
            }
            setRightMenuKeys(rightMenuKeys);
        } else {
            for (let i = 0; i < rightMenuKeys.length; i++) {
                rightMenuKeys[i].disabled = false;
                if(node.key === '1' || node.key === '2'
                    || node.key === '3' || node.key === '4') {
                    if(rightMenuKeys[i].key === MenuKey.Remove) {
                        rightMenuKeys[i].disabled = true;
                    }
                }
            }
        }

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

    function onDragEnter(info) {
        //console.log(info);
        // expandedKeys 需要受控时设置
        // this.setState({
        //   expandedKeys: info.expandedKeys,
        // });
    }

    function onDrop(info) {
        //console.log(info);
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

    function onSelect(selectedKeys, e: { selected: boolean, selectedNodes, node, event }) {
        setSelectedKeys(selectedKeys);
        setCurrStepNode(e.node);
        if (e.node.isLeaf) {

        } else {

        }
    }

    function allowDrop({dropNode, dropPosition}) {
        //console.log(dropNode);
        //console.log(dropPosition);
        if (dropNode.isLeaf && dropPosition === 0) {
            return false;
        }
        if (dropNode.key === '1') {
            return false;
        }
        return true;
    }

    function renderRightMenu() {
        const list = rightMenuKeys.map(v => {
            if (!v.children || v.children.length < 1) {
                return (<Menu.Item icon={v.icon} key={v.key} disabled={v.disabled}>
                    {v.title}
                </Menu.Item>);
            } else if (v.children && v.children.length > 0) {
                const subMenuList = v.children.map(subMenu => {
                    if (!subMenu.children || subMenu.children.length < 1) {
                        return (<Menu.Item icon={subMenu.icon} key={subMenu.key} disabled={subMenu.disabled}>
                            {subMenu.title}
                        </Menu.Item>);
                    } else if (subMenu.children && subMenu.children.length > 0) {
                        const subSubMenuList = subMenu.children.map(subSubMenu => {
                            return (<Menu.Item key={subSubMenu.key}
                                               disabled={subSubMenu.disabled}>{subSubMenu.title}</Menu.Item>)
                        });
                        return (<SubMenu disabled={subMenu.disabled} key={subMenu.key} icon={subMenu.icon}
                                         title={subMenu.title}>
                            {subSubMenuList}
                        </SubMenu>)
                    }
                    return undefined;
                });
                return (<SubMenu disabled={v.disabled} key={v.key} icon={v.icon} title={v.title}>
                    {subMenuList}
                </SubMenu>)
            }
            return undefined;
        });
        return list;
    }

    function getNewTreeNode(define: any, type: string, isLeaf: boolean, title: string, parentLevel: number): StepNode {
        const node: StepNode = {
            type: type,
            isLeaf: isLeaf, title: title,
            level: parentLevel + 1,
            key: RandomUtils.getKey(),
            children: [], define: define,
            disabled: false,
        };
        return node;
    }

    function addTreeNode(define: any, type: string, isLeaf: boolean, title: string) {
        const node: StepNode = getNewTreeNode(define, type, isLeaf, title, currStepNode.level);
        currStepNode.children.push(node);
        setTreeData([...treeData]);
        if (expandedKeys.indexOf(currStepNode.key) < 0) {
            expandedKeys.push(currStepNode.key);
            setExpandedKeys(expandedKeys);
        }
    }

    function addNode(type: string, isLeaf: boolean) {
        const define = getDefine(type);
        addTreeNode(define, type, isLeaf, type);
    }

    function getDefine(type: string): any {
        let define;
        if (type === 'if' || type === 'while' || type === 'loop') {
            define = {name: type, comments: '', condition: '', enabled: true};
        } else if (type === 'http request') {
            define = {
                name: '发送http请求', comments: '', enabled: true, requestType: 'POST',
                url: '', params: [], headers: [], bodyType: 'raw', rawType: 'json', formData: [],
                formUrlencoded: [], content: '', checkErrorList: [], responseExtractorList: [],
            };
        }  else if (type === 'jdbc request') {
            define = {
                name: '执行数据库操作', comments: '', enabled: true, dbName: '',
                resultSetVariableName: '',
                content: '', checkErrorList: [], responseExtractorList: [],
            };
        } else if (type.startsWith('调用平台API(')) {
            define = {name: type, comments: '', parametricList: [], platformApiId: -1, enabled: true};
        }
        return define;
    }

    function onAddIfNode() {
        addNode('if', false);
    }

    function onAddWhileNode() {
        addNode('while', false);
    }

    function onAddLoopNode() {
        addNode('loop', false);
    }

    function onAddHttpRequestNode() {
        addNode('http request', true);
    }

    function onAddJDBCRequestNode() {
        addNode('jdbc request', true);
    }

    function onAddPlatformApi(key: string) {
        const id = key.substr('platform_api_'.length, key.indexOf('$') + 1 - 'platform_api_'.length);
        const name = key.substr(key.indexOf('$') + 1);
        const define = getDefine('调用平台API(' + name + ")");
        define.platformApiId = parseInt(id);
        define.parametricList = [];
        addTreeNode(define, '调用平台API(' + name + ")", true, name);
    }

    function getCopyNodeData(copyStepNode: StepNode) {
        const defineStr = JSON.stringify(copyStepNode.define);
        const copyDefine = JSON.parse(defineStr);
        const newTreeNode = getNewTreeNode(copyDefine, copyStepNode.type,
            copyStepNode.isLeaf, copyStepNode.title, copyStepNode.level);
        const children: {node: StepNode, parentNode: StepNode} [] = [];
        if(copyStepNode.children && copyStepNode.children.length > 0) {
            for (let i = 0; i < copyStepNode.children.length; i++) {
                children.push({node: copyStepNode.children[i], parentNode: newTreeNode});
            }
        }
        while (children.length > 0) {
            const stepNode: {node: StepNode, parentNode: StepNode} = children[0];
            children.shift();
            const defineStr = JSON.stringify(stepNode.node.define);
            const copyDefine = JSON.parse(defineStr);
            const newTreeNode = getNewTreeNode(copyDefine, stepNode.node.type,
                stepNode.node.isLeaf, stepNode.node.title, stepNode.node.level);
            stepNode.parentNode.children.push(newTreeNode);
            if(stepNode.node.children && stepNode.node.children.length > 0) {
                for (let i = 0; i < stepNode.node.children.length; i++) {
                    children.push({node: stepNode.node.children[i], parentNode: newTreeNode});
                }
            }
        }

        return newTreeNode;
    }

    function onCopyNode() {
        const copyNodeData = getCopyNodeData(currStepNode);
        setCopyNodeData(copyNodeData);
        localStorage.setItem(LocalStorageUtils.COPY_STEP_NODE, JSON.stringify(copyNodeData));
    }

    function updateLevelAndKey(): StepNode | null {
        const item = localStorage.getItem(LocalStorageUtils.COPY_STEP_NODE);
        let data: StepNode | null = copyNodeData;
        if (item) {
            try {
                data = JSON.parse(item);
            } catch (e) {
                data = null;
            }
        }
        if (!data) {
            data = copyNodeData;
        }
        if (!data) {
            return null;
        }
        // 更新level和key
        if (currStepNode.isLeaf) {
            data.level = currStepNode.level;
        } else {
            data.level = currStepNode.level + 1;
        }
        data.key = RandomUtils.getKey();
        const children: { node: StepNode, parentNode: StepNode } [] = [];
        if (data.children && data.children.length > 0) {
            for (let i = 0; i < data.children.length; i++) {
                children.push({node: data.children[i], parentNode: data});
            }
        }
        while (children.length > 0) {
            const stepNode: { node: StepNode, parentNode: StepNode } = children[0];
            children.shift();
            stepNode.node.level = stepNode.parentNode.level + 1;
            stepNode.node.key = RandomUtils.getKey();
            if (stepNode.node.children && stepNode.node.children.length > 0) {
                for (let i = 0; i < stepNode.node.children.length; i++) {
                    children.push({node: stepNode.node.children[i], parentNode: stepNode.node});
                }
            }
        }
        return data;
    }

    function onPasteNode() {
        const data = updateLevelAndKey();
        if(!data) {
            message.info('请先复制步骤');
            return;
        }
        if(currStepNode.isLeaf) {
            const node: StepNode|null = findStepNode(treeData, currStepNode.key, true);
            if (node === null) {
                return;
            }
            for (let i = 0; i < node.children.length; i++) {
                if (node.children[i].key === currStepNode.key) {
                    node.children.splice(i + 1, 0, data);
                }
            }
        }else{
            currStepNode.children.push(data);
        }
        setTreeData([...treeData]);
        if (!data.isLeaf && expandedKeys.indexOf(data.key) < 0) {
            expandedKeys.push(data.key);
            setExpandedKeys([...expandedKeys]);
        }
        setSelectedKeys([data.key]);
    }

    function onEnableNode() {
        const node: StepNode|null = findStepNode(treeData, currStepNode.key, false);
        if(node !== null) {
            node.disabled = false;
            node.define.enabled = true;
            setTreeData([...treeData]);
        }
    }

    function onDisableNode() {
        const node: StepNode|null = findStepNode(treeData, currStepNode.key, false);
        if(node !== null) {
            node.disabled = true;
            node.define.enabled = false;
            setTreeData([...treeData]);
        }
    }

    function onRemoveNode() {
        if(!window.confirm('确认删除吗?')) {
            return ;
        }
        const node: StepNode|null = findStepNode(treeData, currStepNode.key, true);
        if(node !== null) {
            for (let i = 0; i < node.children.length; i++) {
                if(node.children[i].key === currStepNode.key) {
                    node.children.splice(i, 1);
                    break;
                }
            }
            setTreeData([...treeData]);
        }
    }

    function onClickRightMenuItem({item, key, keyPath, domEvent}) {
        let platformApiKey;
        if (key.startsWith('platform_api_')) {
            platformApiKey = key;
        }
        switch (key) {
            case MenuKey.If:
                onAddIfNode();
                break;
            case MenuKey.While:
                onAddWhileNode();
                break;
            case MenuKey.Loop:
                onAddLoopNode();
                break;
            case MenuKey.AddHttpRequest:
                onAddHttpRequestNode();
                break;
            case MenuKey.AddJDBCRequest:
                onAddJDBCRequestNode();
                break;
            case MenuKey.Copy:
                onCopyNode();
                break;
            case MenuKey.Paste:
                onPasteNode();
                break;
            case MenuKey.Enable:
                onEnableNode();
                break;
            case MenuKey.Disable:
                onDisableNode();
                break;
            case MenuKey.Remove:
                onRemoveNode();
                break;
            case platformApiKey:
                onAddPlatformApi(key);
                break;
            case '99':
                break;
            default:
                break;
        }
    }

    function onSave() {
        setSaving(true);
        const data = {id: id, type: 1, steps: JSON.stringify(treeData)};
        axios.post(ApiUrlConfig.SAVE_AUTO_CASE_URL, data).then(resp => {
            if (resp.status !== 200) {
                message.error('保存失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                }
            }
        }).finally(() => {
            setSaving(false);
        });
    }

    function renderRightPanel() {
        switch (currStepNode.type) {
            case "root":
                return (<RootNodeEditor refreshTree={refreshTree} stepNode={currStepNode}
                                        define={currStepNode.define}>
                </RootNodeEditor>);
            case "if":
                return (<IfControllerEditor refreshTree={refreshTree} stepNode={currStepNode}
                                            define={currStepNode.define}>
                </IfControllerEditor>);
            case "while":
                return (<WhileControllerEditor refreshTree={refreshTree} stepNode={currStepNode}
                                               define={currStepNode.define}>
                </WhileControllerEditor>);
            case 'loop':
                return (<LoopControllerEditor refreshTree={refreshTree}
                                              stepNode={currStepNode}
                                              define={currStepNode.define}>
                </LoopControllerEditor>);
            case 'http':
            case 'http request':
                return (
                    <HttpEditor userDefinedVariables={rootNode.define.userDefinedVariables}
                                refreshTree={refreshTree}
                                stepNode={currStepNode} define={currStepNode.define}>
                    </HttpEditor>);
            case 'jdbc':
            case 'jdbc request':
                return (<JDBCRequestEditor userDefinedVariables={rootNode.define.userDefinedVariables}
                                           refreshTree={refreshTree}
                                           stepNode={currStepNode} define={currStepNode.define}>
                </JDBCRequestEditor>);
            default:
                if (currStepNode.type.startsWith('调用平台API(')) {
                    return (<PlatformApiEditor userDefinedVariables={rootNode.define.userDefinedVariables}
                                               stepNode={currStepNode} define={currStepNode.define}
                                               refreshTree={refreshTree}>
                    </PlatformApiEditor>);
                }
                return (<CommonNameComments refreshTree={refreshTree} stepNode={currStepNode}
                                            define={currStepNode.define}>
                </CommonNameComments>)
        }
    }

    function draggable(node: any) {
        if (['1', '2', '3', '4'].indexOf(node.key) > -1) {
            return false;
        }
        return true;
    }

    function onRun(runType) {
        if (!window.confirm('确定运行吗？')) {
            return;
        }
        if (runType === 1) {
            setRunning1(true);
        }
        if (runType === 2) {
            setRunning2(true);
        }
        axios.post(ApiUrlConfig.RUN_CASE_URL,
            {runType: runType, caseId: id, runEnvId: runEnvId}).then(resp => {
            if (resp.status !== 200) {
                message.error('运行失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                }
            }
        }).catch(reason => {
            message.error(reason.response.statusText);
        }).finally(() => {
            if (runType === 1) {
                setRunning1(false);
            }
            if (runType === 2) {
                setRunning2(false);
            }
        });
    }

    function onViewResult() {
        window.open("/planresult/" + id + "/2");
    }

    return (
        <div className="case-editor-parent">
            <div className="case-editor-toolbar">
                <Button size="small" type="primary" loading={saving} onClick={onSave}>保存</Button>
                <Button size="small" type="primary" loading={running1} onClick={() => {
                    onRun(1);
                }}>运行</Button>
                <Button size="small" type="primary" loading={running2} onClick={() => {
                    onRun(2);
                }}>运行组合</Button>
                <Button size="small" type="primary" danger>停止</Button>
                <Button size="small" type="default">查看内置函数与变量</Button>
                <Button size="small" type="primary" onClick={() => {
                    onViewResult();
                }}>查看运行结果</Button>
                <RunEnvSelect onChange={setRunEnvId} style={{width: '150px'}} value={runEnvId}></RunEnvSelect>
            </div>
            <div className="case-editor-main-content">
                <div className="case-editor-step-tree">
                    <Tree
                        className="draggable-tree"
                        onRightClick={onRightClick}
                        onExpand={onExpand}
                        defaultExpandedKeys={defaultExpandedKeys}
                        draggable={draggable}
                        blockNode
                        expandedKeys={expandedKeys}
                        selectedKeys={selectedKeys}
                        showLine={false}
                        onDragEnter={onDragEnter}
                        onDrop={onDrop}
                        onSelect={onSelect}
                        allowDrop={allowDrop}
                        treeData={treeData}
                    />
                </div>
                <div className="case-editor-right-panel">
                    {renderRightPanel()}
                </div>
            </div>
            <Menu onClick={onClickRightMenuItem} className="node-tree-context-menu" style={contextMenuPosition as any}
                  mode="vertical">
                {renderRightMenu()}
            </Menu>
        </div>
    )
}

export {AutoCaseEditor};
