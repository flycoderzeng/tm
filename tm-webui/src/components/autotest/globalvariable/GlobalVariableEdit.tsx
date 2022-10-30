import React from "react";
import {FormInstance} from "antd/lib/form";
import axios from "axios";
import {Form, Input, Button, Tooltip, message, Radio} from 'antd';
import { withRouter } from "react-router-dom";
import { RouteComponentProps } from "react-router-dom";
import { ArrowLeftOutlined } from '@ant-design/icons';
import {ApiUrlConfig} from "../../../config/api.url";

interface IProps {}

interface GlobalVariableModel {
    name: string;
    description: string;
    modifyFlag: number;
}

type GlobalVariableProps = IProps & RouteComponentProps;
interface IState {
    id: number;
    ref: any;
    saving: boolean;
    initialValues: GlobalVariableModel;
    setRenderRightFlag: any;
    load: any;
}

const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
};
const tailLayout = {
    wrapperCol: { offset: 8, span: 16 },
};

class GlobalVariableEdit extends React.Component<GlobalVariableProps, IState> {
    constructor(props) {
        super(props);
        const ref = React.createRef<FormInstance>();
        this.state = {
            id: props.id,
            load: this.load,
            setRenderRightFlag: props.setRenderRightFlag,
            ref: ref,
            saving: false,
            initialValues: {
                name: '',
                description: '',
                modifyFlag: 1,
            }
        }
    }

    load = (id: any) => {
        axios.post(ApiUrlConfig.LOAD_GLOBAL_VARIABLE_URL, {id: id}).then(resp => {
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
                        modifyFlag: ret.data.modifyFlag,
                    });
                }
            }
        });
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

    onFinish = values => {
        if(this.state.id > 0) {
            values['id'] = this.state.id;
        }
        this.setState({saving: true});
        axios.post(ApiUrlConfig.SAVE_GLOBAL_VARIABLE_URL, values).then(resp => {
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
        this.load(this.state.id);
    }

    render() {
        return <div className="card stretch-left">
            <div className="card-header card-header-divider">
                编辑全局变量
                <Tooltip title="返回">
                    <Button onClick={() => this.back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
                </Tooltip>
                <span className="card-subtitle">所有用例可以使用</span>
            </div>
            <div className="card-body">
                <Form
                    className=""
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
                        label="是否运行在用例中修改"
                        name="modifyFlag"
                        rules={[{required: true, message: '是否运行在用例中修改'}]}
                    >
                        <Radio.Group>
                            <Radio value={1}>是</Radio>
                            <Radio value={0}>否</Radio>
                        </Radio.Group>
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

export default withRouter(GlobalVariableEdit);
