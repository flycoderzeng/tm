import React from "react";
import axios from "axios";
import {Button, Form, Input, message, Tooltip} from 'antd';
import {RouteComponentProps, withRouter} from "react-router-dom";
import {FormInstance} from 'antd/lib/form';
import {ArrowLeftOutlined} from '@ant-design/icons';
import {ApiUrlConfig} from "../../../config/api.url";
import {ProjectSelect} from "../../manage/project/ProjectSelect";

interface IProps {}
type TagProps = IProps & RouteComponentProps;

interface TagModel {
    name: string;
    projectId: string|number|null|undefined;
}

interface IState {
    id: number;
    ref: any;
    saving: boolean;
    initialValues: TagModel;
}

const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
};

const tailLayout = {
    wrapperCol: { offset: 8, span: 16 },
};

class TagEdit extends React.Component<TagProps, IState> {
    constructor(props) {
        super(props);
        const ref = React.createRef<FormInstance>();
        this.state = {
            id: props.match.params.id,
            ref: ref,
            saving: false,
            initialValues: {
                name: '',
                projectId: null,
            }
        }
    }

    onFinish = values => {
        const data = {
            "data": {
                "type": "tags",
                "attributes": {
                    name: values.name,
                },
                "relationships": {
                    "project": {
                        "data": {
                            "id": values.projectId,
                            "type": "project"
                        }
                    }
                }
            }
        }
        this.setState({saving: true});
        if(this.state.id > 0) {
            data["data"]["id"] = this.state.id;
            axios.patch(ApiUrlConfig.SAVE_TAG_URL + '/' + this.state.id, data,
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
            axios.post(ApiUrlConfig.SAVE_TAG_URL, data,
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
            axios.get(ApiUrlConfig.LOAD_TAG_URL + this.state.id).then(resp => {
                if (resp.status !== 200) {
                    message.error('加载失败');
                } else {
                    const ret = resp.data;
                    if (ret.data) {
                        this.state.ref.current.setFieldsValue({
                            name: ret.data.attributes.name,
                            projectId: !ret.data.attributes.projectId ? null : ret.data.attributes.projectId + ''
                        });
                    }
                }
            });
        }
    }

    back() {
        this.props.history.push('/tags');
    }

    render() {
        return <div className="card">
            <div className="card-header card-header-divider">
                用例标签编辑
                <Tooltip title="返回">
                    <Button onClick={() => this.back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
                </Tooltip>
                <span className="card-subtitle">管理员可配置</span>
            </div>
            <div className="card-body">
                <Form
                    {...layout}
                    name="tag"
                    ref={this.state.ref}
                    initialValues={this.state.initialValues}
                    onFinish={this.onFinish}
                    onFinishFailed={this.onFinishFailed}
                >
                    <Form.Item
                        label="名称"
                        name="name"
                        rules={[{required: true, message: '请输入标签名称!'}]}
                    >
                        <Input style={{width: '300px'}}/>
                    </Form.Item>

                    <Form.Item
                        label="所属项目"
                        name="projectId"
                        rules={[{required: true, message: '请选择项目!'}]}
                    >
                        <ProjectSelect style={{width: '300px'}} value={this.state.initialValues.projectId}></ProjectSelect>
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

export default withRouter(TagEdit);
