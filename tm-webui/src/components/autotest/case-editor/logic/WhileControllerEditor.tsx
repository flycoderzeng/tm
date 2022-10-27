import React, {useState} from "react";
import {CommonNameComments} from "../editor/CommonNameComments";
import {WhileNode} from "../entities/WhileNode";
import {ContentEditor} from "../editor/ContentEditor";
import {Col, Row} from "antd";
import {EditorIState} from "../entities/EditorIState";

const WhileControllerEditor: React.FC<EditorIState<WhileNode>> = (props) => {
    const [content, setContent] = useState(props.define.condition);
    function refreshContent(value) {
        props.define.condition = value;
        setContent(value);
    }

    return (<div>
        <CommonNameComments refreshTree={props.refreshTree} stepNode={props.stepNode} define={props.define}></CommonNameComments>
        <Row style={{paddingTop: '5px'}}>
            <Col flex="100px">循环条件</Col>
            <Col flex="auto">
                <ContentEditor userDefinedVariables={props.userDefinedVariables} refreshContent={refreshContent} language={"sql"} content={content}></ContentEditor>
            </Col>
        </Row>
    </div>)
}
export {WhileControllerEditor};
