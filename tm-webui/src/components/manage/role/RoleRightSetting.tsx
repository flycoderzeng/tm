import React from 'react';
import { RouteComponentProps,withRouter } from "react-router-dom";
import axios from "axios";
import {Button, message, Table, Tooltip, Select} from "antd";
import {ArrowLeftOutlined} from "@ant-design/icons";
import moment from "moment";
import {ApiUrlConfig} from "../../../config/api.url";
interface IProps {}
type CurrProps = IProps & RouteComponentProps;
const { Option } = Select;
interface RoleRightModel {
    id: number;
    rightName: string;
    uri: string;
    type: number;
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
    type: number;
    data: RoleRightModel[];
    rightIdList: string[];
    optionList: any[];
}
class RoleRightSetting extends React.Component<CurrProps, IState> {
    constructor(props) {
        super(props);
        this.state = {
            id: props.match.params.id,
            name: '',
            description: '',
            type: 1,
            addUser: '',
            addTime: '',
            loading: false,
            rightIdList: [],
            data: [],
            optionList: []
        }
    }
    loadRoleInfo() {
        if (this.state.id > 0) {
            axios.post(ApiUrlConfig.LOAD_ROLE_URL, {id: this.state.id}).then(resp => {
                if (resp.status !== 200) {
                    message.error('加载角色失败');
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
                            type: ret.data.type,
                            addUser: ret.data.addUser,
                            addTime: moment(new Date(ret.data.addTime)).format('YYYY-MM-DD HH:mm:ss')
                        });
                        this.getRightList();
                    }
                }
            });
        }
    }
    loadRoleRightInfo() {
        if (this.state.id > 0) {
            axios.post(ApiUrlConfig.QUERY_ROLE_RIGHT_LIST_URL, {id: this.state.id}).then(resp => {
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

    componentDidMount() {
        this.loadRoleInfo();
        this.loadRoleRightInfo();
    }

    delete = id => {
        if(!window.confirm("确定删除吗？")) {
            return;
        }
        axios.post(ApiUrlConfig.DELETE_ROLE_RIGHT_URL, {id: id}).then(resp => {
            if (resp.status !== 200) {
                message.error('删除失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    this.loadRoleRightInfo();
                }
            }
        });
    }

    back() {
        this.props.history.push('/rolelist');
    }

    handleChange = value  => {
        this.setState({
            rightIdList: value
        });
    }

    getRightList() {
        const children :any[] = [];
        axios.post(ApiUrlConfig.QUERY_RIGHT_LIST_URL,
            {
                pageNum: 1,
                pageSize: 1000,
                filterConditionList: [{columnName: 'type', operator: '=', value: this.state.type}]
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
                            text: v.name,
                            value: v.id.toString()
                        });
                        return true;
                    });
                    this.setState({
                        optionList: children
                    });
                }
            }
        });
    }

    add() {
        if(this.state.rightIdList.length < 1) {
            return;
        }
        const rightIdList = this.state.rightIdList.map(v => {
            return v.split('-')[0];
        });
        const data = {roleId: this.state.id, rightIdList: rightIdList};
        this.setState({loading: true});
        axios.post(ApiUrlConfig.ADD_ROLE_RIGHT_URL, data).then(resp => {
            if (resp.status !== 200) {
                message.error('操作失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    this.loadRoleRightInfo();
                    this.setState({
                        rightIdList: []
                    });
                }
            }
        }).finally(() => {
            this.setState({loading: false});
        });
    }

    getOptions() {
        const options = this.state.optionList.map(function(d) {
            return <Option key={d.value} value={d.value + '-' + d.text}>{d.text}</Option>
        });
        return options;
    }

    render() {
        const columns: any[] = [
            {
                title: '权限id',
                dataIndex: 'rightId',
                key: 'rightId',
            },
            {
                title: '权限名称',
                dataIndex: 'rightName',
                key: 'rightName',
            },
            {
                title: 'uri',
                dataIndex: 'uri',
                key: 'uri',
            },
            {
                title: '类型',
                dataIndex: 'rightType',
                key: 'rightType',
                render: text => <span>{text === 1 ? '系统类型' : '项目类型'}</span>,
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
                角色权限关联配置
                <Tooltip title="返回">
                <Button onClick={() => this.back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
            </Tooltip>
                <span className="card-subtitle">管理员可以配置</span>
            </div>
            <div className="card-body">
                <div>
                    <h3><b>1. 角色信息</b><br/></h3>
                    <pre>角色名称：{this.state.name}
                    <br/>角色描述：{this.state.description}
                    <br/>角色类型：{this.state.type === 1 ? '系统角色' : '项目角色'}
                    <br/>创建者：{this.state.addUser}
                    <br/>创建时间：{this.state.addTime}
                    </pre>
                    <h3><b>2. 权限信息</b><br/></h3>
                    <div style={{display: 'flex'}}>
                        <Select
                            mode="multiple"
                            allowClear
                            showSearch
                            notFoundContent={null}
                            value={this.state.rightIdList}
                            style={{ width: '70%', marginRight: '5px' }}
                            placeholder="搜索选择权限"
                            onChange={this.handleChange}>
                            {this.getOptions()}
                        </Select>
                        <Button loading={this.state.loading} onClick={() => this.add()} type="primary">添加</Button>
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

export default withRouter(RoleRightSetting);
