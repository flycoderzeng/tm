import React, {useState} from "react";
import {KeyValueRow} from "../entities/KeyValueRow";
import {AutoComplete, Button, Col, Input, Modal, Row, Select} from "antd";
import {ContentEditor} from "./ContentEditor";
import {AutoCaseVariable} from "../entities/AutoCaseVariable";
import {RandomUtils} from "../../../../utils/RandomUtils";
const { Option } = Select;

export interface IState {
    rows: KeyValueRow[];
    // form-data cookie response-assert response-extractor
    // jdbc-response-assert jdbc-response-extractor
    type?: string;
    userDefinedVariables?: AutoCaseVariable[];
}

const relationOperatorList: any[] = [
    {label: '等于', value: '1'}, {label: '不等于', value: '2'},
    {label: '小于', value: '3'}, {label: '小于等于', value: '4'},
    {label: '大于', value: '5'}, {label: '大于等于', value: '6'},
    {label: '包含', value: '7'}, {label: '不包含', value: '8'},
    {label: '开始以', value: '9'}, {label: '结束以', value: '10'},
    {label: '是null', value: '11'}, {label: '不是null', value: '12'},
    {label: '是空的', value: '13'}, {label: '不是空的', value: '14'},
    {label: '正则匹配', value: '15'},
];

