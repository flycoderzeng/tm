import React from "react";
import axios from "axios";
import {Button, Form, Input, message, Tooltip} from 'antd';
import {RouteComponentProps, withRouter} from "react-router-dom";
import {FormInstance} from 'antd/lib/form';
import {ArrowLeftOutlined} from '@ant-design/icons';
import {ApiUrlConfig} from "../../../config/api.url";

interface IProps {}

type UserProps = IProps & RouteComponentProps;

interface UserModel {
    username: string;
    chineseName: string;
}

interface IState {
    id: number;
    ref: any;
    saving: boolean;
    initialValues: UserModel;
}
const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
};
const tailLayout = {
    wrapperCol: { offset: 8, span: 16 },
};

class UserEdit extends React.Component<UserProps, IState> {
    constructor(props) {
        super(props);
        const ref = React.createRef<FormInstance>();
        this.state = {
            id: props.match.params.id,
            ref: ref,
            saving: false,
            initialValues: {
                username: '',
                chineseName: '',
            }
        }
    }
    onFinish = values => {
        const data = {
            "data": {
                "type": "user",
                "attributes": {
                    username: values.username,
                    chineseName: values.chineseName,
                }
            }
        }
        this.setState({saving: true});
        if(this.state.id > 0) {
            data["data"]["id"] = this.state.id;
            axios.patch(ApiUrlConfig.SAVE_USER_URL + '/' + this.state.id, data,
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
            axios.post(ApiUrlConfig.SAVE_USER_URL, data,
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
            axios.get(ApiUrlConfig.LOAD_USER_URL + this.state.id).then(resp => {
                if (resp.status !== 200) {
                    message.error('加载用户失败');
                } else {
                    const ret = resp.data;
                    if (!ret.data) {
                        return;
                    }
                    this.state.ref.current.setFieldsValue({
                        username: ret.data.attributes.username,
                        chineseName: ret.data.attributes.chineseName,
                    });
                }
            });
        }
    }
    back() {
        this.props.history.push('/userlist');
    }
    render() {
        return <div className="card">
            <div className="card-header card-header-divider">
                编辑用户
                <Tooltip title="返回">
                    <Button onClick={() => this.back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
                </Tooltip>
                <span className="card-subtitle">管理员可配置</span>
            </div>
            <div className="card-body">
                <Form
                    {...layout}
                    name="user"
                    ref={this.state.ref}
                    initialValues={this.state.initialValues}
                    onFinish={this.onFinish}
                    onFinishFailed={this.onFinishFailed}
                >
                    <Form.Item
                        label="英文名"
                        name="username"
                        rules={[{required: true, message: '请输入英文名!'}]}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        label="中文名"
                        name="chineseName"
                        rules={[{required: true, message: '请输入中文名!'}]}
                    >
                        <Input/>
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

export default withRouter(UserEdit);
