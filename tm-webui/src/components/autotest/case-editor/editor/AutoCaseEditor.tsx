import React, {useEffect, useState} from 'react';
import {Button, Menu, message, Modal, Tree} from "antd";
import {RootNodeEditor} from "./RootNodeEditor";
import {
    CopyOutlined,
    DeleteOutlined,
    PauseOutlined,
    PlayCircleOutlined,
    PlusOutlined,
    QuestionOutlined,
    SettingOutlined
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
import {CaseHistoryList} from "../CaseHistoryList";
import {ScriptActionNodeEditor} from "./ScriptActionNodeEditor";
import {WindowTopUtils} from "../../../../utils/WindowTopUtils";
import {Resizable} from "re-resizable";
import copy from "copy-to-clipboard";
import {AutoCaseVariable} from "../entities/AutoCaseVariable";


interface IState {
    id?: number | null;
    groupManageEditorWidth?: number;
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

const defaultExpandedKeys: string[] = [];
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
    'AddScriptAction': '204',
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
    id: "1",
    "isLeaf": false,
    disabled: false,
    seq: 1,
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
            "id": "2",
            disabled: false,
            seq: 2
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
            "id": "3",
            disabled: false,
            seq: 3,
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
            "id": "4",
            disabled: false,
            seq: 4
        }
    ]
}];

let seq = 5;

let HAPPY_CURR_STEP_NODE: StepNode;

