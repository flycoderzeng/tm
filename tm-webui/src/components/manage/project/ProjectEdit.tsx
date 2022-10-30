import React from "react";
import axios from "axios";
import {Form, Input, Button, Tooltip, message} from 'antd';
import { withRouter } from "react-router-dom";
import { RouteComponentProps } from "react-router-dom";
import { FormInstance } from 'antd/lib/form';
import { ArrowLeftOutlined } from '@ant-design/icons';
import {ProjectModel} from "../../../entities/ProjectModel";
import {ApiUrlConfig} from "../../../config/api.url";
interface IProps {}

type ProjectProps = IProps & RouteComponentProps;

interface IState {
    id: number;
    ref: any;
    saving: boolean;
    initialValues: ProjectModel;
}
const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
};
const tailLayout = {
    wrapperCol: { offset: 8, span: 16 },
};

class ProjectEdit extends React.Component<ProjectProps, IState> {
    constructor(props) {
        super(props);
        const ref = React.createRef<FormInstance>();
        this.state = {
            id: props.match.params.id,
            ref: ref,
            saving: false,
            initialValues: {
                id: 0,
                name: '',
                description: '',
            }
        }
    }

    onFinish = values => {
        const data = {
            "data": {
                "type": "project",
                "attributes": {
                    name: values.name,
                    description: values.description
                }
            }
        }
        this.setState({saving: true});
        if(this.state.id > 0) {
            data["data"]["id"] = this.state.id;
            axios.patch(ApiUrlConfig.SAVE_PROJECT_URL + '/' + this.state.id, data,
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
            axios.post(ApiUrlConfig.SAVE_PROJECT_URL, data,
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
            axios.get(ApiUrlConfig.LOAD_PROJECT_URL + this.state.id).then(resp => {
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
                    });
                }
            });
        }
    }

    back() {
        this.props.history.push('/projectlist');
    }

    render() {
        return <div className="card">
            <div className="card-header card-header-divider">
                编辑项目
                <Tooltip title="返回">
                    <Button onClick={() => this.back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
                </Tooltip>
                <span className="card-subtitle">管理员可配置</span>
            </div>
            <div className="card-body">
                <Form
                    {...layout}
                    name="projectForm"
                    ref={this.state.ref}
                    initialValues={this.state.initialValues}
                    onFinish={this.onFinish}
                    onFinishFailed={this.onFinishFailed}
                >
                    <Form.Item
                        label="名称"
                        name="name"
                        rules={[{required: true, message: '请输入项目名称!'}]}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        label="描述"
                        name="description"
                        rules={[{required: true, message: '请输入项目描述!'}]}
                    >
                        <Input.TextArea />
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

export default withRouter(ProjectEdit);
