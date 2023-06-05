import React, {useState} from "react";
import {EditorIState} from "../entities/EditorIState";
import {CommonNameComments} from "./CommonNameComments";
import {Input, Tabs, Select, Radio} from "antd";
import {HttpNode} from "../entities/HttpNode";
import {KeyValueEditor} from "./KeyValueEditor";
import {ContentEditor} from "./ContentEditor";

const { TabPane } = Tabs;

const { Option } = Select;

const HttpEditor: React.FC<EditorIState<HttpNode>> = (props) => {
    const [bodyType, setBodyType] = useState(props.define.bodyType);
    const [rawType, setRawType] = useState(props.define.rawType);
    const [content, setContent] = useState(props.define.content);
    const [url, setUrl] = useState(props.define.url);
    const [requestType, setRequestType] = useState(props.define.requestType);
    const [rawLanguage, setRawLanguage] = useState('json');
    const [activeKey, setActiveKey] = useState('1');

    if(bodyType !== props.define.bodyType) {
        setBodyType(props.define.bodyType);
    }

    if(rawType !== props.define.rawType) {
        setRawType(props.define.rawType);
        setRawLanguage(props.define.rawType);
    }

    if(url !== props.define.url) {
        setUrl(props.define.url);
    }

    if(requestType !== props.define.requestType) {
        setRequestType(props.define.requestType);
    }

    if(content !== props.define.content) {
        setContent(props.define.content);
    }

    function onChangeTab(key) {
        setActiveKey(key);
    }

    function onChangeBodyType(e) {
        setBodyType(e.target.value);
        props.define.bodyType = e.target.value;
    }

    function onChangeRawType(value) {
        setRawType(value);
        if(value === 'text') {
            setRawLanguage('plaintext');
        }else{
            setRawLanguage(value);
        }

        props.define.rawType = value;
    }

    function refreshContent(value) {
        props.define.content = value;
        setContent(value);
    }

    function onChangeUrl(value: any) {
        props.define.url = value.target.value;
        setUrl(value.target.value);
    }

    function onChangeRequestType(value) {
        props.define.requestType = value;
        setRequestType(value);
    }

    function renderBodyArea() {
        let rawTypeSelect;
        if(bodyType === 'raw') {
            rawTypeSelect = <Select value={rawType} defaultValue={rawType} style={{ width: 120 }} onChange={onChangeRawType}>
                <Option value="text">text</Option>
                <Option value="json">json</Option>
                <Option value="xml">xml</Option>
            </Select>
        }
        let contentArea;
        if(bodyType === 'raw') {
            contentArea = (<ContentEditor userDefinedVariables={props.userDefinedVariables}
                                          refreshContent={refreshContent}
                                          language={rawLanguage}
                                          content={content}>
            </ContentEditor>)
        }else if(bodyType === 'form-data') {
            contentArea = (<KeyValueEditor rows={props.define.formData} type={'form-data'}>
            </KeyValueEditor>)
        }else if(bodyType === 'x-www-form-urlencoded') {
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

    return (<div>
        <CommonNameComments refreshTree={props.refreshTree}
                            stepNode={props.stepNode}
                            define={props.define}>
        </CommonNameComments>
        <div style={{paddingTop: '5px'}}>
            <Input.Group compact style={{display: "flex"}}>
                <Select defaultValue={requestType} value={requestType} style={{width: '200px'}} onChange={onChangeRequestType}>
                    <Option value="POST">POST</Option>
                    <Option value="GET">GET</Option>
                </Select>
                <Input defaultValue={url} value={url} placeholder="请求地址" onChange={onChangeUrl}/>
            </Input.Group>
        </div>
        <div style={{paddingTop: '5px'}}>
            <Tabs defaultActiveKey="1" activeKey={activeKey} onChange={onChangeTab} items={[{label: 'Params', key: '1', children: (<KeyValueEditor userDefinedVariables={props.userDefinedVariables}
                                                                                                                             rows={props.define.params}
                                                                                                                             type={''}>
                </KeyValueEditor>)}, {label: 'Headers', key: '2', children: (<KeyValueEditor userDefinedVariables={props.userDefinedVariables}
                                                                                             rows={props.define.headers}
                                                                                             type={''}>
                </KeyValueEditor>)}, {label: 'Body', key: '3', children: renderBodyArea()}, {
                label: '响应断言', key: '4', children: (<KeyValueEditor userDefinedVariables={props.userDefinedVariables}
                                                                        rows={props.define.checkErrorList}
                                                                        type={'response-assert'}>
                </KeyValueEditor>)
            }, {label: '响应提取', key:'5', children: (<KeyValueEditor userDefinedVariables={props.userDefinedVariables}
                                                                       rows={props.define.responseExtractorList}
                                                                       type={'response-extractor'}>
                </KeyValueEditor>)}]}>
            </Tabs>
        </div>
    </div>)
}

export {HttpEditor};
