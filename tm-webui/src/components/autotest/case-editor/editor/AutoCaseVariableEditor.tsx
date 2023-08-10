import React, {useState} from "react";
import {Button, Input, Modal, Checkbox} from "antd";
import {Row, Col, message} from 'antd';
import {AutoCaseVariable} from "../entities/AutoCaseVariable";
import {RandomUtils} from "../../../../utils/RandomUtils";
import {LocalStorageUtils} from "../../../../utils/LocalStorageUtils";

const {TextArea} = Input;

interface IState {
    userDefinedVariables: AutoCaseVariable[];
    onChange: any;
}


const AutoCaseVariableEditor: React.FC<IState> = (props) => {
    const [userDefinedVariables, setUserDefinedVariables] = useState(props.userDefinedVariables);
    const [isVariableDetailModalVisible, setVariableDetailModalVisible] = useState(false);
    const [currIndex, setCurrIndex] = useState(-1);
    const [variableName, setVariableName] = useState("");
    const [variableValue, setVariableValue] = useState("");
    const {onChange} = props;
    const [checkedAll, setCheckedAll] = useState(false);

    if (JSON.stringify(userDefinedVariables) !== JSON.stringify(props.userDefinedVariables)) {
        for (let i = 0; i < props.userDefinedVariables.length; i++) {
            props.userDefinedVariables[i]['key'] = RandomUtils.getKey();
            props.userDefinedVariables[i]['checked'] = false;
        }
        setUserDefinedVariables([...props.userDefinedVariables]);
        setCheckedAll(false);
    }

    function onClickRow(index: number) {
        setCurrIndex(index);
    }

    function onChangeName(value: any, index: number) {
        let vName = value.target.value;
        if(!vName.startsWith('v_')) {
            return;
        }
        userDefinedVariables[index].name = vName;
    }

    function onChangeDescription(value: any, index: number) {
        userDefinedVariables[index].description = value.target.value;
    }

    function onChangeValue(value: any, index: number) {
        userDefinedVariables[index].value = value.target.value;
    }

    function onChangePlanVariableName(value: any, index: number) {
        userDefinedVariables[index].planVariableName = value.target.value;
    }

    function onChangeChecked(value: any, index: number) {
        userDefinedVariables[index].checked = value.target.checked;
        userDefinedVariables[index]['key'] = RandomUtils.getKey();
        setUserDefinedVariables([...userDefinedVariables]);
    }

    function renderRows() {
        return userDefinedVariables.map((value, index) => {
            const variableRowSelectedClass = index === currIndex ? "variable-row-selected" : "";
            return (<Row className={variableRowSelectedClass} style={{marginBottom: '5px'}} key={value['key']}
                         onClick={() => {
                             onClickRow(index)
                         }}>
                <Col span={6} style={{paddingRight: '5px', display: 'flex', alignItems: 'flex-end'}}>
                    <Checkbox style={{marginRight: '5px'}} defaultChecked={value.checked} checked={value.checked} onChange={(v) => {onChangeChecked(v, index);}}></Checkbox>
                    <Input placeholder="变量名称必须以 v_ 开始" defaultValue={value.name} onChange={(v) => {onChangeName(v, index);}}/>
                </Col>
                <Col span={6} style={{paddingRight: '5px'}}>
                    <Input defaultValue={value.description} onChange={(v) => {onChangeDescription(v, index);}}/>
                </Col>
                <Col span={6} style={{paddingRight: '5px'}}>
                    <Input defaultValue={value.value} onChange={(v) => {onChangeValue(v, index);}}/>
                </Col>
                <Col span={6}>
                    <Input defaultValue={value.planVariableName} onChange={(v) => {onChangePlanVariableName(v, index);}}/>
                </Col>
            </Row>)
        });
    }

    function addVariable() {
        const v = {name: '', type: 'string', value: '', planVariableName: '', description: ''};
        v['key'] = RandomUtils.getKey();
        userDefinedVariables.push(v);
        onChange('userDefinedVariables', userDefinedVariables);
        setUserDefinedVariables([...userDefinedVariables]);
    }

    function onRemove() {
        if(!window.confirm('确定删除该变量吗?')) {
            return;
        }
        userDefinedVariables.splice(currIndex, 1);
        onChange('userDefinedVariables', userDefinedVariables);
        setUserDefinedVariables([...userDefinedVariables]);
    }

    function handleOk() {
        userDefinedVariables[currIndex].name = variableName;
        userDefinedVariables[currIndex].value = variableValue;
        userDefinedVariables[currIndex]['key'] = RandomUtils.getKey();
        onChange('userDefinedVariables', userDefinedVariables);
        setUserDefinedVariables([...userDefinedVariables]);
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
        onChange('userDefinedVariables', userDefinedVariables);
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
        onChange('userDefinedVariables', userDefinedVariables);
        setUserDefinedVariables(userDefinedVariables);
    }

    function onTop() {
        if (!userDefinedVariables[currIndex] || currIndex <= 0) {
            message.info('请选择一个变量');
            return;
        }
        const value: any[] = userDefinedVariables.splice(currIndex, 1);
        if(value[0]) {
            userDefinedVariables.splice(0, 0, value[0]);
            onChange('userDefinedVariables', userDefinedVariables);
            setUserDefinedVariables([...userDefinedVariables]);
        }
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
        userDefinedVariables[tempIndex]['key'] = RandomUtils.getKey();
        props.userDefinedVariables[tempIndex] = userDefinedVariables[currIndex];
        props.userDefinedVariables[tempIndex]['key'] = userDefinedVariables[tempIndex]['key']
        userDefinedVariables[currIndex] = temp;
        userDefinedVariables[currIndex]['key'] = RandomUtils.getKey();
        props.userDefinedVariables[currIndex] = temp;
        props.userDefinedVariables[currIndex]['key'] = userDefinedVariables[currIndex]['key'];

        onChange('userDefinedVariables', userDefinedVariables);
        setUserDefinedVariables(userDefinedVariables);
        setCurrIndex(tempIndex);
    }

    function onChangeNameInModal(e) {
        setVariableName(e.target.value);
    }

    function onChangeValueInModal(e) {
        setVariableValue(e.target.value);
    }

    const onCheckAllVariables = (e: any) => {
        const checked = e.target.checked;
        setCheckedAll(checked);
        for (let i = 0; i < userDefinedVariables.length; i++) {
            userDefinedVariables[i].checked = checked;
            userDefinedVariables[i]['key'] = RandomUtils.getKey();
        }
        setUserDefinedVariables([...userDefinedVariables]);
    };

    function onCopy() {
        const values:any[] = [];
        for (let i = 0; i < userDefinedVariables.length; i++) {
            if(userDefinedVariables[i].checked) {
                values.push(userDefinedVariables[i]);
            }
        }
        if(values.length < 1) {
            message.info('请选择用例变量');
        } else {
            LocalStorageUtils.copyVariables(JSON.stringify(values));
            message.success('复制了' + values.length + '个用例变量');
        }
    }

    function onPaste() {
        const item = LocalStorageUtils.getItem(LocalStorageUtils.__COPY_VARIABLES);
        let values: any[];
        try {
            values = JSON.parse(item);
        } catch (e) {
            values = []
        }
        if(!values || values.length < 1) {
            return ;
        }
        if(!window.confirm('确定粘贴变量吗?')) {
            return ;
        }
        const length = userDefinedVariables.length;
        for (let i = 0; i < values.length; i++) {
            let exists: boolean = false;
            for (let j = 0; j < userDefinedVariables.length; j++) {
                if(values[i].name === userDefinedVariables[j].name) {
                    exists = true;
                    break;
                }
            }
            if(!exists) {
                values[i]['key'] = RandomUtils.getKey();
                userDefinedVariables.push(values[i]);
            }
        }
        if(userDefinedVariables.length > length) {
            onChange('userDefinedVariables', userDefinedVariables);
            setUserDefinedVariables([...userDefinedVariables]);
            message.success('粘贴用例变量成功');
        }
    }

    return (
        <div>
            <div>
                <div className="variable-toolbar">
                    <Button size="small" type="default" onClick={onClickDetail}>详细</Button>
                    <Button size="small" type="primary" onClick={addVariable}>添加变量</Button>
                    <Button size="small" type="primary" danger={true} onClick={onRemove}>删除</Button>
                    <Button size="small" type="primary" onClick={() => onMove(1)}>上移</Button>
                    <Button size="small" type="primary" onClick={() => onMove(2)}>下移</Button>
                    <Button size="small" type="primary" onClick={onUpSort}>升序</Button>
                    <Button size="small" type="primary" onClick={onDownSort}>降序</Button>
                    <Button size="small" type="primary" onClick={() => onTop()}>置顶</Button>
                    <Button size="small" type="primary" onClick={() => onCopy()}>复制变量</Button>
                    <Button size="small" type="primary" onClick={() => onPaste()}>粘贴变量</Button>
                </div>
                <Row style={{paddingTop: '5px'}}>
                    <Col span={6} style={{fontWeight: 600, color: '#6e6e6e'}}>
                        <Checkbox style={{marginRight: '5px'}} defaultChecked={checkedAll} checked={checkedAll} onChange={onCheckAllVariables}></Checkbox>
                        变量名称</Col>
                    <Col span={6} style={{fontWeight: 600, color: '#6e6e6e'}}>变量描述</Col>
                    <Col span={6} style={{fontWeight: 600, color: '#6e6e6e'}}>
                        变量值
                    </Col>
                    <Col span={6} style={{fontWeight: 600, color: '#6e6e6e'}}>
                        计划变量
                    </Col>
                </Row>
                <div style={{maxHeight: 'calc(100vh - 350px)', overflow: 'auto'}}>
                {renderRows()}
                </div>
            </div>
            <Modal width={700} title="变量详细" open={isVariableDetailModalVisible} onOk={handleOk}
                   onCancel={handleCancel}>
                <Row style={{paddingBottom: '5px'}}>
                    <Col flex="100px" style={{fontWeight: 600, color: '#6e6e6e'}}>变量名称</Col>
                    <Col flex="auto">
                        <Input placeholder="变量名必须以v_开头" value={variableName} onChange={onChangeNameInModal}/>
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
