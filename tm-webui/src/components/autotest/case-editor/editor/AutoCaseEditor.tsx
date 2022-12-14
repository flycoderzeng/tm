import React, {useState, useEffect} from 'react';
import {Button, Menu, message, Modal, Tree} from "antd";
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
import {PlanResultList} from "../../planresult/PlanResultList";
import {CaseHistoryList} from "../CaseHistoryList";

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

const whenClickLeafStepNodeDisabledMenuItems: string[] = [MenuKey.AddResource, MenuKey.AddPlatformApi,
    MenuKey.AddRecent, MenuKey.AddMostCommonlyUsed, MenuKey.AddLogic];

const initTreeData: StepNode[] = [{
    "type": "root",
    "level": 1,
    "define": {
        "userDefinedVariables": [],
        "cookies": [],
        "groups": [],
        "name": "???????????????",
        "comments": "",
        "enabled": true,
    },
    "title": "???????????????",
    "key": "1",
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
            disabled: false,
            seq: 4
        }
    ]
}];

let seq = 4;

const AutoCaseEditor: React.FC<IState> = (props) => {
    //let history = useHistory();
    const [saving, setSaving] = useState(false);
    const [visibleHistory, setVisibleHistory] = useState(false);
    const [running1, setRunning1] = useState(false);
    const [running2, setRunning2] = useState(false);
    const [loadedPlatformApi, setLoadedPlatformApi] = useState(false);
    const [id, setId] = useState(props.id);
    const [expandedKeys, setExpandedKeys] = useState(['1', '2', '3', '4']);
    const [selectedKeys, setSelectedKeys] = useState<string[]>([]);
    const [contextMenuPosition, setContextMenuPosition] = useState(rightMenuInitStyle);
    const [treeData, setTreeData] = useState<StepNode[]>(initTreeData);
    const [currStepNode, setCurrStepNode] = useState<StepNode>(treeData[0]);
    const [rootNode, setRootNode] = useState<StepNode>(treeData[0]);
    const [runEnvId, setRunEnvId] = useState('');
    const [groupVariables, setGroupVariables] = useState(null);
    const [copyNodeData, setCopyNodeData] = useState<StepNode|null>(null);
    const [rightMenuKeys, setRightMenuKeys] = useState<MenuItem[]>([]);
    const [rightMenuList, setRightMenuList] = useState<any[]>([]);

    if (id !== props.id) {
        setId(props.id);
    }

    function loadPlatformApiTree() {
        if (!loadedPlatformApi) {
            axios.get(ApiUrlConfig.GET_PLATFORM_API_TREE_URL).then(resp => {
                if (resp.status !== 200) {
                    message.error('????????????api??????');
                } else {
                    const ret = resp.data;
                    if (ret.code !== 0) {
                        message.error(ret.message);
                    } else if (ret.data) {
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
                        const rightMenuInitKeys: MenuItem[] = [
                            {key: MenuKey.AddResource, title: '????????????', icon: <PlusOutlined/>, disabled: false, children: []},
                            {key: MenuKey.AddRecent, title: '????????????', icon: <PlusOutlined/>, disabled: false, children: []},
                            {
                                key: MenuKey.AddMostCommonlyUsed, title: '????????????', icon: <PlusOutlined/>, disabled: false, children: [
                                    {key: MenuKey.AddHttpRequest, title: 'HTTP??????', disabled: false},
                                    {key: MenuKey.AddJDBCRequest, title: 'JDBC??????', disabled: false},
                                ]
                            },
                            {key: MenuKey.AddPlatformApi, title: '??????API', icon: <PlusOutlined/>, disabled: false, children: platformApiTree},
                            {key: MenuKey.Remove, title: '??????', icon: <DeleteOutlined/>, disabled: false},
                            {key: MenuKey.Copy, title: '??????', icon: <CopyOutlined/>, disabled: false},
                            {key: MenuKey.Paste, title: '??????', icon: <SettingOutlined/>, disabled: false},
                            {key: MenuKey.Enable, title: '??????', icon: <PlayCircleOutlined/>, disabled: false},
                            {key: MenuKey.Disable, title: '??????', icon: <PauseOutlined/>, disabled: false},
                            {
                                key: MenuKey.AddLogic, title: '????????????', icon: <PlusOutlined/>, disabled: false, children: [
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
    }, [id]);// eslint-disable-line react-hooks/exhaustive-deps

    loadPlatformApiTree();

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
                message.error('??????????????????');
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
                    setTreeData(steps);
                    setCurrStepNode(steps[0]);
                    setRootNode(steps[0]);
                    setRunEnvId(ret.data.lastRunEnvId + '');
                    setGroupVariables(ret.data.groupVariables);
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
        // expandedKeys ?????????????????????
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
                // where to insert ?????????????????????????????????????????????
                item.children.unshift(dragObj);
            });
        } else if (
            (info.node.props.children || []).length > 0 && // Has children
            info.node.props.expanded && // Is expanded
            dropPosition === 1 // On the bottom gap
        ) {
            loop(data, dropKey, item => {
                item.children = item.children || [];
                // where to insert ?????????????????????????????????????????????
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
            isLeaf: isLeaf, title: title,
            level: parentLevel + 1,
            key: RandomUtils.getKey(),
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
                name: '??????http??????', comments: '', enabled: true, requestType: 'POST',
                url: '', params: [], headers: [], bodyType: 'raw', rawType: 'json', formData: [],
                formUrlencoded: [], content: '', checkErrorList: [], responseExtractorList: [],
            };
        }  else if (type === 'jdbc request') {
            define = {
                name: '?????????????????????', comments: '', enabled: true, dbName: '',
                resultSetVariableName: '',
                content: '', checkErrorList: [], responseExtractorList: [],
            };
        } else if (type.startsWith('????????????API(')) {
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
        const define = getDefine('????????????API(' + name + ")");
        define.platformApiId = parseInt(id);
        define.parametricList = [];
        addTreeNode(define, '????????????API(' + name + ")", true, name);
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
        // ??????level???key
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
            message.info('??????????????????');
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
        if(!window.confirm('????????????????')) {
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
        onSaveAutoCase();
    }

    function onSaveAutoCase(steps?: string|null, groups ?: string|null, loading ?: boolean) {
        setSaving(true);
        const data: any = {id: id, type: 1, steps: null,
            groupVariables: groupVariables};
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
                message.error('????????????');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('????????????');
                    if(loading) {
                        load(true);
                    }
                }
            }
        }).finally(() => {
            setSaving(false);
        });
    }

    function renderRightPanel() {
        switch (currStepNode.type) {
            case "root":
                return (<RootNodeEditor refreshTree={refreshTree} stepNode={currStepNode} groupVariables={groupVariables} onChangeGroupVariables={setGroupVariables}
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
                if (currStepNode.type.startsWith('????????????API(')) {
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
        if (!window.confirm('??????????????????')) {
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
                message.error('????????????');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('????????????');
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
        window.open("/planresult/" + id + "/2");
    }

    return (
        <div className="case-editor-parent">
            <div className="case-editor-toolbar">
                <Button size="small" type="primary" loading={saving} onClick={onSave}>??????</Button>
                <Button size="small" type="primary" loading={running1} onClick={() => {
                    onRun(1);
                }}>??????</Button>
                <Button size="small" type="primary" loading={running2} onClick={() => {
                    onRun(2);
                }}>????????????</Button>
                <Button size="small" type="primary" danger>??????</Button>
                <RunEnvSelect size={'small'} onChange={setRunEnvId} style={{width: '150px', marginRight: '5px'}} value={runEnvId}></RunEnvSelect>
                <Button size="small" type="primary" onClick={() => {
                    onViewResult();
                }}>??????????????????</Button>
                <Button size="small" type="default">???????????????????????????</Button>
                <Button size="small" type="default"onClick={() => {
                    setVisibleHistory(true);
                }}>????????????</Button>
            </div>
            <div className="case-editor-main-content">
                <div className="case-editor-step-tree">
                    <Tree
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
                </div>
                <div className="case-editor-right-panel">
                    {renderRightPanel()}
                </div>
            </div>
            <Menu onClick={onClickRightMenuItem} className="node-tree-context-menu" style={contextMenuPosition as any}
                  mode="vertical" items={rightMenuList}>
            </Menu>
            <Modal
                title="??????????????????"
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
