import React, {useEffect, useState} from "react";
import {Col, Input, Row} from "antd";
import {BaseNode} from "../entities/BaseNode";
import {EditorIState} from "../entities/EditorIState";
import {ActionType} from "../entities/ActionType";


const CommonNameComments: React.FC<EditorIState<BaseNode>> = (props) => {
    const [type, setType] = useState(props.stepNode.type);
    const [name, setName] = useState(props.define.name);
    const [comments, setComments] = useState(props.define.comments);
    const {onChange} = props;
    const {refreshTree} = props;
    const {stepNode} = props;

    useEffect(() => {
        setType(props.stepNode.type);
        setName(props.define.name);
        setComments(props.define.comments);
    }, [props.key]);

    function onChangeName(el: any) {
        setName(el.target.value);
        onChange('name', el.target.value);
        refreshTree(ActionType.SetName, stepNode.key, el.target.value);
    }

    function onChangeComments(el: any) {
        setComments(el.target.value);
        onChange('comments', el.target.value);
    }
    return (
        <div>
            <h3>{type}</h3>
            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="150px">名称</Col>
                <Col flex="auto">
                    <Input placeholder="步骤名称" value={name} onChange={onChangeName}/>
                </Col>
            </Row>
            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="150px">描述</Col>
                <Col flex="auto">
                    <Input placeholder="步骤描述" value={comments} onChange={onChangeComments}/>
                </Col>
            </Row>
        </div>
    )
}
export {CommonNameComments};
