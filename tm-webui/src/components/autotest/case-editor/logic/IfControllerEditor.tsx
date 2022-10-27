import React, {useState} from "react";
import {CommonNameComments} from "../editor/CommonNameComments";
import {IfNode} from "../entities/IfNode";
import {ContentEditor} from "../editor/ContentEditor";
import {Col, Row} from "antd";
import {EditorIState} from "../entities/EditorIState";

const IfControllerEditor: React.FC<EditorIState<IfNode>> = (props) => {
    const [content, setContent] = useState(props.define.condition);
    function refreshContent(value) {
        props.define.condition = value;
        setContent(value);
    }

    return (<div>
        <CommonNameComments refreshTree={props.refreshTree} stepNode={props.stepNode} define={props.define}></CommonNameComments>
        <Row style={{paddingTop: '5px'}}>
            <Col flex="100px">表达式</Col>
            <Col flex="auto">
                <ContentEditor userDefinedVariables={props.userDefinedVariables} refreshContent={refreshContent} language={"sql"} content={content}></ContentEditor>
            </Col>
        </Row>
    </div>)
}
export {IfControllerEditor};
