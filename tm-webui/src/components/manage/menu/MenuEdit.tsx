import React from "react";
import axios from "axios";
import {Form, Input, Button, InputNumber, TreeSelect, Tooltip, message} from 'antd';
import { withRouter } from "react-router-dom";
import { RouteComponentProps } from "react-router-dom";
import { FormInstance } from 'antd/lib/form';
import { ArrowLeftOutlined } from '@ant-design/icons';
import {ApiUrlConfig} from "../../../config/api.url";
interface IProps {}

type MenuProps = IProps & RouteComponentProps;


interface MenuTreeNode {
    title: string;
    value: string;
    children ?: MenuTreeNode[];
}

interface MenuModel {
    menuName: string;
    url: string;
    parentId: string;
    seq: string;
}

interface IState {
    menuList: MenuTreeNode[];
    id: number;
    ref: any;
    saving: boolean;
    initialValues: MenuModel;
}
const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
};
const tailLayout = {
    wrapperCol: { offset: 8, span: 16 },
};

class MenuEdit extends React.Component<MenuProps, IState> {
    constructor(props) {
        super(props);
        const ref = React.createRef<FormInstance>();
        this.state = {
            menuList: [],
            id: props.match.params.id,
            ref: ref,
            saving: false,
            initialValues: {
                menuName: '',
                url: '',
                seq: '',
                parentId: '',
            }
        }
    }

    onFinish = values => {
        if(this.state.id > 0) {
            values['id'] = this.state.id;
        }
        this.setState({saving: true});
        axios.post(ApiUrlConfig.SAVE_MENU_URL, values).then(resp => {
            if (resp.status !== 200) {
                message.error('保存失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    this.back();
                }
            }
        }).finally(() => {
            this.setState({saving: false});
        });
    }

    onFinishFailed = errorInfo => {

    };

    componentDidMount() {
        axios.get(ApiUrlConfig.GET_ALL_MENU_TREE_URL).then(response => {
            if (response.status !== 200) {
                message.error('加载菜单列表失败');
            } else {
                const ret = response.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    if(!ret.data) {
                        return;
                    }
                    const menuList :MenuTreeNode[] = [];
                    for(let i=0; i<ret.data.length; i++) {
                        const row = {title: ret.data[i]['menuName'], value: ret.data[i]['id']};
                        menuList.push(row);
                        const children1 = ret.data[i].children;
                        if(children1 && children1.length > 0) {
                            row['children'] = [];
                            for(let j=0; j<children1.length; j++) {
                                const row2 = {title: children1[j]['menuName'], value: children1[j]['id']};
                                row['children'].push(row2);
                            }
                        }
                    }
                    this.setState({
                        menuList: menuList
                    });
                }
            }
        });
        if(this.state.id > 0) {
            axios.post(ApiUrlConfig.LOAD_MENU_URL, {id: this.state.id}).then(resp => {
                if (resp.status !== 200) {
                    message.error('加载菜单失败');
                } else {
                    const ret = resp.data;
                    if (ret.code !== 0) {
                        message.error(ret.message);
                    } else {
                        if (!ret.data) {
                            return;
                        }
                        this.state.ref.current.setFieldsValue({
                            menuName: ret.data.menuName,
                            url: ret.data.url,
                            seq: ret.data.seq,
                            parentId: ret.data.parentId === 0 ? '' : ret.data.parentId
                        });
                    }
                }
            });
        }
    }
    back() {
        this.props.history.push('/menulist');
    }
    render() {
        return <div className="card">
            <div className="card-header card-header-divider">
                编辑菜单
                <Tooltip title="返回">
                <Button onClick={() => this.back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
            </Tooltip>
                <span className="card-subtitle">最多配置三级菜单</span>
            </div>
            <div className="card-body">
                <Form
                    {...layout}
                    name="menu"
                    ref={this.state.ref}
                    initialValues={this.state.initialValues}
                    onFinish={this.onFinish}
                    onFinishFailed={this.onFinishFailed}
                >
                    <Form.Item
                        label="菜单名称"
                        name="menuName"
                        rules={[{required: true, message: '请输入菜单名称!'}]}
                    >
                        <Input style={{width: 300}}/>
                    </Form.Item>

                    <Form.Item
                        label="路由"
                        name="url"
                        rules={[{required: false, message: '请输入路由!'}]}
                    >
                        <Input style={{width: 300}}/>
                    </Form.Item>

                    <Form.Item
                        label="顺序"
                        name="seq"
                        rules={[{required: true, message: '请输入顺序值!'}]}
                    >
                        <InputNumber style={{width: 200}} />
                    </Form.Item>

                    <Form.Item label="父级菜单" name="parentId">
                        <TreeSelect
                            style={{width: 300}}
                            treeDefaultExpandAll
                            allowClear={true}
                            treeData={this.state.menuList}
                        />
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

export default withRouter(MenuEdit);
