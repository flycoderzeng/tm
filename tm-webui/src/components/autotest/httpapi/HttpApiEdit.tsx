import React from "react";
import { withRouter } from "react-router-dom";
import { RouteComponentProps } from "react-router-dom";
import {Form, Input, Button, Tooltip, Select, Radio, message} from "antd";
import {ArrowLeftOutlined} from "@ant-design/icons";
import {FormInstance} from "antd/lib/form";
import { Tabs } from 'antd';
import MonacoEditor from 'react-monaco-editor';

// 引入编辑器组件
import BraftEditor from 'braft-editor'
// 引入编辑器样式
import 'braft-editor/dist/index.css'


import { Collapse } from 'antd';
import { CaretRightOutlined } from '@ant-design/icons';
import { JsonDefineEditor } from "./JsonDefineEditor";
import {FormEditor} from "./FormEditor";
import {HttpApiPreview} from "./HttpApiPreview";
import {HttpApiModel} from "../../../entities/HttpApiModel";
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {RandomUtils} from "../../../utils/RandomUtils";

const { Panel } = Collapse;

const { TabPane } = Tabs;
const { Option } = Select;


interface IProps {}
type AppApiProps = IProps & RouteComponentProps;

interface IState {
    appApi: HttpApiModel,
    remarkBraft: any,
    inputType: string;
    ref: any;
    saving: boolean
    setRenderRightFlag: any;
    load: any;

    initialValues: any;
}
const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
};
const tailLayout = {
    wrapperCol: { offset: 8, span: 16 },
};

class HttpApiEdit extends React.Component<AppApiProps, IState> {
    constructor(props) {
        super(props);
        const ref = React.createRef<FormInstance>();
        this.state = {
            appApi: {
                id: props.id,
                method: '2',
                reqBodyOther: '{"type":"object", "name": "root", "title": "", "description": "", "properties":{}}',
                reqDefineRows: [],
                reqBodyForm: [],
                reqBodyKv: [],
                reqParams: [],
                reqHeaders: [],
                resBody: '{"type":"object", "name": "root", "title": "", "description": "", "properties":{}}',
                resBodyType: '1',
                resDefineRows: [],
                reqBodyMessage: '',
                resBodyMessage: '',
                reqBodyType: '3',
                remark: '',
                name: '',
                description: '',
                url: '',
            },
            remarkBraft: BraftEditor.createEditorState(null),
            inputType: '1',
            load: this.load,
            setRenderRightFlag: props.setRenderRightFlag,
            ref: ref,
            saving: false,

            initialValues: {
                name: '',
                url: '',
                description: ''
            }
        }
    }

    getArrayFromString = (json: string) => {
        try {
            const array = JSON.parse(json) || [];
            return array;
        } catch (e) {
            return [];
        }
    }

    load = (id: any) => {
        if(!id) {
            return ;
        }
        axios.post(ApiUrlConfig.LOAD_APP_API_URL, {id: id}).then(resp => {
            if (resp.status !== 200) {
                message.error('加载接口信息失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                    return;
                }
                const appApi: HttpApiModel = ret.data;
                appApi.method = appApi.method + '';
                appApi.reqBodyType = appApi.reqBodyType + '';
                appApi.resBodyType = appApi.resBodyType + '';
                appApi.reqBodyOther = appApi.reqBodyOther || '{"type":"object", "name": "root", "title": "", "description": "", "properties":{}}';
                appApi.resBody = appApi.resBody || '{"type":"object", "name": "root", "title": "", "description": "", "properties":{}}';
                appApi.reqBodyForm = this.getArrayFromString(ret.data.reqBodyForm);
                appApi.reqParams = this.getArrayFromString(ret.data.reqParams);
                appApi.reqHeaders = this.getArrayFromString(ret.data.reqHeaders);
                appApi.reqBodyKv = this.getArrayFromString(ret.data.reqBodyKv);

                const reqDefineRows = this.changeDefineToRows(appApi.reqBodyOther);
                const resDefineRows = this.changeDefineToRows(appApi.resBody);
                appApi.reqDefineRows = reqDefineRows;
                appApi.resDefineRows = resDefineRows;
                this.setState({
                    appApi: appApi,
                    remarkBraft: BraftEditor.createEditorState(appApi.remark),
                    initialValues: {
                        name: appApi.name,
                        description: appApi.description,
                        url: appApi.url,
                    }
                });
            }
        });
    }

