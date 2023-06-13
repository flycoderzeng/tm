import React, {useEffect, useState} from "react";
import {CommonNameComments} from "../editor/CommonNameComments";
import {WhileNode} from "../entities/WhileNode";
import {ContentEditor} from "../editor/ContentEditor";
import {Col, Row} from "antd";
import {EditorIState} from "../entities/EditorIState";

const WhileControllerEditor: React.FC<EditorIState<WhileNode>> = (props) => {
    const [content, setContent] = useState(props.define.condition);
    const {onChange} = props;
    const {stepNode} = props;

    useEffect(() => {
        setContent(stepNode.define.condition);
    }, [stepNode.define.condition]);

    function refreshContent(value) {
        setContent(value);
        onChange('condition', value);
    }

    return (<div>
        <CommonNameComments refreshTree={props.refreshTree} stepNode={stepNode} define={stepNode.define} onChange={onChange}></CommonNameComments>
        <Row style={{paddingTop: '5px'}}>
            <Col flex="100px">循环条件</Col>
            <Col flex="auto">
                <ContentEditor userDefinedVariables={props.userDefinedVariables} refreshContent={refreshContent} language={"json"} content={content}></ContentEditor>
            </Col>
        </Row>
    </div>)
}
export {WhileControllerEditor};
