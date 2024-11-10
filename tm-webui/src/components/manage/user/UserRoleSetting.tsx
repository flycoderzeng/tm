import React from 'react';
import {RouteComponentProps, withRouter} from "react-router-dom";
import axios from "axios";
import {Button, message, Select, Table, Tooltip} from "antd";
import {ArrowLeftOutlined} from "@ant-design/icons";
import moment from "moment";
import {ApiUrlConfig} from "../../../config/api.url";

interface IProps {}
type CurrProps = IProps & RouteComponentProps;
const { Option } = Select;
interface UserRoleModel {
    id: number;
    roleName: string;
    roleDescription: string;
    addUser: string;
    addTime: string;
}

interface IState {
    id: number;
    username: string;
    chineseName: string;
    addUser: string;
    addTime: string;
    loading: boolean;
    data: UserRoleModel[];
    roleIdList: string[];
    optionList: any[];
}
class UserRoleSetting extends React.Component<CurrProps, IState> {
    constructor(props) {
        super(props);
        this.state = {
            id: props.match.params.id,
            username: '',
            chineseName: '',
            addUser: '',
            addTime: '',
            loading: false,
            roleIdList: [],
            data: [],
            optionList: []
        }
    }
    componentDidMount() {
        this.loadUserInfo();
        this.loadUserRoleInfo();
    }

    loadUserRoleInfo() {
        if (this.state.id > 0) {
            axios.post(ApiUrlConfig.QUERY_USER_ROLE_LIST_URL, {id: this.state.id}).then(resp => {
                if (resp.status !== 200) {
                    message.error('加载角色权限失败');
                } else {
                    const ret = resp.data;
                    if (ret.code !== 0) {
                        message.error(ret.message);
                    } else {
                        if (!ret.data) {
                            return;
                        }
                        const data = ret.data || [];
                        data.map(function(v) {
                            v.addTime = moment(new Date(v.addTime)).format('YYYY-MM-DD HH:mm:ss');
                            return true;
                        });
                        this.setState({
                            data: data
                        });
                    }
                }
            });
        }
    }

    loadUserInfo() {
        if (this.state.id > 0) {
            axios.get(ApiUrlConfig.LOAD_USER_URL + this.state.id).then(resp => {
                if (resp.status !== 200) {
                    message.error('加载用户失败');
                } else {
                    const ret = resp.data;
                    if (!ret.data) {
                        return;
                    }
                    this.setState({
                        username: ret.data.attributes.username,
                        chineseName: ret.data.attributes.chineseName,
                        addUser: ret.data.attributes.addUser,
                        addTime: ret.data.attributes.addTime
                    });
                    this.getRoleList();
                }
            });
        }
    }

    getRoleList() {
        const children :any[] = [];
        axios.get(ApiUrlConfig.QUERY_ROLE_LIST_URL + '?page%5Btotals%5D&page%5Bnumber%5D=1&filter%5Brole%5D=status%3D%3D0;type%3D%3D01&sort=id&page%5Bsize%5D=100').then(resp => {
            if (resp.status !== 200) {
                message.error('加载列表失败');
            } else {
                const ret = resp.data;
                ret.data && ret.data.map(function(v) {
                    children.push({
                        text: v.attributes.name + ': ' + v.attributes.chineseName+"("+v.attributes.description+")",
                        value: v.id
                    });
                    return true;
                });
                this.setState({
                    optionList: children
                });
            }
        });
    }
    back() {
        this.props.history.push('/userlist');
    }
    handleChange = value  => {
        this.setState({
            roleIdList: value
        });
    }
    getOptions() {
        const options = this.state.optionList.map(function(d) {
            return <Option key={d.value} value={d.value + '-' + d.text}>{d.text}</Option>
        });
        return options;
    }

    delete = id => {
        if (!window.confirm("确定删除吗？")) {
            return;
        }
        axios.post(ApiUrlConfig.DELETE_USER_ROLE_URL, {id: id}).then(resp => {
            if (resp.status !== 200) {
                message.error('删除失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    this.loadUserRoleInfo();
                }
            }
        });
    }

    add() {
        if(this.state.roleIdList.length < 1) {
            return;
        }
        const roleIdList = this.state.roleIdList.map(v => {
            return v.split('-')[0];
        });
        const data = {userId: this.state.id, roleIdList: roleIdList};
        this.setState({loading: true});
        axios.post(ApiUrlConfig.ADD_USER_ROLE_URL, data).then(resp => {
            if (resp.status !== 200) {
                message.error('操作失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    this.loadUserRoleInfo();
                    this.setState({
                        roleIdList: []
                    });
                }
            }
        }).finally(() => {
            this.setState({loading: false});
        });
    }
    render() {
        const columns: any[] = [
            {
                title: '角色id',
                dataIndex: 'roleId',
                key: 'roleId',
            },
            {
                title: '角色名称',
                dataIndex: 'roleName',
                key: 'roleName',
            },
            {
                title: '角色描述',
                dataIndex: 'roleDescription',
                key: 'roleDescription',
            },
            {
                title: '添加者',
                dataIndex: 'addUser',
                render: text => <span>{text}</span>,
            },{
                title: '添加时间',
                dataIndex: 'addTime',
                render: text => <span>{text}</span>,
            }, {
                title: '操作',
                fixed: 'right',
                render: (text, record) => (
                    <div>
                        <Tooltip title="删除">
                            <Button danger size="small" type="primary" shape="circle"
                                    onClick={() => this.delete(record.id)}>D</Button>
                        </Tooltip>
                    </div>
                )
            }
        ];
        return (<div className="card">
            <div className="card-header card-header-divider">
                用户角色关联配置
                <Tooltip title="返回">
                    <Button onClick={() => this.back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
                </Tooltip>
                <span className="card-subtitle">管理员可以配置</span>
            </div>
            <div className="card-body">
                <div>
                    <h3><b>1. 用户信息</b><br/></h3>
                    <pre>英文名：{this.state.username}
                    <br/>中文名：{this.state.chineseName}
                        <br/>创建者：{this.state.addUser}
                        <br/>创建时间：{this.state.addTime}
                    </pre>
                    <h3><b>2. 角色信息</b><br/></h3>
                    <div style={{display: 'flex'}}>
                        <Select
                            mode="multiple"
                            allowClear
                            showSearch
                            notFoundContent={null}
                            value={this.state.roleIdList}
                            style={{ width: '70%', marginRight: '5px' }}
                            placeholder="搜索选择角色"
                            onChange={this.handleChange}>
                            {this.getOptions()}
                        </Select>
                        <Button onClick={() => this.add()} type="primary" loading={this.state.loading}>添加</Button>
                    </div>
                    <div>
                        <Table
                            size="small"
                            footer={() => '共' + this.state.data.length + '条数据'}
                            dataSource={this.state.data}
                            columns={columns} />
                    </div>
                </div>
            </div>
        </div>)
    }
}

export default withRouter(UserRoleSetting)
