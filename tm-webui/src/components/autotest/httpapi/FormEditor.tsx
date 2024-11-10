import React, {useState} from "react";
import {Button, Col, Input, Row, Select} from "antd";
import {CloseOutlined,} from '@ant-design/icons';
import {RandomUtils} from "../../../utils/RandomUtils";

// type 1-form 2-kv 3-query 4-headers
interface IState {
    defineRows: any[],
    type: string,
}

const Option = Select.Option;

const FormEditor: React.FC<IState> = (props) => {
    let addBtnTitle = '添加form参数';
    const [type, setType] = useState(props.type);
    const [defineRows, setDefineRows] = useState(props.defineRows);

    if(type !== props.type) {
        setType(props.type);
    }

    if(defineRows.length < 1) {
        defineRows.push({key: RandomUtils.getKey(), name: '', type: 'text', required: '1', example: '', remark: '', value: ''});
        setDefineRows(defineRows);
    }
    if(type === '1') {
        addBtnTitle = '添加form参数';
    } else if(type === '2') {
        addBtnTitle = '添加key-value参数';
    } else if(type === '3') {
        addBtnTitle = '添加query参数';
    } else if(type === '4') {
        addBtnTitle = '添加header';
    }

    function onChangeName(value: string, row: any) {
        row.name = value;
    }

    function onChangeType(value: string, row: any) {
        row.type = value;
    }

    function onChangeExample(value: string, row: any) {
        row.example = value;
    }

    function onChangeRemark(value: string, row: any) {
        row.remark = value;
    }

    function onChangeRequired(value: string, row: any) {
        row.required = value;
    }

    function onChangeValue(value: string, row: any) {
        row.value = value;
    }

    function onAddRow() {
        defineRows.push({key: RandomUtils.getKey(), name: '', type: 'text', required: '1', example: '', remark: '', value: ''});
        setDefineRows([...defineRows]);
    }

    function onRemoveRow(row: any) {
        let ind = -1;
        for (let i = 0; i < defineRows.length; i++) {
            if(defineRows[i].key === row.key) {
                ind = i;
                break;
            }
        }
        if(ind > -1) {
            defineRows.splice(ind, 1);
        }
        setDefineRows([...defineRows]);
    }

    function renderDefineRows() {

        return defineRows.map(row => {
            let nameSpan = 4;
            let valueSpan = 4;
            let typeSpan = 3;
            let requireSpan = 3;
            let exampleSpan = 5;
            let remarkSpan = 6;
            let valueCol;
            let typeCol;
            let requiredCol;

            if(type === '4') {
                remarkSpan = 7;
                nameSpan = 5;
                valueSpan = 6;

                valueCol = <Col span={valueSpan} className="key-value-row-col">
                    <Input placeholder="Value" defaultValue={row.value} onChange={(value) => {onChangeValue(value.target.value, row);}}/>
                </Col>
            }
            if(type === '1') {
                typeCol = <Col span={typeSpan} className="key-value-row-col">
                    <Select onChange={(value) => {
                        onChangeType(value, row);
                    }} defaultValue={row.type} className="select-max-width">
                        <Option key="text" value="text">text</Option>
                        <Option key="file" value="file">file</Option>
                    </Select>
                </Col>
            }
            if(type === '1' || type === '2' || type === '3') {
                requiredCol = <Col span={requireSpan} className="key-value-row-col">
                    <Select onChange={(value) => {
                        onChangeRequired(value, row);
                    }} defaultValue={row.required} className="select-max-width">
                        <Option key="1" value="1">必须</Option>
                        <Option key="2" value="2">非必须</Option>
                    </Select>
                </Col>
                nameSpan = 7;
                remarkSpan = 7;
                exampleSpan = 6;
            }
            if(type === '1') {
                nameSpan = 6;
                remarkSpan = 6;
                exampleSpan = 5;
            }
            return (<div key={row.key} style={{paddingTop: '5px'}}>
                <Row>
                    <Col span={nameSpan} className="key-value-row-col">
                        <Input placeholder="Key" defaultValue={row.name} onChange={(value) => {onChangeName(value.target.value, row);}}/>
                    </Col>
                    {valueCol}
                    {typeCol}
                    {requiredCol}
                    <Col span={exampleSpan} className="key-value-row-col">
                        <Input placeholder="参数示例" defaultValue={row.example} onChange={(value) => {onChangeExample(value.target.value, row);}}/>
                    </Col>
                    <Col span={remarkSpan} className="key-value-row-col">
                        <Input placeholder="备注" defaultValue={row.remark} onChange={(value) => {onChangeRemark(value.target.value, row);}}/>
                    </Col>
                    <Col span={1} className="key-value-row-col">
                        <span onClick={() => onRemoveRow(row)} className="icon-close span-cursor-pointer" style={{marginLeft: '8px'}}><CloseOutlined /></span>
                    </Col>
                </Row></div>)
        });
    }


    return (
        <div>
            <Button type="primary" size="small" onClick={onAddRow}>{addBtnTitle}</Button>
            <div style={{paddingTop: '5px'}}>
                {renderDefineRows()}
            </div>
        </div>
    )
}

export {FormEditor};
