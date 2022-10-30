import React from "react";
import axios from "axios";
import {Form, Input, Button, Select, Tooltip, message} from 'antd';
import { withRouter } from "react-router-dom";
import { RouteComponentProps } from "react-router-dom";
import { FormInstance } from 'antd/lib/form';
import { ArrowLeftOutlined, ArrowRightOutlined } from '@ant-design/icons';
import {ApiUrlConfig} from "../../../config/api.url";
interface IProps {}
const { Option } = Select;
type RoleProps = IProps & RouteComponentProps;

interface RoleModel {
    name: string;
    description: string;
    chineseName: string;
    type: string;
}

interface IState {
    id: number;
    ref: any;
    saving: boolean;
    initialValues: RoleModel;
}
const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
};
const tailLayout = {
    wrapperCol: { offset: 8, span: 16 },
};

class RoleEdit extends React.Component<RoleProps, IState> {
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
                chineseName: '',
                type: "1",
            }
        }
    }

    onFinish = values => {
        const data = {
            "data": {
                "type": "role",
                "attributes": {
                    name: values.menuName,
                    description: values.description,
                    chineseName: values.chineseName,
                    type: values.type
                }
            }
        }
        this.setState({saving: true});
        if(this.state.id > 0) {
            data["data"]["id"] = this.state.id;
            axios.patch(ApiUrlConfig.SAVE_ROLE_URL + '/' + this.state.id, data,
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
            axios.post(ApiUrlConfig.SAVE_ROLE_URL, data,
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
            axios.get(ApiUrlConfig.LOAD_ROLE_URL + this.state.id).then(resp => {
                if (resp.status !== 200) {
                    message.error('加载失败');
                } else {
                    const ret = resp.data;
                    if (!ret.data) {
                        return;
                    }
                    this.state.ref.current.setFieldsValue({
                        name: ret.data.attributes.name,
                        description: ret.data.attributes.description,
                        chineseName: ret.data.attributes.chineseName,
                        type: ret.data.attributes.type.toString()
                    });
                }
            });
        }
    }

    back() {
        this.props.history.push('/rolelist');
    }

    goRoleRightSetting() {
        this.props.history.push('/rolerightsetting/' + this.state.id)
    }

    showRoleRightSettingBtn() {
        if(this.state.id > 0) {
            return (<Tooltip title="权限关联配置">
                <Button style={{float: 'right'}} onClick={() => this.goRoleRightSetting()} type="primary" size="small" shape="circle" icon={<ArrowRightOutlined />} />
            </Tooltip>);
        }
    }

    render() {
        return <div className="card">
            <div className="card-header card-header-divider">
                编辑角色
                <Tooltip title="返回">
                    <Button onClick={() => this.back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
                </Tooltip>

                {
                    this.showRoleRightSettingBtn()
                }

                <span className="card-subtitle">管理员可配置</span>
            </div>
            <div className="card-body">
                <Form
                    {...layout}
                    name="role"
                    ref={this.state.ref}
                    initialValues={this.state.initialValues}
                    onFinish={this.onFinish}
                    onFinishFailed={this.onFinishFailed}
                >
                    <Form.Item
                        label="角色名称"
                        name="name"
                        rules={[{required: true, message: '请输入角色名称!'}]}
                    >
                        <Input placeholder="如：ROLE_PROJECT_TESTER"/>
                    </Form.Item>

                    <Form.Item
                        label="中文名称"
                        name="chineseName"
                        rules={[{required: true, message: '请输入角色中文名称!'}]}
                    >
                        <Input placeholder="如：测试"/>
                    </Form.Item>

                    <Form.Item
                        label="描述"
                        name="description"
                        rules={[{required: true, message: '请输入角色描述!'}]}
                    >
                        <Input placeholder="如：拥有创建、修改、运行用例等权限"/>
                    </Form.Item>

                    <Form.Item
                        name="type"
                        label="角色类型"
                        rules={[{ required: true, message: '请选择角色类型!' }]}
                    >
                        <Select placeholder="请选择角色类型">
                            <Option value="1">系统角色</Option>
                            <Option value="2">项目角色</Option>
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

export default withRouter(RoleEdit);
