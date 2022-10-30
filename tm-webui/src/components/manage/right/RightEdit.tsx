import React from "react";
import axios from "axios";
import {Form, Input, Button, Select, Tooltip, message} from 'antd';
import { withRouter } from "react-router-dom";
import { RouteComponentProps } from "react-router-dom";
import { FormInstance } from 'antd/lib/form';
import { ArrowLeftOutlined } from '@ant-design/icons';
import {ApiUrlConfig} from "../../../config/api.url";
interface IProps {}
const { Option } = Select;
type RightProps = IProps & RouteComponentProps;

interface RightModel {
    name: string;
    uri: string;
    type: string;
}

interface IState {
    id: number;
    ref: any;
    saving: boolean;
    initialValues: RightModel;
}
const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
};
const tailLayout = {
    wrapperCol: { offset: 8, span: 16 },
};

class RightEdit extends React.Component<RightProps, IState> {
    constructor(props) {
        super(props);
        const ref = React.createRef<FormInstance>();
        this.state = {
            id: props.match.params.id,
            ref: ref,
            saving: false,
            initialValues: {
                name: '',
                uri: '',
                type: "1",
            }
        }
    }
    onFinish = values => {
        const data = {
            "data": {
                "type": "right",
                "attributes": {
                    name: values.name,
                    uri: values.uri,
                    type: values.type
                }
            }
        }
        this.setState({saving: true});
        if(this.state.id > 0) {
            data["data"]["id"] = this.state.id;
            axios.patch(ApiUrlConfig.SAVE_RIGHT_URL + '/' + this.state.id, data,
                {headers: {"Content-Type": "application/vnd.api+json"}}).then(resp => {
                if (resp.status !== 204) {
                    message.error('保存失败');
                } else {
                    message.success('操作成功');
                    this.back();
                }
            }).finally(() => {
                this.setState({saving: false});
            });
        }else{
            axios.post(ApiUrlConfig.SAVE_RIGHT_URL, data,
                {headers: {"Content-Type": "application/vnd.api+json"}}).then(resp => {
                if (resp.status !== 201) {
                    message.error('保存失败');
                } else {
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
            axios.get(ApiUrlConfig.LOAD_RIGHT_URL + this.state.id).then(resp => {
                if (resp.status !== 200) {
                    message.error('加载失败');
                } else {
                    const ret = resp.data;
                    if (!ret.data) {
                        return;
                    }
                    this.state.ref.current.setFieldsValue({
                        name: ret.data.attributes.name,
                        uri: ret.data.attributes.uri,
                        type: ret.data.attributes.type.toString()
                    });
                }
            });
        }
    }
    back() {
        this.props.history.push('/rightlist');
    }
    render() {
        return <div className="card">
            <div className="card-header card-header-divider">
                编辑权限
                <Tooltip title="返回">
                    <Button onClick={() => this.back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
                </Tooltip>
                <span className="card-subtitle">管理员可配置</span>
            </div>
            <div className="card-body">
                <Form
                    {...layout}
                    name="right"
                    ref={this.state.ref}
                    initialValues={this.state.initialValues}
                    onFinish={this.onFinish}
                    onFinishFailed={this.onFinishFailed}
                >
                    <Form.Item
                        label="权限名称"
                        name="name"
                        rules={[{required: true, message: '请输入权限名称!'}]}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        label="uri"
                        name="uri"
                        rules={[{required: true, message: '请输入权限路径!'}]}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        name="type"
                        label="权限类型"
                        rules={[{ required: true, message: '请选择权限类型!' }]}
                    >
                        <Select placeholder="请选择权限类型">
                            <Option value="1">系统权限</Option>
                            <Option value="2">项目权限</Option>
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

export default withRouter(RightEdit);
