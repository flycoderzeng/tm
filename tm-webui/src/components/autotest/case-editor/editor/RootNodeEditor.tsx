import React from "react";
import {CommonNameComments} from "./CommonNameComments";
import {RootNode} from "../entities/RootNode";
import {AutoCaseVariableEditor} from "../editor/AutoCaseVariableEditor";
import {EditorIState} from "../entities/EditorIState";
import {Tabs} from "antd";
import {KeyValueEditor} from "./KeyValueEditor";
import {GroupManageEditor} from "./GroupManageEditor";
const { TabPane } = Tabs;

const RootNodeEditor: React.FC<EditorIState<RootNode>> = (props) => {
    if(!props.define.cookies) {
        props.define.cookies = [];
    }

    function onChangeTab() {

    }

    return (
        <div>
            <CommonNameComments refreshTree={props.refreshTree} stepNode={props.stepNode} define={props.define}></CommonNameComments>
            <Tabs defaultActiveKey="1" onChange={onChangeTab} items={[{label: '用例变量', key: '1',
                children: (<AutoCaseVariableEditor userDefinedVariables={props.define.userDefinedVariables}></AutoCaseVariableEditor>)},
                {label: 'cookies', key: '2', children: (<KeyValueEditor userDefinedVariables={props.define.userDefinedVariables} rows={props.define.cookies} type={'cookie'}></KeyValueEditor>)},
                {label: '组合管理', key: '3', children: (<GroupManageEditor groupVariables={props.groupVariables}></GroupManageEditor>)}]}>
            </Tabs>
        </div>
    )
}
export {RootNodeEditor};