    changeDefineToRows(defineJson: any): any[] {
        if(!defineJson) {
            return [];
        }
        try {
            defineJson = JSON.parse(defineJson);
        } catch (e) {
            return [];
        }
        const defineRows: any[] = [];
        defineRows.push({name: 'root', level: 0, paddingLeft: '0px', key: RandomUtils.getKey(), title: defineJson.title,
            description: defineJson.description,
            disabled: true, children: [], define: defineJson, expanded: true, type: defineJson.type,
            displayChild: 'block',
            required: false});
        this.transferDefineJsonToRows(defineRows[0], defineJson.required||[]);
        return defineRows;
    }

    transferDefineJsonToRows(currRow: any, required: string[]) {
        if(currRow.define.properties) {
            for(const key in currRow.define.properties) {
                const childRow = {name: key, level: (currRow.level+1), paddingLeft: '0px', key: RandomUtils.getKey(),
                    disabled: false, children: [], define: currRow.define.properties[key], expanded: true,
                    displayChild: 'block', title: currRow.define.properties[key].title,
                    description: currRow.define.properties[key].description,
                    required: required.indexOf(key) > -1};
                childRow.paddingLeft = childRow.level * 10 + 'px';
                currRow.children.push(childRow);
                this.transferDefineJsonToRows(childRow, currRow.define.properties[key].required || []);
            }
        }
        if(currRow.define.items) {
            const childRow = {name: 'Items', paddingLeft: '0px', expanded: true,
                disabled: true, level: (currRow.level+1), title: '', description: '',
                displayChild: 'block', key: RandomUtils.getKey(),
                children: [], define: currRow.define.items,
                required: false
            }
            childRow.paddingLeft = childRow.level * 10 + 'px';
            currRow.children.push(childRow);
            this.transferDefineJsonToRows(childRow, currRow.define.items.required || []);
        }
    }

    changeRowsToDefineJson(defineRows: any[]) {
        const root = defineRows[0];
        const defineJson = {type:"object", name: 'root', title: root.title,
            description: root.description, required: [], properties:{}};
        if(!defineRows || defineRows.length < 1) {
            return defineJson;
        }

        defineJson.type = root.type;
        defineJson.title = root.title;
        defineJson.description = root.description;
        if(root.children && root.children.length > 0) {
            this.transferRowsToDefineJson(root.children, defineJson);
        }
        return defineJson;
    }

    transferRowsToDefineJson(rows: any[], defineJson: any) {
        for (let i = 0; i < rows.length; i++) {
            const define = {type: rows[i].define.type, name: rows[i].name, title: rows[i].title,
                description: rows[i].description};
            if(defineJson.type === 'object') {
                defineJson.properties[define.name] = define;
            }else if(defineJson.type === 'array') {
                defineJson.items = define;
                defineJson.items['required'] = [];
            }
            if(rows[i].required) {
                if(defineJson.required) {
                    defineJson.required.push(rows[i].name);
                }else if(defineJson.items && defineJson.items.required) {
                    defineJson.items.required.push(rows[i].name);
                }
            }
            if(rows[i].children && rows[i].children.length > 0) {
                if(define.type === 'object') {
                    define['required'] = [];
                    define['properties'] = {};
                }else if(define.type === 'array') {
                    define['items'] = {};
                }
                this.transferRowsToDefineJson(rows[i].children, define);
            }
        }
    }

