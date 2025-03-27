import React, {useEffect, useState} from "react";
import {EditorIState} from "../entities/EditorIState";
import {CommonNameComments} from "./CommonNameComments";
import {AutoComplete, Col, Input, Radio, Row, Select, Tabs} from "antd";
import {HttpNode} from "../entities/HttpNode";
import {KeyValueEditor} from "./KeyValueEditor";
import {ContentEditor} from "./ContentEditor";
import {WindowTopUtils} from "../../../../utils/WindowTopUtils";
import {KeyValueRow} from "../entities/KeyValueRow";
import {DCNSelect} from "../../../testmanage/dcnconfig/DCNSelect";

const {Option} = Select;

const HttpEditor: React.FC<EditorIState<HttpNode>> = (props) => {
    const [bodyType, setBodyType] = useState(props.define.bodyType);
    const [rawType, setRawType] = useState(props.define.rawType);
    const [content, setContent] = useState(props.define.content);
    const [url, setUrl] = useState(props.define.url);
    const [saveFileName, setSaveFileName] = useState(props.define.saveFileName);
    const [requestType, setRequestType] = useState(props.define.requestType);
    const [rawLanguage, setRawLanguage] = useState('json');
    const [activeKey, setActiveKey] = useState('1');

    const [dcnId, setDcnId] = useState(props.define.dcnId);

    const [params, setParams] = useState(props.define.params);
    const [headers, setHeaders] = useState(props.define.headers);
    const [checkErrorList, setCheckErrorList] = useState(props.define.checkErrorList);
    const [responseExtractorList, setResponseExtractorList] = useState(props.define.responseExtractorList);

    const {onChange} = props;
    const {stepNode} = props;

    if (!WindowTopUtils.getWindowTopObject('activeTabJson')) {
        WindowTopUtils.setWindowTopObject('activeTabJson', {});
    }

    useEffect(() => {
        setBodyType(stepNode.define.bodyType);
        setRawType(stepNode.define.rawType);
        setRawLanguage(stepNode.define.rawType);
        setUrl(stepNode.define.url);
        setRequestType(stepNode.define.requestType);
        setContent(stepNode.define.content);
        setParams(props.define.params);
        setHeaders(props.define.headers);
        setCheckErrorList(props.define.checkErrorList);
        setResponseExtractorList(props.define.responseExtractorList);
    }, [props.key]);

    if (WindowTopUtils.getWindowTopObject('activeTabJson')[stepNode.key] &&
        WindowTopUtils.getWindowTopObject('activeTabJson')[stepNode.key] !== activeKey) {
        setActiveKey(WindowTopUtils.getWindowTopObject('activeTabJson')[stepNode.key]);
    }

    function onChangeTab(key) {
        WindowTopUtils.getWindowTopObject(WindowTopUtils.object_activeTabJson)[stepNode.key] = key;
        setActiveKey(key);
    }

    function onChangeDcnId(v: any) {
        setDcnId(v);
        onChange('dcnId', v);
    }

    function onChangeBodyType(e) {
        setBodyType(e.target.value);
        onChange('bodyType', e.target.value);
    }

    function onChangeRawType(value) {
        setRawType(value);
        if (value === 'text') {
            setRawLanguage('plaintext');
        } else {
            setRawLanguage(value);
        }

        onChange('rawType', value);
    }

    function refreshContent(value) {
        setContent(value);
        onChange('content', value);
    }

    function onChangeUrl(value: any) {
        setUrl(value.target.value);
        onChange('url', value.target.value);
    }

    function onChangeSaveFileName(value: any) {
        setSaveFileName(value);
        onChange('saveFileName', value);
    }

    function onChangeRequestType(value) {
        setRequestType(value);
        onChange('requestType', value);
    }

    function onChangeParams(params: KeyValueRow[]) {
        setParams(params);
        onChange('params', params);
    }

    function onChangeHeaders(headers: KeyValueRow[]) {
        setHeaders(headers);
        onChange('headers', headers);
    }

    function onChangeCheckErrorList(checkErrorList: KeyValueRow[]) {
        setCheckErrorList(checkErrorList);
        onChange('checkErrorList', checkErrorList);
    }

    function onChangeResponseExtractorList(responseExtractorList: KeyValueRow[]) {
        setResponseExtractorList(responseExtractorList);
        onChange('responseExtractorList', responseExtractorList);
    }

    function renderBodyArea() {
        let rawTypeSelect;
        if (bodyType === 'raw') {
            rawTypeSelect =
                <Select value={rawType} defaultValue={rawType} style={{width: 120}} onChange={onChangeRawType}>
                    <Option value="text">text</Option>
                    <Option value="json">json</Option>
                    <Option value="xml">xml</Option>
                </Select>
        }
        let contentArea;
        if (bodyType === 'raw') {
            contentArea = (<ContentEditor userDefinedVariables={props.userDefinedVariables}
                                          refreshContent={refreshContent}
                                          language={rawLanguage}
                                          content={content}>
            </ContentEditor>)
        } else if (bodyType === 'form-data') {
            contentArea = (<KeyValueEditor rows={props.define.formData} type={'form-data'}>
            </KeyValueEditor>)
        } else if (bodyType === 'x-www-form-urlencoded') {
            contentArea = (<KeyValueEditor rows={props.define.formUrlencoded} type={''}>
            </KeyValueEditor>)
        }
        return <div>
            <div>
                <Radio.Group onChange={onChangeBodyType} value={bodyType}>
                    <Radio value="none">none</Radio>
                    <Radio value="form-data">form-data</Radio>
                    <Radio value="x-www-form-urlencoded">x-www-form-urlencoded</Radio>
                    <Radio value="raw">raw</Radio>
                </Radio.Group>
                {rawTypeSelect}
            </div>
            <div style={{paddingTop: '5px'}}>
                {contentArea}
            </div>
        </div>
    }

    const options = props.userDefinedVariables?.map(v => {
        return {label: v.name, value: '${' + v.name + '}'};
    }) as any[];
    let width = '160px';

    return (<div>
        <CommonNameComments refreshTree={props.refreshTree} key={props.key}
                            stepNode={stepNode}
                            define={stepNode.define} onChange={onChange}>
        </CommonNameComments>
        <div style={{paddingTop: '5px'}}>
            <Input.Group compact style={{display: "flex"}}>
                <Select defaultValue={requestType} value={requestType} style={{width: '200px'}}
                        onChange={onChangeRequestType}>
                    <Option value="POST">POST</Option>
                    <Option value="GET">GET</Option>
                </Select>
                <Input defaultValue={url} value={url} placeholder="请求地址" onChange={onChangeUrl}/>
            </Input.Group>
        </div>
        <div style={{paddingTop: '5px'}}>
            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex={width}>分布式节点</Col>
                <Col flex="auto">
                    <DCNSelect value={dcnId} onChange={onChangeDcnId} style={{width: '250px'}}></DCNSelect>
                </Col>
            </Row>
        </div>
        <div style={{paddingTop: '5px'}}>
            <Tabs defaultActiveKey="1" activeKey={activeKey} onChange={onChangeTab} items={[{
                label: 'Params', key: '1', children: (<KeyValueEditor userDefinedVariables={props.userDefinedVariables}
                                                                      rows={params}
                                                                      onChange={onChangeParams}
                                                                      type={''}>
                </KeyValueEditor>)
            }, {
                label: 'Headers', key: '2', children: (<KeyValueEditor userDefinedVariables={props.userDefinedVariables}
                                                                       rows={headers}
                                                                       onChange={onChangeHeaders}
                                                                       type={''}>
                </KeyValueEditor>)
            }, {label: 'Body', key: '3', children: renderBodyArea()}, {
                label: '响应断言',
                key: '4',
                children: (<KeyValueEditor userDefinedVariables={props.userDefinedVariables}
                                           rows={checkErrorList}
                                           onChange={onChangeCheckErrorList}
                                           type={'response-assert'}>
                </KeyValueEditor>)
            }, {
                label: '响应提取',
                key: '5',
                children: (<KeyValueEditor userDefinedVariables={props.userDefinedVariables}
                                           rows={responseExtractorList}
                                           onChange={onChangeResponseExtractorList}
                                           type={'response-extractor'}>
                </KeyValueEditor>)
            }]}>
            </Tabs>
        </div>
        <div style={{paddingTop: '5px'}}>
            <Row style={{paddingBottom: '5px', alignItems: 'center'}}>
                <Col flex={width}>保存文件名</Col>
                <Col flex="auto">
                    <AutoComplete placeholder="保存文件的名称" value={saveFileName}
                                  style={{width: '100%'}} options={options} onChange={onChangeSaveFileName}/>
                </Col>
            </Row>
        </div>
    </div>)
}

export {HttpEditor};
