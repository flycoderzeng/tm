import React, {useState} from "react";
import {Button, Input, Modal} from "antd";
import {Row, Col, message} from 'antd';
import {AutoCaseVariable} from "../entities/AutoCaseVariable";
import {RandomUtils} from "../../../../utils/RandomUtils";

const {TextArea} = Input;

interface IState {
    userDefinedVariables: AutoCaseVariable[]
}


const AutoCaseVariableEditor: React.FC<IState> = (props) => {
    const [userDefinedVariables, setUserDefinedVariables] = useState(props.userDefinedVariables);
    const [isVariableDetailModalVisible, setVariableDetailModalVisible] = useState(false);
    const [currIndex, setCurrIndex] = useState(-1);
    const [variableName, setVariableName] = useState("");
    const [variableValue, setVariableValue] = useState("");

    if (JSON.stringify(userDefinedVariables) !== JSON.stringify(props.userDefinedVariables)) {
        for (let i = 0; i < userDefinedVariables.length; i++) {
            userDefinedVariables[i]['key'] = RandomUtils.getKey();
        }
        setUserDefinedVariables(props.userDefinedVariables);
    }

    function onClickRow(index: number) {
        setCurrIndex(index);
    }

    function onChangeName(value: any, index: number) {
        userDefinedVariables[index].name = value.target.value;
    }

    function onChangeValue(value: any, index: number) {
        userDefinedVariables[index].value = value.target.value;
    }

    function renderRows() {
        return userDefinedVariables.map((value, index) => {
            const variableRowSelectedClass = index === currIndex ? "variable-row-selected" : "";
            return (<Row className={variableRowSelectedClass} style={{marginBottom: '5px'}} key={value['key']}
                         onClick={() => {
                             onClickRow(index)
                         }}>
                <Col span={12} style={{paddingRight: '5px'}}>
                    <Input defaultValue={value.name} onChange={(v) => {onChangeName(v, index);}}/>
                </Col>
                <Col span={12}>
                    <Input defaultValue={value.value} onChange={(v) => {onChangeValue(v, index);}}/>
                </Col>
            </Row>)
        });
    }

    function addVariable() {
        const v = {name: '', type: 'string', value: ''};
        v['key'] = RandomUtils.getKey();
        userDefinedVariables.push(v);
        setUserDefinedVariables(userDefinedVariables);
    }

    function onRemove() {
        if(!window.confirm('确定删除该变量吗?')) {
            return;
        }
        userDefinedVariables.splice(currIndex, 1);
        setUserDefinedVariables(userDefinedVariables);
    }

    function handleOk() {
        userDefinedVariables[currIndex].name = variableName;
        userDefinedVariables[currIndex].value = variableValue;
        userDefinedVariables[currIndex]['key'] = RandomUtils.getKey();
        setUserDefinedVariables(userDefinedVariables);
        setVariableDetailModalVisible(false);
    }

    function handleCancel() {
        setVariableDetailModalVisible(false);
    }

    function onClickDetail() {
        if (!userDefinedVariables[currIndex]) {
            message.info('请选择一个变量');
            return;
        }
        setVariableName(userDefinedVariables[currIndex].name);
        setVariableValue(userDefinedVariables[currIndex].value);
        setVariableDetailModalVisible(true);
    }

    function onUpSort() {
        userDefinedVariables.sort((a, b) => {
            if (a.name <= b.name) {
                return -1;
            } else if (a.name === b.name) {
                return 0;
            } else {
                return 1;
            }
        });
        setUserDefinedVariables(userDefinedVariables);
    }

    function onDownSort() {
        userDefinedVariables.sort((a, b) => {
            if (a.name <= b.name) {
                return 1;
            } else if (a.name === b.name) {
                return 0;
            } else {
                return -1;
            }
        });
        setUserDefinedVariables(userDefinedVariables);
    }

    function onMove(type: number) {
        if (!userDefinedVariables[currIndex]) {
            message.info('请选择一个变量');
            return;
        }
        if ((type === 1 || type === 3) && currIndex <= 0) {
            return;
        }
        if (type === 2 && currIndex >= userDefinedVariables.length - 1) {
            return;
        }
        let tempIndex;
        if (type === 1) {
            tempIndex = currIndex - 1;
        } else if (type === 2) {
            tempIndex = currIndex + 1;
        } else if (type === 3) {
            tempIndex = 0;
        }
        const temp = userDefinedVariables[tempIndex];
        userDefinedVariables[tempIndex] = userDefinedVariables[currIndex];
        userDefinedVariables[currIndex] = temp;
        setUserDefinedVariables(userDefinedVariables);
        setCurrIndex(tempIndex);
    }

    function onChangeNameInModal(e) {
        setVariableName(e.target.value);
    }

    function onChangeValueInModal(e) {
        setVariableValue(e.target.value);
    }

    return (
        <div>
            <div>
                <div className="variable-toolbar">
                    <Button size="small" type="primary" onClick={onClickDetail}>详细</Button>
                    <Button size="small" type="primary" onClick={addVariable}>添加变量</Button>
                    <Button size="small" type="primary" onClick={onRemove}>删除</Button>
                    <Button size="small" type="primary" onClick={() => onMove(1)}>上移</Button>
                    <Button size="small" type="primary" onClick={() => onMove(2)}>下移</Button>
                    <Button size="small" type="primary" onClick={onUpSort}>升序</Button>
                    <Button size="small" type="primary" onClick={onDownSort}>降序</Button>
                    <Button size="small" type="primary" onClick={() => onMove(3)}>置顶</Button>
                </div>
                <Row style={{paddingTop: '5px'}}>
                    <Col span={12} style={{fontWeight: 600, color: '#6e6e6e'}}>变量名称</Col>
                    <Col span={12} style={{fontWeight: 600, color: '#6e6e6e'}}>
                        变量值
                    </Col>
                </Row>
                {renderRows()}
            </div>
            <Modal width={700} title="变量详细" visible={isVariableDetailModalVisible} onOk={handleOk}
                   onCancel={handleCancel}>
                <Row style={{paddingBottom: '5px'}}>
                    <Col flex="100px" style={{fontWeight: 600, color: '#6e6e6e'}}>变量名称</Col>
                    <Col flex="auto">
                        <Input value={variableName} onChange={onChangeNameInModal}/>
                    </Col>
                </Row>
                <Row style={{paddingBottom: '5px'}}>
                    <Col flex="100px" style={{fontWeight: 600, color: '#6e6e6e'}}>变量值</Col>
                    <Col flex="auto">
                        <TextArea rows={8} value={variableValue} onChange={onChangeValueInModal}/>
                    </Col>
                </Row>
            </Modal>
        </div>
    )
}
export {AutoCaseVariableEditor};
