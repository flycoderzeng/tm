import React, {useState} from "react";
import {EditorIState} from "../entities/EditorIState";
import {JDBCRequestNode} from "../entities/JDBCRequestNode";
import {CommonNameComments} from "./CommonNameComments";
import {AutoComplete, Col, Input, Row, Tabs} from "antd";
import {ContentEditor} from "./ContentEditor";
import {KeyValueEditor} from "./KeyValueEditor";
const { TabPane } = Tabs;

const JDBCRequestEditor: React.FC<EditorIState<JDBCRequestNode>> = (props) => {
    const [dbName, setDbName] = useState(props.define.dbName);
    const [retryTimes, setRetryTimes] = useState(props.define.retryTimes);
    const [content, setContent] = useState(props.define.content);
    const [resultSetVariableName, setResultSetVariableName] = useState(props.define.resultSetVariableName);
    const [countVariableName, setCountVariableName] = useState(props.define.countVariableName);
    if(dbName !== props.define.dbName) {
        setDbName(props.define.dbName);
    }
    if(content !== props.define.content) {
        setContent(props.define.content);
    }
    if(resultSetVariableName !== props.define.resultSetVariableName) {
        setResultSetVariableName(props.define.resultSetVariableName);
    }
    if(countVariableName !== props.define.countVariableName) {
        setCountVariableName(props.define.countVariableName);
    }

    function onChangeResultSetVariableName(v: any) {
        setResultSetVariableName(v);
        props.define.resultSetVariableName = v;
    }

    function onChangeCountVariableName(v: any) {
        setCountVariableName(v);
        props.define.countVariableName = v;
    }

    function refreshContent(value) {
        props.define.content = value;
        setContent(value);
    }

    function onChangeDbName(el: any) {
        setDbName(el.target.value);
        props.define.dbName = el.target.value;
    }

    function onChangeRetryTimes(el: any) {
        setRetryTimes(el.target.value);
        props.define.retryTimes = el.target.value;
    }

    const options = props.userDefinedVariables?.map(v => {
        return {label: v.name, value: '${' + v.name + '}'};
    }) as any[];

    return (<div>
        <CommonNameComments refreshTree={props.refreshTree} stepNode={props.stepNode} define={props.define}>
        </CommonNameComments>
        <div>
            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="120px">数据库名称</Col>
                <Col flex="auto">
                    <Input placeholder="数据库名称，如：test" value={dbName} onChange={onChangeDbName}/>
                </Col>
            </Row>

            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex="120px">结果集输出到变量</Col>
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
                <Col flex="120px">总行数输出到变量</Col>
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
                <Col flex="120px">重试次数</Col>
                <Col flex="auto">
                    <Input type="number" placeholder="当出现连接超时、断言失败等情况时的重试次数" value={retryTimes} onChange={onChangeRetryTimes}/>
                </Col>
            </Row>
            <Tabs defaultActiveKey="1">
                <TabPane tab="sql语句" key="1">
                    <ContentEditor userDefinedVariables={props.userDefinedVariables}
                                   refreshContent={refreshContent}
                                   language={'sql'}
                                   content={content}>
                    </ContentEditor>
                </TabPane>
                <TabPane tab="结果断言" key="2">
                    <KeyValueEditor userDefinedVariables={props.userDefinedVariables}
                                    rows={props.define.checkErrorList}
                                    type={'jdbc-response-assert'}>
                    </KeyValueEditor>
                </TabPane>
                <TabPane tab="变量保存" key="3">
                    <KeyValueEditor userDefinedVariables={props.userDefinedVariables}
                                    rows={props.define.responseExtractorList}
                                    type={'jdbc-response-extractor'}>
                    </KeyValueEditor>
                </TabPane>
            </Tabs>
        </div>
    </div>)
}

export {JDBCRequestEditor}