    changeRowsToJsonMessage = (defineRows: any[]) => {
        const root = defineRows[0];
        const jsonMessage = {};
        if(!defineRows || defineRows.length < 1) {
            return jsonMessage;
        }
        if(root.children && root.children.length > 0) {
            this.transferRowsToJsonMessage(root, jsonMessage);
        }
        return jsonMessage;
    }

    transferRowsToJsonMessage = (root: any, jsonMessage: any) => {
        const rows = root.children || [];
        for (let i = 0; i < rows.length; i++) {
            if(rows[i].define.type === 'string' && root.define.type !== 'array') {
                jsonMessage[rows[i].name] = "";
            }else if(rows[i].define.type === 'string' && root.define.type === 'array') {
                jsonMessage.push("");
            }else if(rows[i].define.type === 'integer' && root.define.type !== 'array') {
                jsonMessage[rows[i].name] = 1;
            }else if(rows[i].define.type === 'integer' && root.define.type === 'array') {
                jsonMessage.push(1);
            }else if(rows[i].define.type === 'boolean' && root.define.type !== 'array') {
                jsonMessage[rows[i].name] = true;
            }else if(rows[i].define.type === 'boolean' && root.define.type === 'array') {
                jsonMessage.push(true);
            }else if(rows[i].define.type === 'number' && root.define.type !== 'array') {
                jsonMessage[rows[i].name] = 3.14;
            }else if(rows[i].define.type === 'number' && root.define.type === 'array') {
                jsonMessage.push(3.14);
            }else if(rows[i].define.type === 'object' && root.define.type !== 'array') {
                jsonMessage[rows[i].name] = {};
            }else if(rows[i].define.type === 'object' && root.define.type === 'array') {
                jsonMessage.push({});
            }else if(rows[i].define.type === 'array') {
                jsonMessage[rows[i].name] = [];
            }
            if(rows[i].children && rows[i].children.length > 0 && root.define.type !== 'array') {
                this.transferRowsToJsonMessage(rows[i], jsonMessage[rows[i].name]);
            }else if(rows[i].children && rows[i].children.length > 0 && root.define.type === 'array') {
                this.transferRowsToJsonMessage(rows[i], jsonMessage[0]);
            }
        }
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if (prevState.id !== nextProps.id) {
            prevState.load(nextProps.id);
            return {
                ...prevState,
                id: nextProps.id,
            };
        }
        return null;
    }

    back() {
        this.state.setRenderRightFlag(0);
    }

    componentDidMount() {
        this.load(this.state.appApi.id);
    }

    onChangeReqBodyType = (e: any) => {
        this.setState({
            appApi: {
                ...this.state.appApi,
                reqBodyType: e.target.value,
            }
        });
    }

    onChangeInputType = (e: any) => {
        this.setState({
            inputType: e.target.value,
        });
    }

    renderEditor() {
        if(this.state.appApi.reqBodyType === '1') {
            return (<FormEditor key="1" defineRows={this.state.appApi.reqBodyForm} type="1"></FormEditor>)
        }
        if(this.state.appApi.reqBodyType === '2') {
            return (<FormEditor key="2" defineRows={this.state.appApi.reqBodyKv} type="2"></FormEditor>)
        }
        if(this.state.appApi.reqBodyType === '3') {
            return (<JsonDefineEditor key="3" id={this.state.appApi.id} defineRows={this.state.appApi.reqDefineRows}></JsonDefineEditor>)
        }
    }

    renderReqInputArea() {
        if(this.state.inputType === '1') {
            return (<div>
                <div style={{marginTop: '10px'}}>
                    <Radio.Group value={this.state.appApi.reqBodyType} onChange={this.onChangeReqBodyType}>
                        <Radio key="1" value="1">form-data</Radio>
                        <Radio key="2" value="2">x-www-from-urlencoded</Radio>
                        <Radio key="3" value="3">json</Radio>
                    </Radio.Group>
                </div>

                <div className="api-define-div">
                 {this.renderEditor()}
                </div>
            </div>)
        }
        if(this.state.inputType === '2') {
            return (<FormEditor key={4} defineRows={this.state.appApi.reqParams} type="3"></FormEditor>)
        }
        if(this.state.inputType === '3') {
            return (<FormEditor key={5} defineRows={this.state.appApi.reqHeaders} type="4"></FormEditor>)
        }
    }

