import React, {useState} from "react";
import {Input} from "antd";
import { Row, Col } from 'antd';
import {BaseNode} from "../entities/BaseNode";
import {EditorIState} from "../entities/EditorIState";
import {ActionType} from "../entities/ActionType";


const CommonNameComments: React.FC<EditorIState<BaseNode>> = (props) => {
    const [type, setType] = useState(props.stepNode.type);
    const [name, setName] = useState(props.define.name);
    const [comments, setComments] = useState(props.define.comments);
    if(type !== props.stepNode.type) {
        setType(props.stepNode.type);
    }
    if(name !== props.define.name) {
        setName(props.define.name);
    }
    if(comments !== props.define.comments) {
        setComments(props.define.comments);
    }

    function onChangeName(el: any) {
        setName(el.target.value);
        props.define.name = el.target.value;
        props.refreshTree(ActionType.SetName, props.stepNode.key, el.target.value);
    }

    function onChangeComments(el: any) {
        setComments(el.target.value);
        props.define.comments = el.target.value;
    }
    return (
        <div>
            <h3>{type}</h3>
            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="120px">名称</Col>
                <Col flex="auto">
                    <Input placeholder="步骤名称" value={name} onChange={onChangeName}/>
                </Col>
            </Row>
            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="120px">描述</Col>
                <Col flex="auto">
                    <Input placeholder="步骤描述" value={comments} onChange={onChangeComments}/>
                </Col>
            </Row>
        </div>
    )
}
export {CommonNameComments};
