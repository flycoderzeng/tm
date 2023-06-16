import React from "react";
import {CommonNameComments} from "./CommonNameComments";
import {RootNode} from "../entities/RootNode";
import {AutoCaseVariableEditor} from "../editor/AutoCaseVariableEditor";
import {EditorIState} from "../entities/EditorIState";
import {Tabs} from "antd";
import {KeyValueEditor} from "./KeyValueEditor";
import {GroupManageEditor} from "./GroupManageEditor";

const RootNodeEditor: React.FC<EditorIState<RootNode>> = (props) => {
    const {onChangeGroupVariables} = props;
    const {onChange} = props;
    if(!props.define.cookies) {
        props.define.cookies = [];
    }

    function onChangeTab() {

    }

    return (
        <div>
            <CommonNameComments refreshTree={props.refreshTree} stepNode={props.stepNode} define={props.define} onChange={onChange}></CommonNameComments>
            <Tabs defaultActiveKey="1" onChange={onChangeTab} items={[{label: '用例变量', key: '1',
                children: (<AutoCaseVariableEditor userDefinedVariables={props.define.userDefinedVariables} onChange={onChange}></AutoCaseVariableEditor>)},
                {label: 'cookies', key: '2', children: (<KeyValueEditor userDefinedVariables={props.define.userDefinedVariables} rows={props.define.cookies} type={'cookie'}></KeyValueEditor>)},
                {label: '组合管理', key: '3', children: (<GroupManageEditor groupVariables={props.groupVariables} userDefinedVariables={props.define.userDefinedVariables} onChangeGroupVariables={onChangeGroupVariables}></GroupManageEditor>)}]}>
            </Tabs>
        </div>
    )
}
export {RootNodeEditor};