const AutoCaseEditor: React.FC<IState> = (props) => {
    //let history = useHistory();
    const [saving, setSaving] = useState(false);
    const [visibleHistory, setVisibleHistory] = useState(false);
    const [running1, setRunning1] = useState(false);
    const [running2, setRunning2] = useState(false);
    const [id, setId] = useState(props.id);
    const [caseName, setCaseName] = useState('');
    const [expandedKeys, setExpandedKeys] = useState<string[]>([]);
    const [selectedKeys, setSelectedKeys] = useState<string[]>([]);
    const [contextMenuPosition, setContextMenuPosition] = useState(rightMenuInitStyle);
    const [treeData, setTreeData] = useState<StepNode[]>(initTreeData);
    const [currStepNode, setCurrStepNode] = useState<StepNode>(treeData[0]);
    const [rootNode, setRootNode] = useState<StepNode>(treeData[0]);
    const [runEnvId, setRunEnvId] = useState('');
    const [groupVariables, setGroupVariables] = useState<string|null>(null);
    const [copyNodeData, setCopyNodeData] = useState<StepNode|null>(null);
    const [rightMenuKeys, setRightMenuKeys] = useState<MenuItem[]>([]);
    const [rightMenuList, setRightMenuList] = useState<any[]>([]);
    const [treeCheckEnable, setTreeCheckEnable] = useState<boolean>(false);
    const [checkedKeys, setCheckedKeys] = useState<any[]>([]);
    const {groupManageEditorWidth} = props;

    useEffect(() => {
        setId(props.id);
    }, [props.id]);

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
        return () => {

        }
    }, [id]);// eslint-disable-line react-hooks/exhaustive-deps

    function loadPlatformApiTree() {
        axios.get(ApiUrlConfig.GET_PLATFORM_API_TREE_URL).then(resp => {
            if (resp.status !== 200) {
                message.error('加载平台api失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else if (ret.data) {
                    const platformApiTree: MenuItem[] = [];
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

                    const rightMenuInitKeys: MenuItem[] = [
                        //{key: MenuKey.AddResource, title: '添加资源', icon: <PlusOutlined/>, disabled: false, children: []},
                        //{key: MenuKey.AddRecent, title: '添加最近', icon: <PlusOutlined/>, disabled: false, children: []},
                        {
                            key: MenuKey.AddMostCommonlyUsed, title: '添加常用', icon: <PlusOutlined/>, disabled: false, children: [
                                {key: MenuKey.AddHttpRequest, title: 'HTTP请求', disabled: false},
                                {key: MenuKey.AddJDBCRequest, title: 'JDBC请求', disabled: false},
                                {key: MenuKey.AddScriptAction, title: 'Shell脚本', disabled: false},
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
                    setRightMenuKeys(rightMenuInitKeys);
                    setRightMenuList(renderRightMenu(rightMenuInitKeys));
                }
            }
        });
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

    function initStepSeq(steps: StepNode[]) {
        seq = 1;
        const stack: StepNode[] = [steps[0]];
        let init = false;
        let maxSeq = -1;
        if(steps[0].seq) {
            init = true;
        }
        while (true) {
            const stepNode: StepNode|undefined = stack.shift();
            if(stepNode && stepNode.children) {
                for (let i = 0; i < stepNode.children.length; i++) {
                    stack.push(stepNode.children[i]);
                }
            }
            if(stepNode && !stepNode.id) {
                stepNode.id = stepNode.key;
            }
            if(stepNode && stepNode.id && (stepNode.id === '1' || stepNode.id === '2'
                || stepNode.id === '3' || stepNode.id === '4')) {
                defaultExpandedKeys.push(stepNode?.key);
                expandedKeys.push(stepNode?.key);
            }
            if(stepNode && !init) {
                stepNode.seq = seq;
                seq = seq + 1;
            }else if(stepNode && stepNode.seq > maxSeq){
                maxSeq = stepNode.seq;
            }
            if(!stepNode) {
                break;
            }
        }
        if(init) {
            seq = maxSeq + 1;
        }
    }

    function load(hideHistoryModal?: boolean) {
        axios.post(ApiUrlConfig.LOAD_AUTO_CASE_URL, {id: id}).then(resp => {
            if(hideHistoryModal) {
                setVisibleHistory(false);
            }
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
                    initStepSeq(steps);
                    setCaseName(ret.data.name);
                    setExpandedKeys([...expandedKeys]);
                    setCheckedKeys([]);
                    setTreeCheckEnable(false);
                    setTreeData(steps);
                    setCurrStepNode(steps[0]);
                    setSelectedKeys([steps[0].key]);
                    HAPPY_CURR_STEP_NODE = steps[0];
                    setRootNode(steps[0]);
                    setRunEnvId(ret.data.lastRunEnvId == null ? '' : (ret.data.lastRunEnvId + ''));
                    setGroupVariables(ret.data.groupVariables||null);
                    WindowTopUtils.expandLeftTree(ret.data, 1);
                    WindowTopUtils.setWindowTopObject(WindowTopUtils.object_currDataNode, ret.data);
                }
            }
        });
    }

    function onRightClick({event, node}: any) {
        setCurrStepNode(node);
        HAPPY_CURR_STEP_NODE = node;
        setSelectedKeys([node.key]);
        if (node.id === '1') {
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
            setRightMenuKeys([...rightMenuKeys]);
            setRightMenuList(renderRightMenu(rightMenuKeys));
        } else {
            for (let i = 0; i < rightMenuKeys.length; i++) {
                rightMenuKeys[i].disabled = false;
                if(node.id === '1' || node.id === '2'
                    || node.id === '3' || node.id === '4') {
                    if(rightMenuKeys[i].key === MenuKey.Remove) {
                        rightMenuKeys[i].disabled = true;
                    }
                }
            }
            setRightMenuKeys([...rightMenuKeys]);
            setRightMenuList(renderRightMenu(rightMenuKeys));
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
        setCurrStepNode(prevState => {
            const n: any = Object.assign({}, currStepNode, e.node);
            return n;
        });
        HAPPY_CURR_STEP_NODE = e.node;
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
        if (dropNode.id === '1') {
            return false;
        }
        return true;
    }

    function renderRightMenu(menuItems: MenuItem[]) {
        const list: any[] = menuItems.map(v => {
            if (!v.children || v.children.length < 1) {
                return {icon: v.icon, key: v.key, disabled: v.disabled, label: v.title};
            } else if (v.children && v.children.length > 0) {
                const r: any = {icon: v.icon, key: v.key, disabled: v.disabled, label: v.title};

                const subMenuList = v.children.map(subMenu => {
                    if (!subMenu.children || subMenu.children.length < 1) {
                        return {icon: subMenu.icon, key: subMenu.key, disabled: subMenu.disabled, label: subMenu.title};
                    } else if (subMenu.children && subMenu.children.length > 0) {
                        const r1: any = {icon: subMenu.icon, key: subMenu.key, disabled: subMenu.disabled, label: subMenu.title};
                        const subSubMenuList = subMenu.children.map(subSubMenu => {
                            return {icon: subSubMenu.icon, key: subSubMenu.key, disabled: subSubMenu.disabled, label: subSubMenu.title};
                        });
                        r1.children = subSubMenuList;
                        return r1;
                    }
                    return undefined;
                });
                r.children = subMenuList;
                return r;
            }
            return undefined;
        });
        return list;
    }

    function getNewTreeNode(define: any, type: string, isLeaf: boolean, title: string, parentLevel: number): StepNode {
        const node: StepNode = {
            type: type,
            isLeaf: isLeaf,
            title: title,
            level: parentLevel + 1,
            key: RandomUtils.getKey(),
            id: type + "_" + title,
            children: [], define: define,
            disabled: false,
            seq: seq,
        };
        seq = seq + 1;
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
                autoIncrementPrimaryKeyVariableName: '',
            };
        }  else if (type === 'shell script') {
            define = {
                name: '执行shell脚本命令', comments: '', enabled: true,
                content: '', scriptResultVariableName: ''
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

    function onAddScriptActionNode() {
        addNode('shell script', true);
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
        localStorage.setItem(LocalStorageUtils.COPY_STEP_NODE, JSON.stringify([copyNodeData]));
        onCopyVariables();
    }

    function onPasteStepVariables() {
        const item = localStorage.getItem(LocalStorageUtils.__COPY_STEP_VARIABLES);
        if(item == null || item === '') {
            return ;
        }
        try {
            let arr: AutoCaseVariable[] = JSON.parse(item);
            for (let i = 0; i < arr.length; i++) {
                let definedVariables = treeData[0].define.userDefinedVariables;
                let exists = false;
                for (let j = 0; j < definedVariables.length; j++) {
                    if(arr[i].name === definedVariables[j].name) {
                        exists = true;
                        break;
                    }
                }
                if(!exists) {
                    definedVariables.push(arr[i]);
                }
            }
            setTreeData([...treeData]);
        } catch (e) {
            console.log(e);
        }
    }

    function onCopyVariables() {
        const steps = localStorage.getItem(LocalStorageUtils.COPY_STEP_NODE);
        if(!steps) {
            return;
        }
        const array = steps.match(/\$\{(v_[0-9a-zA-Z_]+?)\}/g);
        if(array == null) {
            return ;
        }
        const variables: any[] = [];
        for (let i = 0; i < array.length; i++) {
            let definedVariables = treeData[0].define.userDefinedVariables;
            for (let j = 0; j < definedVariables.length; j++) {
                let element = array[i];
                element = element.substring(2, element.length - 1);
                if(element === definedVariables[j].name) {
                    variables.push(definedVariables[j]);
                }
            }
        }
        if(variables.length > 0) {
            localStorage.setItem(LocalStorageUtils.__COPY_STEP_VARIABLES, JSON.stringify(variables));
        }else{
            localStorage.setItem(LocalStorageUtils.__COPY_STEP_VARIABLES, "");
        }
    }

    function updateLevelAndKey(): (StepNode | null) [] {
        const item = localStorage.getItem(LocalStorageUtils.COPY_STEP_NODE);
        let nodes: (StepNode|null)[] = [];
        if (item) {
            try {
                nodes = JSON.parse(item);
            } catch (e) {
                nodes = [];
            }
        }
        if (nodes.length < 1 && copyNodeData !== null && copyNodeData !== undefined) {
            nodes = [copyNodeData];
        }
        if (nodes.length < 1) {
            return [];
        }

        for (let i = 0; i < nodes.length; i++) {
            const node: StepNode | null = nodes[i];
            if(node === null) {
                continue;
            }
            // 更新level和key
            if (currStepNode.isLeaf) {
                node.level = currStepNode.level;
            } else {
                node.level = currStepNode.level + 1;
            }
            node.key = RandomUtils.getKey();
            const children: { node: StepNode, parentNode: StepNode } [] = [];
            if (node.children && node.children.length > 0) {
                for (let i = 0; i < node.children.length; i++) {
                    children.push({node: node.children[i], parentNode: node});
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
        }

        return nodes;
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
            case MenuKey.AddScriptAction:
                onAddScriptActionNode();
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
        onSaveAutoCase();
    }

    function onSaveAutoCase(steps?: string|null, groups ?: string|null, loading ?: boolean) {
        setSaving(true);
        const data: any = {id: id, type: 1, steps: null,
            groupVariables: groupVariables || ''};
        if(steps !== undefined) {
            data.steps = steps;
        }else{
            data.steps = JSON.stringify(treeData);
        }
        if(groups !== undefined) {
            data.groupVariables = groups;
        }

        axios.post(ApiUrlConfig.SAVE_AUTO_CASE_URL, data).then(resp => {
            if (resp.status !== 200) {
                message.error('保存失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    if(loading) {
                        load(true);
                    }
                }
            }
        }).finally(() => {
            setSaving(false);
        });
    }

    function onChangeDefine(key: string, value: any) {
        if(HAPPY_CURR_STEP_NODE) {
            HAPPY_CURR_STEP_NODE.define[key] = value;
        }
    }

    function renderRightPanel() {
        switch (currStepNode.type) {
            case "root":
                return (<RootNodeEditor key={currStepNode.key} refreshTree={refreshTree} stepNode={currStepNode} groupVariables={groupVariables} onChangeGroupVariables={setGroupVariables}
                                        define={currStepNode.define} onChange={onChangeDefine} groupManageEditorWidth={groupManageEditorWidth} treeData={treeData}>
                </RootNodeEditor>);
            case "if":
                return (<IfControllerEditor key={currStepNode.key} refreshTree={refreshTree} stepNode={currStepNode}
                                            define={currStepNode.define} onChange={onChangeDefine}>
                </IfControllerEditor>);
            case "while":
                return (<WhileControllerEditor key={currStepNode.key} refreshTree={refreshTree} stepNode={currStepNode}
                                               define={currStepNode.define} onChange={onChangeDefine}>
                </WhileControllerEditor>);
            case 'loop':
                return (<LoopControllerEditor key={currStepNode.key} refreshTree={refreshTree}
                                              stepNode={currStepNode}
                                              define={currStepNode.define} onChange={onChangeDefine}>
                </LoopControllerEditor>);
            case 'http':
            case 'http request':
                return (
                    <HttpEditor key={currStepNode.key} userDefinedVariables={rootNode.define.userDefinedVariables}
                                refreshTree={refreshTree}
                                stepNode={currStepNode} define={currStepNode.define} onChange={onChangeDefine}>
                    </HttpEditor>);
            case 'jdbc':
            case 'jdbc request':
                return (<JDBCRequestEditor key={currStepNode.key} userDefinedVariables={rootNode.define.userDefinedVariables}
                                           refreshTree={refreshTree}
                                           stepNode={currStepNode} define={currStepNode.define} onChange={onChangeDefine}>
                </JDBCRequestEditor>);
            case 'shell script':
                return (<ScriptActionNodeEditor key={currStepNode.key} userDefinedVariables={rootNode.define.userDefinedVariables}
                                                refreshTree={refreshTree}
                                                stepNode={currStepNode} define={currStepNode.define} onChange={onChangeDefine}>
                </ScriptActionNodeEditor>);
            default:
                if (currStepNode.type.startsWith('调用平台API(')) {
                    return (<PlatformApiEditor key={currStepNode.key} userDefinedVariables={rootNode.define.userDefinedVariables}
                                               stepNode={currStepNode} define={currStepNode.define}
                                               refreshTree={refreshTree} onChange={onChangeDefine}>
                    </PlatformApiEditor>);
                }
                return (<CommonNameComments key={currStepNode.key} refreshTree={refreshTree} stepNode={currStepNode}
                                            define={currStepNode.define} onChange={onChangeDefine}>
                </CommonNameComments>)
        }
    }

    function draggable(node: any) {
        if (['1', '2', '3', '4'].indexOf(node.id) > -1) {
            return false;
        }
        return true;
    }

    function refreshStepSeq() {
        seq = 1;
        const stack: StepNode[] = [treeData[0]];
        while (true) {
            const stepNode: StepNode|undefined = stack.shift();
            if(stepNode) {
                stepNode.seq = seq;
                seq = seq + 1;
            }
            if(stepNode && stepNode.children) {
                for (let i = 0; i < stepNode.children.length; i++) {
                    stack.push(stepNode.children[i]);
                }
            }
            if(!stepNode) {
                break;
            }
        }
        setTreeData([...treeData]);
    }

    function onRun(runType) {
        if (!runEnvId) {
            message.info('请选择环境');
            return;
        }
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
            message.error(reason);
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
        window.open("#/planresult/" + id + "/2/0");
    }

    function showHelpPage() {
        window.open("https://github.com/flycoderzeng/tm/wiki");
    }

    function refreshKey(steps: StepNode[]): string[] {
        const newExpandedKeys: string[] = [];
        const stack: StepNode[] = [steps[0]];
        while (true) {
            const stepNode: StepNode|undefined = stack.shift();
            if(stepNode && stepNode.children) {
                for (let i = 0; i < stepNode.children.length; i++) {
                    stack.push(stepNode.children[i]);
                }
            }
            if(stepNode) {
                const key = RandomUtils.getKey();
                if(expandedKeys.indexOf(stepNode.key) >-1) {
                    newExpandedKeys.push(key);
                }
                stepNode.key = key;
            }
            if(!stepNode) {
                break;
            }
        }
        return newExpandedKeys;
    }

    function onPasteNode() {
        const nodes: (StepNode|null)[] = updateLevelAndKey();
        if(nodes.length < 1) {
            message.info('请先复制步骤');
            return;
        }
        if(currStepNode.isLeaf) {
            const parentNode: StepNode|null = findStepNode(treeData, currStepNode.key, true);
            if (parentNode === null) {
                return;
            }
            for (let i = 0; i < parentNode.children.length; i++) {
                if (parentNode.children[i].key === currStepNode.key) {
                    for (let j = 0; j < nodes.length; j++) {
                        const node2 = nodes[j];
                        if(node2 !== null) {
                            parentNode.children.splice(i + 1, 0, node2);
                        }
                    }
                }
            }
        }else{
            for (let j = 0; j < nodes.length; j++) {
                const node1 = nodes[j];
                if(node1 !== null) {
                    currStepNode.children.push(node1);
                }
            }
        }
        refreshStepSeq();
        for (let i = 0; i < nodes.length; i++) {
            const node = nodes[i];
            if (node !== null && !node.isLeaf && expandedKeys.indexOf(node.key) < 0) {
                expandedKeys.push(node.key);
            }
            setExpandedKeys([...expandedKeys]);
        }

        onPasteStepVariables();
    }

    function getCheckedStepNode(steps: StepNode[]): StepNode[] {
        const checkedStepNodes: StepNode[] = [];
        const stack: StepNode[] = [steps[0]];
        while (true) {
            const stepNode: StepNode|undefined = stack.shift();
            if(stepNode && stepNode.children) {
                for (let i = 0; i < stepNode.children.length; i++) {
                    stack.push(stepNode.children[i]);
                }
            }
            if(stepNode && checkedKeys.indexOf(stepNode.key) > -1) {
                checkedStepNodes.push(stepNode);
            }
            if(!stepNode) {
                break;
            }
        }
        return checkedStepNodes;
    }

    function batchCopySteps() {
        if(!treeCheckEnable) {
            setTreeCheckEnable(true);
            message.info('请选择步骤');
            return;
        }
        if(checkedKeys.length < 1) {
            setTreeCheckEnable(false);
            return;
        }
        // 复制步骤
        const steps:StepNode[] = [];
        //console.log(checkedKeys);
        const checkedStepNodes: StepNode[] = getCheckedStepNode(treeData);

        for (let i = 0; i < checkedStepNodes.length; i++) {
            if(isParentChecked(checkedStepNodes[i])) {
                checkedStepNodes[i] = null as any;
            }
        }
        for (let i = 0; i < checkedStepNodes.length; i++) {
            if(checkedStepNodes[i] === null) {
                continue;
            }
            if(checkedStepNodes[i].level === 1) {
                for (let j = 0; j < checkedStepNodes[i].children.length; j++) {
                    steps.push(checkedStepNodes[i].children[j]);
                }
            }else{
                steps.push(checkedStepNodes[i]);
            }
        }
        localStorage.setItem(LocalStorageUtils.COPY_STEP_NODE, JSON.stringify(steps));
        onCopyVariables();

        message.info('复制用例步骤成功');
        const newExpandedKeys: string[] = refreshKey(treeData);
        setExpandedKeys([...newExpandedKeys]);
        setTreeData([...treeData]);
        setCheckedKeys([]);
        setTreeCheckEnable(false);
    }

    function isParentChecked(stepNode: StepNode) {
        const node: StepNode|null = findStepNode(treeData, stepNode.key, true);
        if(node !== null && checkedKeys.indexOf(node.key) > -1) {
            return true;
        }
        return false;
    }

    function onCheck(checked, info) {
        setCheckedKeys(checked);
    }

    function copyCaseId() {
        if(copy(caseName + ' [' + id + ']')) {
            message.success('复制成功');
        }else{
            message.error('复制失败');
        }
    }

    return (
        <div className="case-editor-parent">
            <div className="case-editor-toolbar">
                <Button type="primary" loading={saving} onClick={onSave}>保存</Button>
                <Button type="primary" loading={running1} onClick={() => {
                    onRun(1);
                }}>运行</Button>
                <Button type="default" loading={running2} onClick={() => {
                    onRun(2);
                }}>运行组合</Button>
                {/*<Button type="primary" danger>停止</Button>*/}
                <RunEnvSelect onChange={setRunEnvId} style={{width: '180px', marginRight: '5px'}} value={runEnvId}></RunEnvSelect>
                <Button type="primary" onClick={() => {
                    onViewResult();
                }}>查看运行结果</Button>
                <Button type="default" onClick={() => {
                    setVisibleHistory(true);
                }}>恢复历史</Button>
                <Button type="primary" onClick={() => {
                    refreshStepSeq();
                }}>刷新步骤序号</Button>
                <Button type="primary" onClick={() => {
                    batchCopySteps();
                }}>批量复制步骤</Button>
                <Button type="default" onClick={() => {
                    copyCaseId();
                }}>复制用例ID</Button>
                <Button icon={<QuestionOutlined />} onClick={() => {
                    showHelpPage();
                }}>查看帮助文档</Button>
            </div>
            <div className="case-editor-main-content">
                <Resizable
                    defaultSize={{
                        width: 310,
                        height: '100%',
                    }}
                    minWidth={310}
                    maxWidth={550}
                    className="case-editor-step-tree"
                    enable={{top:false, right:true, bottom:false, left:false, topRight:false, bottomRight:false, bottomLeft:false, topLeft:false}}
                >
                    <Tree
                        checkable={treeCheckEnable}
                        checkedKeys={checkedKeys}
                        onCheck={onCheck}
                        className="draggable-tree"
                        onRightClick={onRightClick}
                        onExpand={onExpand}
                        defaultExpandedKeys={defaultExpandedKeys}
                        draggable={draggable}
                        titleRender={(nodeData) => {return (<span>{nodeData.seq + ': ' + nodeData.title}</span>)}}
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
                </Resizable>
                <div className="case-editor-right-panel">
                    {renderRightPanel()}
                </div>
            </div>
            <Menu onClick={onClickRightMenuItem} className="node-tree-context-menu" style={contextMenuPosition as any}
                  mode="vertical" items={rightMenuList}>
            </Menu>
            <Modal
                title="用例历史记录"
                open={visibleHistory}
                onOk={() => setVisibleHistory(false)}
                onCancel={() => setVisibleHistory(false)}
                width={900}
                footer={null}
            >
                <div>
                    <CaseHistoryList caseId={id} saveAutoCase={onSaveAutoCase}></CaseHistoryList>
                </div>
            </Modal>
        </div>
    )
}

export {AutoCaseEditor};
