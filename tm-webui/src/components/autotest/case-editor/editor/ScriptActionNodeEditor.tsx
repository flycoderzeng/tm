import React, {useState} from "react";
import {EditorIState} from "../entities/EditorIState";
import {ScriptActionNode} from "../entities/ScriptActionNode";
import {CommonNameComments} from "./CommonNameComments";
import {AutoComplete, Col, Input, Row} from "antd";
import {ContentEditor} from "./ContentEditor";

const ScriptActionNodeEditor: React.FC<EditorIState<ScriptActionNode>> = (props) => {
    const [content, setContent] = useState(props.define.content);
    const [scriptResultVariableName, setScriptResultVariableName] = useState(props.define.scriptResultVariableName);

    if(content !== props.define.content) {
        setContent(props.define.content);
    }

    if(scriptResultVariableName !== props.define.scriptResultVariableName) {
        setScriptResultVariableName(props.define.scriptResultVariableName);
    }
    function refreshContent(value) {
        props.define.content = value;
        setContent(value);
    }

    function onChangeScriptResultVariableName(v: any) {
        setScriptResultVariableName(v);
        props.define.scriptResultVariableName = v;
    }

    const options = props.userDefinedVariables?.map(v => {
        return {label: v.name, value: '${' + v.name + '}'};
    }) as any[];

    return (<div>
        <CommonNameComments refreshTree={props.refreshTree} stepNode={props.stepNode} define={props.define}>
        </CommonNameComments>
        <div>
            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="120px">shell脚本内容</Col>
                <Col flex="auto">
                    <ContentEditor userDefinedVariables={props.userDefinedVariables}
                                   refreshContent={refreshContent}
                                   language={'shell'}
                                   content={content}>
                    </ContentEditor>
                </Col>
            </Row>
            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="120px">脚本结果输出到变量</Col>
                <Col flex="auto">
                    <AutoComplete
                        placeholder="变量名"
                        value={scriptResultVariableName} onChange={onChangeScriptResultVariableName}
                        style={{width: '100%'}}
                        options={options}
                    />
                </Col>
            </Row>
        </div>
    </div>)
}

export {ScriptActionNodeEditor}