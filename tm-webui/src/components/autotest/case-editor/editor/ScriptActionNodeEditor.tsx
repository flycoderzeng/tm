import React, {useEffect, useState} from "react";
import {EditorIState} from "../entities/EditorIState";
import {ScriptActionNode} from "../entities/ScriptActionNode";
import {CommonNameComments} from "./CommonNameComments";
import {AutoComplete, Col, Row} from "antd";
import {ContentEditor} from "./ContentEditor";
import {DataTypeEnum} from "../../../../entities/DataTypeEnum";
import {CommonRemoteSearchSingleSelect} from "../../../common/components/CommonRemoteSearchSingleSelect";

const ScriptActionNodeEditor: React.FC<EditorIState<ScriptActionNode>> = (props) => {
    const [content, setContent] = useState(props.define.content);
    const [interpreterPath, setInterpreterPath] = useState(props.define.interpreterPath);
    const [scriptId, setScriptId] = useState(props.define.scriptId);
    const [scriptResultVariableName, setScriptResultVariableName] = useState(props.define.scriptResultVariableName);
    const {onChange} = props;
    const {stepNode} = props;

    useEffect(() => {
        setContent(stepNode.define.content);
        setScriptResultVariableName(stepNode.define.scriptResultVariableName);
    }, [props.key]);

    function refreshContent(value) {
        setContent(value);
        onChange('content', value);
    }

    function onChangeScriptResultVariableName(v: any) {
        setScriptResultVariableName(v);
        onChange('scriptResultVariableName', v);
    }

    function onChangeInterpreterPath(v: any) {
        setInterpreterPath(v);
        onChange('interpreterPath', v);
    }

    function onChangeScriptId(v: any) {
        setScriptId(v);
        onChange('scriptId', v);
    }

    const options = props.userDefinedVariables?.map(v => {
        return {label: v.name, value: '${' + v.name + '}'};
    }) as any[];

    return (<div>
        <CommonNameComments refreshTree={props.refreshTree} stepNode={stepNode} define={stepNode.define} onChange={onChange}>
        </CommonNameComments>
        <div>
            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="150px">执行器路径</Col>
                <Col flex="auto">
                    <AutoComplete
                        placeholder=""
                        value={interpreterPath} onChange={onChangeInterpreterPath}
                        style={{width: '100%'}}
                    />
                </Col>
            </Row>
            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="150px">执行脚本</Col>
                <Col flex="auto">
                    <CommonRemoteSearchSingleSelect
                        style={{width: 300}}
                        onChange={onChangeScriptId}
                        type={'resource'}
                        value={scriptId}
                        dataTypeId={DataTypeEnum.AUTO_SHELL}></CommonRemoteSearchSingleSelect>
                </Col>
            </Row>
            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="150px">脚本内容或参数</Col>
                <Col flex="auto">
                    <ContentEditor userDefinedVariables={props.userDefinedVariables}
                                   refreshContent={refreshContent}
                                   language={'shell'}
                                   content={content}>
                    </ContentEditor>
                </Col>
            </Row>
            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="150px">脚本结果输出到变量</Col>
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