    handleEditorChange = (remark) => {
        this.setState({
            remarkBraft: remark,
        });
    }

    filterEmptyNameItem = (rows: any[]) => {
        return rows.filter(value => {
            if(value.name.trim() !== '') {
                return true;
            }
            return false;
        });
    }

    onSave = () => {
        const values = this.state.ref.current.getFieldsValue();
        values.id = this.state.appApi.id;
        values.name = this.state.appApi.name;
        values.description = this.state.appApi.description;
        values.reqHeaders = JSON.stringify(this.filterEmptyNameItem(this.state.appApi.reqHeaders));
        values.reqParams = JSON.stringify(this.filterEmptyNameItem(this.state.appApi.reqParams));
        values.reqBodyKv = JSON.stringify(this.filterEmptyNameItem(this.state.appApi.reqBodyKv));
        values.reqBodyForm = JSON.stringify(this.filterEmptyNameItem(this.state.appApi.reqBodyForm));
        values.reqBodyType = this.state.appApi.reqBodyType;
        values.url = this.state.appApi.url;
        values.method = this.state.appApi.method;
        values.remark = this.state.remarkBraft.toHTML();
        values.reqBodyOther = JSON.stringify(this.changeRowsToDefineJson(this.state.appApi.reqDefineRows));
        values.resBody = JSON.stringify(this.changeRowsToDefineJson(this.state.appApi.resDefineRows));
        values.reqBodyMessage = JSON.stringify(this.changeRowsToJsonMessage(this.state.appApi.reqDefineRows), null, 2);
        values.resBodyMessage = JSON.stringify(this.changeRowsToJsonMessage(this.state.appApi.resDefineRows), null, 2);

        this.setState({
            saving: true
        });
        axios.post(ApiUrlConfig.SAVE_APP_API_URL, values).then(resp => {
            if (resp.status !== 200) {
                message.error('保存失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('保存成功');
                    this.load(ret.data);
                }
            }
        }).finally(() => {
            this.setState({
                saving: false
            });
        });
    }

    onChangeUrl = (value: any) => {
        this.setState({
            appApi: {
                ...this.state.appApi,
                url: value.target.value
            },
        });
    }

    onChangeName = (value: any) => {
        this.setState({
            appApi: {
                ...this.state.appApi,
                name: value.target.value
            },
        });
    }

    onChangeDescription = (value: any) => {
        this.setState({
            appApi: {
                ...this.state.appApi,
                description: value.target.value
            },
        });
    }

    onChangeMethod = (value: any) => {
        let inputType = '1';
        if(value === '1') {
            inputType = '2';
        }else{
            inputType = '1';
        }
        this.setState({
            inputType: inputType,
            appApi: {
                ...this.state.appApi,
                method: value
            },
        });
    }

    onClickResponseTab = (key: string, event: any) => {
        if (key === '2') {
            this.setState({
                appApi: {
                    ...this.state.appApi,
                    resBodyMessage: JSON.stringify(this.changeRowsToJsonMessage(this.state.appApi.resDefineRows), null, 2),
                }
            });
        }
    }

