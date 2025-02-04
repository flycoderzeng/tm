import React from "react";
import {FormInstance} from "antd/lib/form";
import axios from "axios";
import {Button, Form, Input, message, Select, Tooltip} from 'antd';
import {RouteComponentProps, withRouter} from "react-router-dom";
import {ArrowLeftOutlined} from '@ant-design/icons';
import MonacoEditor from 'react-monaco-editor';
import {ApiUrlConfig} from "../../../config/api.url";
import {DataTypeEnum} from "../../../entities/DataTypeEnum";

interface IProps {}
const { Option } = Select;
interface ShellModel {
    name: string;
    description: string;
    type: string;
    content: string;
}
interface ScriptTypeModel {
    display: string;
    value: string;
}
type ShellProps = IProps & RouteComponentProps;
interface IState {
    id: number;
    scriptTypeList: ScriptTypeModel[];
    ref: any;
    saving: boolean;
    language: string;
    initialValues: ShellModel;
    setRenderRightFlag: any;
    loadAutoScript: any;
}

const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
};
const tailLayout = {
    wrapperCol: { offset: 8, span: 16 },
};

class ShellEdit extends React.Component<ShellProps, IState> {
    constructor(props) {
        super(props);
        const ref = React.createRef<FormInstance>();
        this.state = {
            id: props.id,
            language: 'shell',
            loadAutoScript: this.loadScript,
            setRenderRightFlag: props.setRenderRightFlag,
            ref: ref,
            scriptTypeList: [],
            saving: false,
            initialValues: {
                name: '',
                description: '',
                type: '1',
                content: '',
            }
        }
    }

    loadScript = (id: any) => {
        axios.post(ApiUrlConfig.LOAD_AUTO_SCRIPT_URL, {id: id}).then(resp => {
            if (resp.status !== 200) {
                message.error('加载脚本失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    if (!ret.data) {
                        return;
                    }
                    this.state.ref.current.setFieldsValue({
                        name: ret.data.name,
                        description: ret.data.description,
                        type: ret.data.type+'',
                    });
                    this.setState({
                        initialValues: {
                            ...this.state.initialValues,
                            content: ret.data.content || ''
                        }
                    });
                    this.setLanguage(ret.data.type+'');
                }
            }
        });
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if (prevState.id !== nextProps.id) {
            prevState.loadAutoScript(nextProps.id);
            return {
                ...prevState,
                id: nextProps.id,
            };
        }
        return null;
    }

    back() {
        this.state.setRenderRightFlag(DataTypeEnum.ALL);
    }

    onFinish = values => {
        if(this.state.id > 0) {
            values['id'] = this.state.id;
        }
        values['content'] = this.state.initialValues.content;
        this.setState({saving: true});
        axios.post(ApiUrlConfig.SAVE_AUTO_SCRIPT_URL, values).then(resp => {
            if (resp.status !== 200) {
                message.error('保存失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                }
            }
        }).finally(() => {
            this.setState({saving: false});
        });
    }

    onFinishFailed = errorInfo => {

    };

    componentDidMount() {
        this.loadScriptTypeList();
        this.loadScript(this.state.id);
    }

    loadScriptTypeList() {
        axios.get(ApiUrlConfig.GET_SCRIPT_TYPE_LIST_URL).then(resp => {
            if (resp.status !== 200) {
                message.error('操作失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    this.setState({
                        scriptTypeList: ret.data
                    });
                }
            }
        });
    }

    renderScriptTypeList = () => {
        return this.state.scriptTypeList.map(row => {
            return <Option key={row.value} value={row.value}>{row.display}</Option>
        });
    }

    onChangeHandle = (value, e) => {
        this.setState({
            initialValues: {
                ...this.state.initialValues,
                content: value,
            }
        });
    }

    onChangeScriptType = (value, e) => {
        this.setLanguage(value);
    }

    setLanguage(scriptType: string) {
        if(scriptType === '1') {
            this.setState({
                language: 'shell'
            });
        }else if(scriptType === '2' || scriptType === '3') {
            this.setState({
                language: 'python'
            });
        }else{
            this.setState({
                language: 'shell'
            });
        }
    }

    editorDidMountHandle(editor, monaco) {
        editor.focus();
        editor.setSelection(new monaco.Range(1, 1, 1, 1));
    }

    render() {
        const options = {
            selectOnLineNumbers: true,
            renderSideBySide: false,
            autoFocus: false,
            automaticLayout: true,
        };
        return <div className="card stretch-left">
            <div className="card-header card-header-divider">
                编辑脚本
                <Tooltip title="返回">
                    <Button onClick={() => this.back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
                </Tooltip>
                <span className="card-subtitle">支持shell、python等</span>
            </div>
            <div className="card-body">
                <Form
                    className="stretch-left"
                    {...layout}
                    name="shell"
                    ref={this.state.ref}
                    initialValues={this.state.initialValues}
                    onFinish={this.onFinish}
                    onFinishFailed={this.onFinishFailed}
                >
                    <Form.Item
                        label="名称"
                        name="name"
                        rules={[{required: true, message: '请输入名称!'}]}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        label="描述"
                        name="description"
                        rules={[{required: false, message: '请输入描述!'}]}
                    >
                        <Input.TextArea rows={4} />
                    </Form.Item>

                    <Form.Item
                        label="脚本类型"
                        name="type"
                        rules={[{required: true, message: '请选择类型!'}]}
                    >
                        <Select defaultValue="1" style={{ width: 120 }} onChange={this.onChangeScriptType}>
                            {this.renderScriptTypeList()}
                        </Select>
                    </Form.Item>

                    <Form.Item name="content" label="脚本内容">
                        <div className="editor-container" >
                            <MonacoEditor
                                theme="vs-dark"
                                language={this.state.language}
                                value={this.state.initialValues.content}
                                options={options}
                                onChange={this.onChangeHandle}
                                editorDidMount={this.editorDidMountHandle}
                            />
                        </div>
                    </Form.Item>

                    <Form.Item {...tailLayout}>
                        <div className="fixed-bottom">
                            <Button type="primary" htmlType="submit" loading={this.state.saving} style={{left: '70%'}}>
                                保存
                            </Button>
                        </div>
                    </Form.Item>
                </Form>
            </div>
        </div>
    }
}

export default withRouter(ShellEdit);
