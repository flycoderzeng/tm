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
        axios.get(ApiUrlConfig.QUERY_ROLE_LIST_URL + '?page%5Btotals%5D&page%5Bnumber%5D=1&filter%5Brole%5D=status%3D%3D0;type%3D%3D02&sort=id&page%5Bsize%5D=100').then(resp => {
            if (resp.status !== 200) {
                message.error('??????????????????');
            } else {
                const ret = resp.data;
                ret.data && ret.data.map(v => {
                    children.push({
                        text: v.attributes.chineseName+"("+v.attributes.description+")",
                        value: v.id.toString()
                    });
                    return true;
                });
                this.setState({
                    roleOptionList: children
                });
            }
        });
    }

    loadProjectInfo() {
        if(this.state.id > 0) {
            axios.get(ApiUrlConfig.LOAD_PROJECT_URL + this.state.id).then(resp => {
                if (resp.status !== 200) {
                    message.error('????????????');
                } else {
                    const ret = resp.data;
                    if (!ret.data) {
                        return;
                    }
                    this.setState({
                        name: ret.data.attributes.name,
                        description: ret.data.attributes.description,
                        addTime: ret.data.attributes.addTime,
                        addUser: ret.data.attributes.addUser
                    });
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

        this.setState({
            fetching: true,
        });
        const children :any[] = [];
        axios.get(ApiUrlConfig.QUERY_USER_LIST_URL + '?page%5Btotals%5D&page%5Bnumber%5D=1&filter%5Buser%5D=status%3D%3D0;(username%3D%3D*' + encodeURIComponent(value) + '*,chineseName%3D%3D*' + encodeURIComponent(value) + '*)&sort=id&page%5Bsize%5D=30').then(resp => {
            if (resp.status !== 200) {
                message.error('????????????');
            } else {
                const ret = resp.data;
                if (!ret.data) {
                    return;
                }
                ret.data && ret.data.map(function(v) {
                    children.push({
                        text: v.attributes.username+"("+v.attributes.chineseName+")",
                        value: v.id.toString()
                    });
                    return true;
                });
                this.setState({
                    userOptionList: children
                });
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
            message.info('???????????????');
            return;
        }
        if(this.state.roleIdList.length < 1) {
            message.info('???????????????');
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
                message.error('????????????');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('????????????');
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
        if (!window.confirm("??????????????????")) {
            return;
        }
        axios.post(ApiUrlConfig.DELETE_PROJECT_USER_URL, {id: id}).then(resp => {
            if (resp.status !== 200) {
                message.error('????????????');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('????????????');
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
                message.error('????????????????????????');
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
        if(!window.confirm("??????????????????")) {
            e.preventDefault();
            return;
        }
        axios.post(ApiUrlConfig.DELETE_PROJECT_USER_ROLE_URL,
            {projectId: this.state.id, userId: userId, roleId: roleId}).then(resp => {
            if (resp.status !== 200) {
                message.error('????????????');
                e.preventDefault();
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                    e.preventDefault();
                } else {
                    message.success('????????????');
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
                title: '????????????',
                dataIndex: 'username',
            },
            {
                title: '?????????',
                dataIndex: 'chineseName',
            },
            {
                title: '??????',
                dataIndex: 'roleList',
                render: (text: any[], record) => this.renderRoleList(text, record),
            },
            {
                title: '?????????',
                dataIndex: 'addUser',
                render: text => <span>{text}</span>,
            },{
                title: '????????????',
                dataIndex: 'addTime',
                render: text => <span>{text}</span>,
            }, {
                title: '??????',
                fixed: 'right',
                render: (text, record) => (
                    <div>
                        <Tooltip title="??????">
                            <Button danger size="small" type="primary" shape="circle"
                                    onClick={() => this.delete(record.id)}>D</Button>
                        </Tooltip>
                    </div>
                )
            }];
        return (<div className="card">
            <div className="card-header card-header-divider">
                ??????????????????
                <Tooltip title="??????">
                    <Button onClick={() => this.back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
                </Tooltip>
                <span className="card-subtitle">?????????????????????</span>
            </div>
            <div className="card-body">
                <div>
                    <h3><b>1. ????????????</b><br/></h3>
                    <pre>??????id???{this.state.id}
                        <br/>???????????????{this.state.name}
                        <br/>???????????????{this.state.description}
                        <br/>????????????{this.state.addUser}
                        <br/>???????????????{this.state.addTime}
                    </pre>
                    <h3><b>2. ????????????</b><br/></h3>
                    <div style={{display: 'flex'}}>
                        <Select
                            mode="multiple"
                            allowClear
                            showSearch
                            notFoundContent={this.state.fetching ? <Spin size="small" /> : null}
                            filterOption={false}
                            value={this.state.userIdList}
                            style={{ width: '40%', marginRight: '5px' }}
                            placeholder="??????????????????"
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
                            placeholder="??????????????????"
                            onChange={this.handleChangeRole}>
                            {this.getRoleOptions()}
                        </Select>
                        <Button loading={this.state.loading} onClick={() => this.add()} type="primary">??????</Button>
                    </div>
                    <div>
                        <Table
                            size="small"
                            footer={() => '???' + this.state.data.length + '?????????'}
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
