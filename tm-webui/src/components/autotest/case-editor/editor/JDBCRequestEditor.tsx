import React, {useEffect, useState} from "react";
import {EditorIState} from "../entities/EditorIState";
import {JDBCRequestNode} from "../entities/JDBCRequestNode";
import {CommonNameComments} from "./CommonNameComments";
import {AutoComplete, Col, Input, message, Row, Tabs} from "antd";
import {ContentEditor} from "./ContentEditor";
import {KeyValueEditor} from "./KeyValueEditor";
import {WindowTopUtils} from "../../../../utils/WindowTopUtils";
import axios from "axios";
import {ApiUrlConfig} from "../../../../config/api.url";

let loadedDatabaseNames = false;
let databaseNamesAll: any[] = [];

const JDBCRequestEditor: React.FC<EditorIState<JDBCRequestNode>> = (props) => {
    const [dbName, setDbName] = useState(props.define.dbName);
    const [activeKey, setActiveKey] = useState('1');
    const [retryTimes, setRetryTimes] = useState(props.define.retryTimes);
    const [content, setContent] = useState(props.define.content);
    const [resultSetVariableName, setResultSetVariableName] = useState(props.define.resultSetVariableName);
    const [countVariableName, setCountVariableName] = useState(props.define.countVariableName);
    const [autoIncrementPrimaryKeyVariableName, setAutoIncrementPrimaryKeyVariableName] = useState(props.define.autoIncrementPrimaryKeyVariableName);
    const [databaseNames, setDatabaseNames] = useState<any[]>(databaseNamesAll);
    const {onChange} = props;
    const {stepNode} = props;

    useEffect(() => {
        setDbName(props.define.dbName);
        setContent(props.define.content);
        setResultSetVariableName(props.define.resultSetVariableName);
        setCountVariableName(props.define.countVariableName);
    }, [props.key]);

    if(!loadedDatabaseNames) {
        loadedDatabaseNames = true;
        axios.get(ApiUrlConfig.GET_ALL_DATABASE_NAMES_URL).then(resp => {
            if (resp.status !== 200) {
                message.error('加载数据库名称列表失败');
            } else {
                const ret = resp.data;
                if(ret.data) {
                    const names: any[] = ret.data.map(v => {
                        return {label: v.dbName, value: v.dbName};
                    });
                    databaseNamesAll = names;
                    setDatabaseNames(names)
                }
            }
        });
    }

    if(!WindowTopUtils.getWindowTopObject('activeTabJson')) {
        WindowTopUtils.setWindowTopObject('activeTabJson', {});
    }

    if(WindowTopUtils.getWindowTopObject('activeTabJson')[props.stepNode.key] &&
        WindowTopUtils.getWindowTopObject('activeTabJson')[props.stepNode.key] !== activeKey) {
        setActiveKey(WindowTopUtils.getWindowTopObject('activeTabJson')[props.stepNode.key]);
    }

    function onChangeResultSetVariableName(v: any) {
        setResultSetVariableName(v);
        onChange('resultSetVariableName', v);
    }

    function onChangeCountVariableName(v: any) {
        setCountVariableName(v);
        onChange('countVariableName', v);
    }

    function onChangeAutoIncrementPrimaryKeyVariableName(v: any) {
        setAutoIncrementPrimaryKeyVariableName(v);
        onChange('autoIncrementPrimaryKeyVariableName', v);
    }

    function refreshContent(value) {
        setContent(value);
        onChange('content', value);
    }

    function onChangeDbName(v: any) {
        setDbName(v);
        onChange('dbName', v);
    }

    function onChangeRetryTimes(el: any) {
        setRetryTimes(el.target.value);
        onChange('retryTimes', el.target.value);
    }

    function onChangeTab(key) {
        WindowTopUtils.getWindowTopObject(WindowTopUtils.object_activeTabJson)[props.stepNode.key] = key;
        setActiveKey(key);
    }

    const options = props.userDefinedVariables?.map(v => {
        return {label: v.name, value: '${' + v.name + '}'};
    }) as any[];

    return (<div>
        <CommonNameComments refreshTree={props.refreshTree} stepNode={stepNode} define={stepNode.define} onChange={onChange}>
        </CommonNameComments>
        <div>
            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="130px">数据库名称</Col>
                <Col flex="auto">
                    <AutoComplete placeholder="数据库名称，如：test" value={dbName}
                                  style={{width: '100%'}} options={databaseNames} onChange={onChangeDbName}/>
                </Col>
            </Row>

            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="130px">结果集输出到变量</Col>
                <Col flex="auto">
                    <AutoComplete
                        placeholder="变量名"
                        value={resultSetVariableName} onChange={onChangeResultSetVariableName}
                        style={{width: '100%'}}
                        options={options}
                    />
                </Col>
            </Row>

            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="130px">总行数输出到变量</Col>
                <Col flex="auto">
                    <AutoComplete
                        placeholder="变量名"
                        value={countVariableName} onChange={onChangeCountVariableName}
                        style={{width: '100%'}}
                        options={options}
                    />
                </Col>
            </Row>

            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="130px">自增主键值输出到变量</Col>
                <Col flex="auto">
                    <AutoComplete
                        placeholder="变量名"
                        value={autoIncrementPrimaryKeyVariableName} onChange={onChangeAutoIncrementPrimaryKeyVariableName}
                        style={{width: '100%'}}
                        options={options}
                    />
                </Col>
            </Row>

            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="130px">重试次数</Col>
                <Col flex="auto">
                    <Input type="number" placeholder="当出现连接超时、断言失败等情况时的重试次数" value={retryTimes} onChange={onChangeRetryTimes}/>
                </Col>
            </Row>

            <Tabs defaultActiveKey="1" activeKey={activeKey} onChange={onChangeTab} style={{paddingBottom: '5px'}} items={[{label: 'sql语句', key: '1', children: (<ContentEditor userDefinedVariables={props.userDefinedVariables}
                                                                                                                                     refreshContent={refreshContent}
                                                                                                                                     language={'sql'}
                                                                                                                                     content={content}>
                </ContentEditor>)}, {label: '结果断言', key: '2', children: (<KeyValueEditor userDefinedVariables={props.userDefinedVariables}
                                                                                             rows={stepNode.define.checkErrorList}
                                                                                             type={'jdbc-response-assert'}>
                </KeyValueEditor>)}, {label: '变量保存', key: '3', children: (<KeyValueEditor userDefinedVariables={props.userDefinedVariables}
                                                                                              rows={stepNode.define.responseExtractorList}
                                                                                              type={'jdbc-response-extractor'}>
                </KeyValueEditor>)}]}>
            </Tabs>
        </div>
    </div>)
}

export {JDBCRequestEditor}