    render() {
        const language = 'json';
        const options = {
            selectOnLineNumbers: true,
            minimap: {
                enabled: false,
            },
            wordWrap: 'on' as any,
            readOnly: true,
            renderSideBySide: false
        };
        return <div className="card">
            <div className="card-header card-header-divider" style={{paddingLeft: '10px'}}>
                接口定义编辑
                <Tooltip title="返回">
                    <Button onClick={() => this.back()} type="primary" size="small" shape="circle"
                            icon={<ArrowLeftOutlined/>}/>
                </Tooltip>
                <span className="card-subtitle">被测系统接口的定义</span>
            </div>
            <div className="card-body" style={{paddingLeft: 25}}>
                <Collapse
                    bordered={false}
                    defaultActiveKey={['1', '2', '3', '4']}
                    expandIcon={({ isActive }) => <CaretRightOutlined rotate={isActive ? 90 : 0} />}
                    className="site-collapse-custom-collapse"
                >
                    <Panel header="基本设置" key="1" className="site-collapse-custom-panel">
                        <Form
                            {...layout}
                            name="basic"
                            initialValues={this.state.initialValues}
                            ref={this.state.ref}
                        >
                            <Form.Item
                                label="接口名称"
                                name="name"
                                rules={[{ required: true, message: '请输入接口名称!' }]}
                            >
                                <div>
                                    <Input onChange={this.onChangeName} value={this.state.appApi.name} defaultValue={this.state.appApi.name} />
                                </div>
                            </Form.Item>

                            <Form.Item
                                label="简要描述"
                                name="description"
                                rules={[{required: false}]}
                            >
                                <div>
                                    <Input.TextArea rows={4} onChange={this.onChangeDescription} value={this.state.appApi.description} defaultValue={this.state.appApi.description} />
                                </div>
                            </Form.Item>

                            <Form.Item
                                label="接口地址"
                                name="url"
                                rules={[{ required: true, message: '请输入接口地址!' }]}
                            >
                                <Input.Group compact>
                                    <Select value={this.state.appApi.method} onChange={this.onChangeMethod}>
                                        <Option key="1" value="1">GET</Option>
                                        <Option key="2" value="2">POST</Option>
                                    </Select>
                                    <Input onChange={this.onChangeUrl} style={{ width: '80%' }} value={this.state.appApi.url} defaultValue={this.state.appApi.url} />
                                </Input.Group>
                            </Form.Item>
                        </Form>
                    </Panel>
                    <Panel header="请求参数设置" key="2" className="site-collapse-custom-panel">
                        <div style={{textAlign: 'center'}}>
                            <Radio.Group defaultValue="1" value={this.state.inputType} buttonStyle="solid" onChange={this.onChangeInputType}>
                                <Radio.Button key="1" style={{display: this.state.appApi.method === '2' ? 'inline-block' : 'none'}} value="1">Body</Radio.Button>
                                <Radio.Button key="2" value="2">Params</Radio.Button>
                                <Radio.Button key="3" value="3">Headers</Radio.Button>
                            </Radio.Group>
                        </div>
                        {this.renderReqInputArea()}

                    </Panel>
                    <Panel header="返回数据设置" key="3" className="site-collapse-custom-panel">
                        <Tabs defaultActiveKey="1" onTabClick={this.onClickResponseTab}>
                            <TabPane tab="模板" key="1">
                                <div>
                                    <JsonDefineEditor key="10" id={this.state.appApi.id} defineRows={this.state.appApi.resDefineRows}></JsonDefineEditor>
                                </div>
                            </TabPane>
                            <TabPane tab="预览" key="2">
                                <div className="editor-container" >
                                    <MonacoEditor
                                        theme="vs-dark"
                                        language={language}
                                        value={this.state.appApi.resBodyMessage}
                                        options={options}
                                    />
                                </div>
                            </TabPane>
                        </Tabs>
                    </Panel>
                    <Panel header="备注" key="4" className="site-collapse-custom-panel">
                        <div style={{background: '#fff'}}>
                            <BraftEditor
                                value={this.state.remarkBraft}
                                onChange={this.handleEditorChange}
                            />
                        </div>
                    </Panel>
                </Collapse>

                <div className="bottom-tool-bar">
                    <Button type="primary" onClick={this.onSave} loading={this.state.saving}>保存</Button>
                </div>
            </div>
        </div>
    }
}

export default withRouter(HttpApiEdit);
