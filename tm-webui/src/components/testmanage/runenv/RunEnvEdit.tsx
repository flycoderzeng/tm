import React from "react";
import axios from "axios";
import {Button, Form, Input, message, Select, Tooltip} from 'antd';
import {RouteComponentProps, withRouter} from "react-router-dom";
import {FormInstance} from 'antd/lib/form';
import {ArrowLeftOutlined} from '@ant-design/icons';
import {ApiUrlConfig} from "../../../config/api.url";

interface IProps {}
type RunEnvProps = IProps & RouteComponentProps;

interface RunEnvModel {
    name: string|null;
    description: string|null;
    httpIp: string|null;
    httpPort: string|null;
    dbUsername: string|null;
    dbPassword: string|null;
    dbIp: string|null;
    dbPort: string|null;
    dbSchemaName: string|null;
    dbType: string|null|number;
}

interface IState {
    id: number;
    ref: any;
    saving: boolean;
    initialValues: RunEnvModel;
}

const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
};

const tailLayout = {
    wrapperCol: { offset: 8, span: 16 },
};

class RunEnvEdit extends React.Component<RunEnvProps, IState> {
    constructor(props) {
        super(props);
        const ref = React.createRef<FormInstance>();
        this.state = {
            id: props.match.params.id,
            ref: ref,
            saving: false,
            initialValues: {
                name: '',
                description: '',
                httpIp: '',
                httpPort: '',
                dbUsername: '',
                dbPassword: '',
                dbIp: '',
                dbPort: '',
                dbSchemaName: '',
                dbType: null,
            }
        }
    }

    onFinish = values => {
        const data = {
            "data": {
                "type": "run_env",
                "attributes": {
                    name: values.name,
                    description: values.description,
                    httpIp: values.httpIp,
                    httpPort: values.httpPort,
                    dbUsername: values.dbUsername,
                    dbPassword: values.dbPassword,
                    dbIp: values.dbIp,
                    dbPort: values.dbPort,
                    dbSchemaName: values.dbSchemaName,
                    dbType: values.dbType
                }
            }
        }
        this.setState({saving: true});
        if(this.state.id > 0) {
            data["data"]["id"] = this.state.id;
            axios.patch(ApiUrlConfig.SAVE_RUN_ENV_URL + '/' + this.state.id, data,
                {headers: {"Content-Type": "application/vnd.api+json"}}).then(resp => {
                if (resp.status !== 204) {
                    message.error('操作失败');
                } else {
                    message.success('操作成功');
                    this.back();
                }
            }).finally(() => {
                this.setState({saving: false});
            });
        }else{
            axios.post(ApiUrlConfig.SAVE_RUN_ENV_URL, data,
                {headers: {"Content-Type": "application/vnd.api+json"}}).then(resp => {
                if (resp.status !== 201) {
                    message.error('操作失败');
                } else {
                    const ret = resp.data;
                    this.setState({id: ret.data.id});
                    message.success('操作成功');
                    this.back();
                }
            }).finally(() => {
                this.setState({saving: false});
            });
        }
    }

    onFinishFailed = errorInfo => {
        console.log('Failed:', errorInfo);
    };

    componentDidMount() {
        if(this.state.id > 0) {
            axios.get(ApiUrlConfig.LOAD_RUN_ENV_URL + this.state.id).then(resp => {
                if (resp.status !== 200) {
                    message.error('加载失败');
                } else {
                    const ret = resp.data;
                    if (ret.data) {
                        this.state.ref.current.setFieldsValue({
                            name: ret.data.attributes.name,
                            description: ret.data.attributes.description,
                            httpIp: ret.data.attributes.httpIp,
                            httpPort: ret.data.attributes.httpPort,
                            dbUsername: ret.data.attributes.dbUsername,
                            dbPassword: ret.data.attributes.dbPassword,
                            dbIp: ret.data.attributes.dbIp,
                            dbPort: ret.data.attributes.dbPort,
                            dbSchemaName: ret.data.attributes.dbSchemaName,
                            dbType: (ret.data.attributes.dbType && ret.data.attributes.dbType+'') || null,
                        });
                    }
                }
            });
        }
    }

    back() {
        this.props.history.push('/runenvlist');
    }

    render() {
        // @ts-ignore
        return <div className="card">
            <div className="card-header card-header-divider">
                运行环境编辑
                <Tooltip title="返回">
                    <Button onClick={() => this.back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
                </Tooltip>
                <span className="card-subtitle">管理员可配置</span>
            </div>
            <div className="card-body">
                <Form
                    {...layout}
                    name="runenv"
                    ref={this.state.ref}
                    initialValues={this.state.initialValues}
                    onFinish={this.onFinish}
                    onFinishFailed={this.onFinishFailed}
                >
                    <Form.Item
                        label="名称"
                        name="name"
                        rules={[{required: true, message: '请输入环境名称!'}]}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        label="描述"
                        name="description"
                        rules={[{required: true, message: '请输入环境描述!'}]}
                    >
                        <Input.TextArea rows={4}/>
                    </Form.Item>

                    <Form.Item
                        label="http服务IP"
                        name="httpIp"
                        rules={[{required: false, message: '请输入ip!'}]}
                    >
                        <Input style={{width: '200px'}} placeholder="如：192.168.1.121"/>
                    </Form.Item>

                    <Form.Item
                        label="http服务端口"
                        name="httpPort"
                        rules={[{required: false, message: '请输入端口!'}]}
                    >
                        <Input style={{width: '150px'}} placeholder="如：8080"/>
                    </Form.Item>

                    <Form.Item
                        label="数据库用户名"
                        name="dbUsername"
                        rules={[{required: false, message: '请输入数据库用户名!'}]}
                    >
                        <Input style={{width: '300px'}} />
                    </Form.Item>

                    <Form.Item
                        label="数据库密码"
                        name="dbPassword"
                        rules={[{required: false, message: '请输入数据库密码!'}]}
                    >
                        <Input style={{width: '300px'}} />
                    </Form.Item>

                    <Form.Item
                        label="数据库IP"
                        name="dbIp"
                        rules={[{required: false, message: '请输入ip!'}]}
                    >
                        <Input style={{width: '200px'}} placeholder="如：192.168.1.121"/>
                    </Form.Item>

                    <Form.Item
                        label="数据库端口"
                        name="dbPort"
                        rules={[{required: false, message: '请输入端口!'}]}
                    >
                        <Input style={{width: '150px'}} placeholder="如：3306"/>
                    </Form.Item>

                    <Form.Item
                        label="数据库所属schema"
                        name="dbSchemaName"
                        rules={[{required: false, message: '请输入数据库所属schema!'}]}
                    >
                        <Input style={{width: '200px'}} />
                    </Form.Item>

                    <Form.Item
                        label="数据库类型"
                        name="dbType"
                        rules={[{required: false, message: '请选择数据库类型!'}]}
                    >
                        <Select style={{ width: 120 }} options={[{label: 'MySql', value: '1'}, {label: 'Postgresql', value: '2'}, {label: '达梦', value: '3'}]}>
                        </Select>
                    </Form.Item>

                    <Form.Item {...tailLayout}>
                        <Button type="primary" htmlType="submit" loading={this.state.saving}>
                            保存
                        </Button>
                    </Form.Item>
                </Form>
            </div>
        </div>
    }
}

export default withRouter(RunEnvEdit);
