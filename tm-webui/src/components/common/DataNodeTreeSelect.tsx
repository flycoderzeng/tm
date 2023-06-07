import React, {useEffect, useState} from "react";
import {message, Tooltip, Tree} from "antd";
import axios from "axios";
import {ApiUrlConfig} from "../../config/api.url";
import {Key} from "react/index";
import {DataTypeEnumDescription} from "../../entities/DataTypeEnumDescription";

interface IState {
    projectId ?: number|null;
    dataTypeId ?: number|null;
    onChange: any;
}
const initTreeData = [{ title: '自动化用例', key: '1-1' }];

const rootTitle = {
    2: DataTypeEnumDescription.APP_API,
    3: DataTypeEnumDescription.AUTO_SHELL,
    4: DataTypeEnumDescription.GLOBAL_VARIABLE,
    5: DataTypeEnumDescription.PLATFORM_API,
    6: DataTypeEnumDescription.AUTO_CASE,
    7: DataTypeEnumDescription.AUTO_PLAN
}
function updateTreeData(list: any[], key: React.Key, children: any[]): any[] {
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


const DataNodeTreeSelect: React.FC<IState> = (props) => {
    const [checkedKeys, setCheckedKeys] = useState<React.Key[]>([]);
    const [treeData, setTreeData] = useState<any[]>(['1-1']);
    const [projectId, setProjectId] = useState(props.projectId);
    const [dataTypeId, setDataTypeId] = useState(props.dataTypeId);
    const [expandedKeys, setExpandedKeys] = useState<React.Key[]>([]);
    const [autoExpandParent, setAutoExpandParent] = useState<boolean>(true);
    if(props.dataTypeId)
        initTreeData[0].title = rootTitle[props.dataTypeId];
    if(projectId !== props.projectId) {
        setProjectId(props.projectId);
    }
    if(dataTypeId !== props.dataTypeId) {
        setDataTypeId(props.dataTypeId);
    }

    useEffect(() => {
        setTreeData(initTreeData);
        onLoadData({key: '1-1', children: undefined}).then(() => {});
    }, [projectId, dataTypeId]);

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
        setAutoExpandParent(false);
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
                            const n: any = {title: null, dataNode: undefined, isLeaf: false, key: ''};
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
    function onCheck(checkedKeysValue, e): void {
        setCheckedKeys(checkedKeysValue);
        if(props.onChange) {
            const keys:string[] = [];
            for (let i = 0; i < checkedKeysValue.length; i++) {
                const valueElement:string = checkedKeysValue[i];
                const split:string[] = valueElement.split('-');
                if(split && split.length > 0) {
                    keys.push(split[0]);
                }
            }
            props.onChange(keys);
        }
    }

    return (
        <div>
            <Tree
                checkable
                loadData={onLoadData}
                onExpand={onExpand}
                expandedKeys={expandedKeys}
                autoExpandParent={autoExpandParent}
                onCheck={onCheck}
                checkedKeys={checkedKeys}
                treeData={treeData}
            />
        </div>
    )
}
export {DataNodeTreeSelect}