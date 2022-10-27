import React from 'react';
import { RouteComponentProps,withRouter } from "react-router-dom";
import axios from "axios";
import {Button, message, Table, Tooltip, Select, Spin, Tag} from "antd";
import {ArrowLeftOutlined} from "@ant-design/icons";
import moment from "moment";
import {ProjectUserRoleModel} from "../../../entities/ProjectUserRoleModel";
import {ApiUrlConfig} from "../../../config/api.url";
interface IProps {}
type CurrProps = IProps & RouteComponentProps;
const { Option } = Select;

interface ProjectUserModel {
    id: number;
    roleList: ProjectUserRoleModel[];
    username: string;
    chineseName: string;
    addTime: string;
    addUser: string;
}

interface IState {
    id: number;
    name: string;
    description: string;
    addUser: string;
    addTime: string;
    loading: boolean;
    loadingProjectUser: boolean;
    fetching: boolean;
    userIdList: string[];
    roleIdList: string[];
    userOptionList: any[];
    roleOptionList: any[];
    data: ProjectUserModel[];
    pagination: any;
}
class ProjectUserSetting extends React.Component<CurrProps, IState> {
    constructor(props) {
        super(props);
        this.state = {
            id: props.match.params.id,
            name: '',
            description: '',
            addUser: '',
            addTime: '',
            loading: false,
            fetching: false,
            userIdList: [],
            roleIdList: [],
            userOptionList: [],
            roleOptionList: [],
            data: [],
            loadingProjectUser: false,
            pagination: {
                current: 1,
                pageNum: 1,
                pageSize: 10,
                linkOperator: 'or',
                total: 0,
                filterConditionList: [{"columnName": "username", "value": ''},
                    {"columnName": "chinese_name", "value": ''}]
            },
        }
    }
    getRoleList() {
        const children :any[] = [];
        axios.post(ApiUrlConfig.QUERY_ROLE_LIST_URL,
            {
                pageNum: 1,
                pageSize: 1000,
                filterConditionList: [{columnName: 'type', operator: '=', value: 2}]
            }).then(resp => {
            if (resp.status !== 200) {
                message.error('加载列表失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    ret.data.rows && ret.data.rows.map(function(v) {
                        children.push({
                            text: v.chineseName+"("+v.description+")",
                            value: v.id.toString()
                        });
                        return true;
                    });
                    this.setState({
                        roleOptionList: children
                    });
                }
            }
        });
    }

    loadProjectInfo() {
        if(this.state.id > 0) {
            axios.post(ApiUrlConfig.LOAD_PROJECT_URL, {id: this.state.id}).then(resp => {
                if (resp.status !== 200) {
                    message.error('加载失败');
                } else {
                    const ret = resp.data;
                    if (ret.code !== 0) {
                        message.error(ret.message);
                    } else {
                        if (!ret.data) {
                            return;
                        }
                        this.setState({
                            name: ret.data.name,
                            description: ret.data.description,
                            addTime: moment(new Date(ret.data.addTime)).format('YYYY-MM-DD HH:mm:ss'),
                            addUser: ret.data.addUser
                        });
                    }
                }
            });
        }
    }
    componentDidMount() {
        this.loadProjectInfo();
        this.getRoleList();
        this.loadProjectUserInfo();
    }
    handleChangeUser = value  => {
        this.setState({
            userIdList: value
        });
    }
    handleSearchUser = value => {
        if(!value || value.length < 1) {
            return;
        }
        const filterConditionList =
            [{"columnName": "username", "value":value},
                {"columnName": "chinese_name", "value":value}];
        const data = {
            pageNum: 1,
            pageSize: 30,
            linkOperator: 'or',
            filterConditionList: filterConditionList
        };
        this.setState({
            fetching: true,
        });
        const children :any[] = [];
        axios.post(ApiUrlConfig.QUERY_USER_LIST_URL, data).then(resp => {
            if (resp.status !== 200) {
                message.error('搜索失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    if (!ret.data) {
                        return;
                    }
                    ret.data.rows && ret.data.rows.map(function(v) {
                        children.push({
                            text: v.username+"("+v.chineseName+")",
                            value: v.id.toString()
                        });
                        return true;
                    });
                    this.setState({
                        userOptionList: children
                    });
                }
            }
        }).finally(() => {
            this.setState({
                fetching: false
            });
        });
    }
    handleChangeRole = value  => {
        this.setState({
            roleIdList: value
        });
    }
    getUserOptions() {
        const options = this.state.userOptionList.map(function(d) {
            return <Option key={d.value} value={d.value + '-' + d.text}>{d.text}</Option>
        });
        return options;
    }
    getRoleOptions() {
        const options = this.state.roleOptionList.map(function(d) {
            return <Option key={d.value} value={d.value + '-' + d.text}>{d.text}</Option>
        });
        return options;
    }
    loadProjectUserInfo() {
        this.loadDataList(this.state.pagination);
    }
    add() {
        if(this.state.userIdList.length < 1) {
            message.info('请选择用户');
            return;
        }
        if(this.state.roleIdList.length < 1) {
            message.info('请选择角色');
            return;
        }
        this.setState({
            loading: true
        });
        const userIdList = this.state.userIdList.map(v => {
            return v.split('-')[0];
        });
        const roleIdList = this.state.roleIdList.map(v => {
            return v.split('-')[0];
        });

        axios.post(ApiUrlConfig.ADD_PROJECT_USER_URL, {
            projectId: this.state.id,
            userIdList: userIdList,
            roleIdList: roleIdList
        }).then(resp => {
            if (resp.status !== 200) {
                message.error('搜索失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    this.loadProjectUserInfo();
                    this.setState({
                        userIdList: [],
                        roleIdList: [],
                    });
                }
            }
        }).finally(() => {
            this.setState({
                loading: false
            });
        })
    }
    back() {
        this.props.history.push('/projectlist');
    }
    delete = id => {
        if (!window.confirm("确定删除吗？")) {
            return;
        }
        axios.post(ApiUrlConfig.DELETE_PROJECT_USER_URL, {id: id}).then(resp => {
            if (resp.status !== 200) {
                message.error('删除失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    this.loadProjectUserInfo();
                }
            }
        });
    }
    onChange = (pagination) => {
        this.loadDataList(pagination);
    }
    loadDataList = (pagination, reset ?: boolean) => {
        if (!pagination) {
            pagination = this.state.pagination;
        }
        if (reset) {
            pagination.current = 1;
        }
        const data = {
            projectId: this.state.id,
            pageNum: pagination.current,
            pageSize: pagination.pageSize,
            current: pagination.current,
            linkOperator: pagination.linkOperator || this.state.pagination.linkOperator,
            filterConditionList: pagination.filterConditionList || this.state.pagination.filterConditionList
        };

        this.setState({
            loadingProjectUser: true, pagination: {
                ...this.state.pagination,
                pageNum: pagination.current,
                pageSize: pagination.pageSize,
                current: pagination.current,
            }
        });
        axios.post(ApiUrlConfig.QUERY_PROJECT_USER_LIST_URL, data).then(resp => {
            if (resp.status !== 200) {
                message.error('加载项目用户失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    ret.data.rows.map(v => {
                        v.addTime = moment(new Date(v.addTime)).format('YYYY-MM-DD HH:mm:ss');
                        return null;
                    });
                    this.setState({
                        data: ret.data.rows,
                        pagination: {
                            ...this.state.pagination,
                            total: ret.data.total
                        }
                    });
                }
            }
        }).finally(() => {
            this.setState({loadingProjectUser: false});
        });
    }

    deleteUserRole(e: any, userId: number, roleId: number) {
        if(!window.confirm("确定删除吗？")) {
            e.preventDefault();
            return;
        }
        axios.post(ApiUrlConfig.DELETE_PROJECT_USER_ROLE_URL,
            {projectId: this.state.id, userId: userId, roleId: roleId}).then(resp => {
            if (resp.status !== 200) {
                message.error('删除失败');
                e.preventDefault();
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                    e.preventDefault();
                } else {
                    message.success('操作成功');
                    this.loadProjectUserInfo();
                }
            }
        });
    }

    renderRoleList(text: ProjectUserRoleModel[], record) {
        let r : any[] = [];
        if(text) {
            r = text.map(v => {
                return (<Tag closable color="geekblue" onClose={e => {this.deleteUserRole(e, v.userId, v.roleId);}}>
                    {v.roleChineseName}
                </Tag>)
            });
        }
        return r;
    }
    render() {
        const columns : any[] = [
            {
                title: '用户名称',
                dataIndex: 'username',
            },
            {
                title: '中文名',
                dataIndex: 'chineseName',
            },
            {
                title: '角色',
                dataIndex: 'roleList',
                render: (text: any[], record) => this.renderRoleList(text, record),
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
            }];
        return (<div className="card">
            <div className="card-header card-header-divider">
                项目用户管理
                <Tooltip title="返回">
                    <Button onClick={() => this.back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
                </Tooltip>
                <span className="card-subtitle">管理员可以配置</span>
            </div>
            <div className="card-body">
                <div>
                    <h3><b>1. 项目信息</b><br/></h3>
                    <pre>项目id：{this.state.id}
                        <br/>项目名称：{this.state.name}
                        <br/>项目描述：{this.state.description}
                        <br/>创建者：{this.state.addUser}
                        <br/>创建时间：{this.state.addTime}
                    </pre>
                    <h3><b>2. 用户信息</b><br/></h3>
                    <div style={{display: 'flex'}}>
                        <Select
                            mode="multiple"
                            allowClear
                            showSearch
                            notFoundContent={this.state.fetching ? <Spin size="small" /> : null}
                            filterOption={false}
                            value={this.state.userIdList}
                            style={{ width: '40%', marginRight: '5px' }}
                            placeholder="搜索选择用户"
                            onSearch={this.handleSearchUser}
                            onChange={this.handleChangeUser}>
                            {this.getUserOptions()}
                        </Select>
                        <Select
                            mode="multiple"
                            allowClear
                            showSearch
                            notFoundContent={null}
                            value={this.state.roleIdList}
                            style={{ width: '40%', marginRight: '5px' }}
                            placeholder="搜索选择角色"
                            onChange={this.handleChangeRole}>
                            {this.getRoleOptions()}
                        </Select>
                        <Button loading={this.state.loading} onClick={() => this.add()} type="primary">添加</Button>
                    </div>
                    <div>
                        <Table
                            size="small"
                            footer={() => '共' + this.state.data.length + '条数据'}
                            dataSource={this.state.data}
                            loading={this.state.loadingProjectUser}
                            pagination={this.state.pagination}
                            onChange={this.onChange}
                            columns={columns} />
                    </div>
                </div>
            </div>
        </div>)
    }
}
export default withRouter(ProjectUserSetting);