const KeyValueEditor: React.FC<IState> = (props) => {
    const [rows, setRows] = useState(props.rows || []);
    const [type, setType] = useState(props.type);
    const [currIndex, setCurrIndex] = useState(-1);
    const [isDetailVisible, setIsDetailVisible] = useState(false);
    const [currKey, setCurrKey] = useState('');
    const [currValue, setCurrValue] = useState('');

    let nameTheadName = 'Name';
    let valueTheadName = 'Value';
    let typeThead;
    let domainThead;
    let pathThead;
    let relationOperatorThead;
    let assertLevelThead;
    let extractorTypeThead;
    let rowNumberThead;
    let spanSize = 8;
    let valueColSpanSize = 3;
    let descriptionSpanSize = 4;
    let namePlaceholder = '';

    if(type === 'form-data') {
        typeThead = <Col span={3}><div>类型</div></Col>;
        spanSize = 7;
        valueColSpanSize = 8;
    }else if(type === 'cookie') {
        spanSize = 5;
        valueColSpanSize = 5;
        domainThead = <Col span={spanSize}><div>Domain</div></Col>;
        pathThead = <Col span={spanSize}><div>Path</div></Col>;
    }else if(type === 'response-assert') {
        spanSize = 7;
        relationOperatorThead = <Col span={3}><div>关系</div></Col>;
        assertLevelThead = <Col span={3}><div>级别</div></Col>;
    }else if(type === 'jdbc-response-assert') {
        spanSize = 5;
        relationOperatorThead = <Col span={3}><div>关系</div></Col>;
        assertLevelThead = <Col span={3}><div>级别</div></Col>;
        rowNumberThead = <Col span={2}><div>行号</div></Col>;
        nameTheadName = '列名';
        valueTheadName = '预期值';
        namePlaceholder = '列名，如: card_no';
    }

    if(type === 'response-extractor' || type === 'response-assert') {
        spanSize = 8;
        extractorTypeThead = <Col span={3}><div>提取类型</div></Col>;
        nameTheadName = '参数路径';
        namePlaceholder = '如:$.code或者$.array[0].name或者xpath等';
    }

    if(type === 'jdbc-response-extractor') {
        spanSize = 6;
        nameTheadName = '列名';
        valueTheadName = '输出到变量';
        namePlaceholder = '列名，如: card_no';
        rowNumberThead = <Col span={2}><div>行号</div></Col>;
    }

    if(type !== 'cookie' && type !== 'response-assert') {
        descriptionSpanSize = spanSize;
        valueColSpanSize = 8;
    }

    if(type === 'response-assert') {
        valueTheadName = '预期值';
        valueColSpanSize = 3;
        descriptionSpanSize = 4;
    }

    if(type === 'jdbc-response-assert') {
        spanSize = 5
        valueTheadName = '预期值';
        valueColSpanSize = 5;
        descriptionSpanSize = 6;
    }

    if(type ==='form-data') {
        descriptionSpanSize = 6;
    }

    if(type === 'response-extractor') {
        valueTheadName = '输出到变量';
        valueColSpanSize = 5;
        descriptionSpanSize = 8;
    }

    if(type === 'jdbc-response-extractor') {
        valueColSpanSize = 8;
        descriptionSpanSize = 8;
    }

    if(type !== props.type) {
        setType(props.type);
    }

    if(JSON.stringify(rows) !== JSON.stringify(props.rows||[])) {
        for (let i = 0; i < props.rows.length; i++) {
            props.rows[i]['key'] = RandomUtils.getKey();
        }
        setRows(props.rows||[]);
    }else if(rows.length > 0 && !rows[0]['key']) {
        for (let i = 0; i < rows.length; i++) {
            rows[i]['key'] = RandomUtils.getKey();
        }
        setRows(rows);
    }

    function add() {
        const row: KeyValueRow = {name: '', value: '',
            description: '', type: 'text', domain: '',
            extractorType: '1',
            path: '', relationOperator: '1', assertLevel: 'error'};
        row['key'] = RandomUtils.getKey();
        rows.push(row);
        setRows(rows);
    }

    function onRemove() {
        rows.splice(currIndex, 1);
        setRows(rows);
    }

    function onClickRow(index: number) {
        setCurrIndex(index);
    }

    function onChangeName(value: any) {
        rows[currIndex].name = value.target.value;
    }

    function onChangeDomain(value: any) {
        rows[currIndex].domain = value.target.value;
    }

    function onChangeRowNumber(value: any) {
        rows[currIndex].rowNumber = value.target.value;
    }

    function onChangeValue(value: any) {
        rows[currIndex].value = value;
    }

    function onChangePath(value: any) {
        rows[currIndex].path = value.target.value;
    }

    function onChangeDescription(value: any) {
        rows[currIndex].description = value.target.value;
    }

    function onChangeType(value) {
        rows[currIndex].type = value;
    }

    function onChangeExtractorType(value) {
        rows[currIndex].extractorType = value;
    }

    function onChangeRelationOperatorType(value) {
        rows[currIndex].relationOperator = value;
    }

    function onChangeAssertLevel(value) {
        rows[currIndex].assertLevel = value;
    }

    function renderRelationOperator() {
        return relationOperatorList.map(v => {
           return  <Option value={v.value} key={v.value}>{v.label}</Option>
        });
    }

    function renderRows() {
        const options = props.userDefinedVariables?.map(v => {
            return {label: v.name, value: '${' + v.name + '}'};
        }) as any[];
        return rows.map((value, index) => {
            const paramsRowSelectedClass = index === currIndex ? "common-row-selected" : "";
            let typeCol;
            let domainCol;
            let pathCol;
            let relationOperatorCol;
            let assertLevelCol;
            let extractorTypeCol;
            let countNumberCol;

            if(type === 'form-data') {
                typeCol = <Col span={3} style={{paddingRight: '5px'}}>
                    <Select defaultValue={value.type} style={{ width: '100%' }} onChange={onChangeType}>
                        <Option value="text">text</Option>
                        <Option value="file">file</Option>
                    </Select>
                </Col>
            }else if(type === 'cookie') {
                domainCol = <Col span={spanSize} style={{paddingRight: '5px'}}>
                    <Input defaultValue={value.domain} onChange={onChangeDomain}/>
                </Col>;
                pathCol = <Col span={spanSize} style={{paddingRight: '5px'}}>
                    <Input defaultValue={value.path} onChange={onChangePath}/>
                </Col>;
            }else if(type === 'response-assert' || type === 'jdbc-response-assert') {
                relationOperatorCol = <Col span={3} style={{paddingRight: '5px'}}>
                    <Select defaultValue={value.relationOperator} style={{ width: '100%' }} onChange={onChangeRelationOperatorType}>
                        {renderRelationOperator()}
                    </Select>
                </Col>
                assertLevelCol = <Col span={3} style={{paddingRight: '5px'}}>
                    <Select defaultValue={value.assertLevel} style={{ width: '100%' }} onChange={onChangeAssertLevel}>
                        <Option value="error">ERROR</Option>
                        <Option value="warning">WARNING</Option>
                    </Select>
                </Col>
            }

            if(type === 'response-extractor' || type === 'response-assert') {
                extractorTypeCol = <Col span={3} style={{paddingRight: '5px'}}>
                    <Select defaultValue={value.extractorType} style={{ width: '100%' }} onChange={onChangeExtractorType}>
                        <Option value="1">响应体</Option>
                        <Option value="2">Cookie</Option>
                        <Option value="3">响应头</Option>
                    </Select>
                </Col>
            }

            if(type === 'jdbc-response-extractor' || type === 'jdbc-response-assert') {
                countNumberCol = <Col span={2} style={{paddingRight: '5px'}}>
                    <Input type="number" defaultValue={value.rowNumber} onChange={onChangeRowNumber} placeholder="默认第一行"/>
                </Col>;
            }

            return (<Row className={paramsRowSelectedClass} style={{marginBottom: '5px'}} key={value['key']} onClick={() => {onClickRow(index)}}>
                {countNumberCol}
                <Col span={spanSize} style={{paddingRight: '5px'}}>
                    <Input placeholder={namePlaceholder} defaultValue={value.name} onChange={onChangeName}/>
                </Col>
                {typeCol}
                {extractorTypeCol}
                {relationOperatorCol}
                <Col span={valueColSpanSize} style={{paddingRight: '5px'}}>
                    <AutoComplete
                        defaultValue={value.value} onChange={onChangeValue}
                        style={{width: '100%'}}
                        options={options}
                    />
                </Col>
                {assertLevelCol}
                {domainCol}
                {pathCol}
                <Col span={descriptionSpanSize}>
                    <Input defaultValue={value.description} onChange={onChangeDescription}/>
                </Col>
            </Row>)
        });
    }

    function onDetail() {
        if(!rows[currIndex]) {
            return ;
        }
        setIsDetailVisible(true);
        setCurrKey(rows[currIndex].name);
        setCurrValue(rows[currIndex].value);
    }

    function handleOk() {
        rows[currIndex].name = currKey;
        rows[currIndex].value = currValue;
        rows[currIndex]['key'] = RandomUtils.getKey();
        setRows([...rows]);
        setIsDetailVisible(false);
    }

    function handleCancel() {
        setIsDetailVisible(false);
    }

    function refreshContent(value: string) {
        setCurrValue(value);
    }

    function onChangeKeyInModal(e) {
        setCurrKey(e.target.value);
    }

    return (<div>
        <div className="variable-toolbar">
            <Button size="small" type="primary" onClick={add}>添加</Button>
            <Button size="small" type="primary" onClick={onRemove}>删除</Button>
            <Button size="small" type="primary" onClick={onDetail}>详细</Button>
        </div>
        <Row className="common-header">
            {rowNumberThead}
            <Col span={spanSize}><div>{nameTheadName}</div></Col>
            {typeThead}
            {extractorTypeThead}
            {relationOperatorThead}
            <Col span={valueColSpanSize}><div>{valueTheadName}</div></Col>
            {assertLevelThead}
            {domainThead}
            {pathThead}
            <Col span={descriptionSpanSize}><div>描述</div></Col>
        </Row>
        {renderRows()}

        <Modal width={800} title="详细" open={isDetailVisible} onOk={handleOk} onCancel={handleCancel}>
            <Row style={{paddingBottom: '5px'}}>
                <Col flex="100px">{nameTheadName}</Col>
                <Col flex="auto">
                    <Input value={currKey} onChange={onChangeKeyInModal}/>
                </Col>
            </Row>
            <Row style={{paddingBottom: '5px'}}>
                <Col flex="100px">{valueTheadName}</Col>
                <Col flex="auto">
                    <ContentEditor userDefinedVariables={props.userDefinedVariables} language={'sql'} content={currValue} refreshContent={refreshContent}></ContentEditor>
                </Col>
            </Row>
        </Modal>
    </div>)
}

export {KeyValueEditor}
