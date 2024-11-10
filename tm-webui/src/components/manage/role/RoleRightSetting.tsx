import React from 'react';
import {RouteComponentProps, withRouter} from "react-router-dom";
import axios from "axios";
import {Button, message, Select, Table, Tooltip} from "antd";
import {ArrowLeftOutlined} from "@ant-design/icons";
import {ApiUrlConfig} from "../../../config/api.url";

interface IProps {}
type CurrProps = IProps & RouteComponentProps;
const { Option } = Select;
interface RoleRightModel {
    rightId: number;
    key: string;
    name: string;
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
            axios.get(ApiUrlConfig.LOAD_ROLE_URL + this.state.id + '?include=rights').then(resp => {
                if (resp.status !== 200) {
                    message.error('加载角色失败');
                } else {
                    const ret = resp.data;
                    if (!ret.data) {
                        return;
                    }
                    const rows: RoleRightModel[] = [];
                    if (ret.included) {
                        ret.included.map(v => {
                            rows.push({name: v.attributes.name,
                                uri: v.attributes.uri,
                                rightId: v.id,
                                type: v.attributes.type,
                                addUser: v.attributes.addUser,
                                key: v.id+'',
                                addTime: v.attributes.addTime});
                            return null;
                        });
                    }
                    this.setState({
                        name: ret.data.attributes.name,
                        description: ret.data.attributes.description,
                        type: ret.data.attributes.type,
                        addUser: ret.data.attributes.addUser,
                        addTime: ret.data.attributes.addTime,
                        data: rows
                    });
                    this.getRightList();
                }
            });
        }
    }

    componentDidMount() {
        this.loadRoleInfo();
    }

    delete = rightId => {
        if(!window.confirm("确定删除吗？")) {
            return;
        }
        axios.post(ApiUrlConfig.DELETE_ROLE_RIGHT_URL, {rightId: rightId, roleId: this.state.id}).then(resp => {
            if (resp.status !== 200) {
                message.error('删除失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    this.loadRoleInfo();
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
        axios.get(ApiUrlConfig.QUERY_RIGHT_LIST_URL + '?page%5Btotals%5D&page%5Bnumber%5D=1&filter%5Bright%5D=status%3D%3D0;type%3D%3D' + this.state.type + '&sort=id&page%5Bsize%5D=100').then(resp => {
            if (resp.status !== 200) {
                message.error('加载列表失败');
            } else {
                const ret = resp.data;
                ret.data && ret.data.map(function(v) {
                    children.push({
                        text: v.attributes.name + ': ' + v.attributes.uri,
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
                    this.setState({
                        rightIdList: []
                    });
                    this.loadRoleInfo();
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
                dataIndex: 'name',
                key: 'name',
            },
            {
                title: 'uri',
                dataIndex: 'uri',
                key: 'uri',
            },
            {
                title: '类型',
                dataIndex: 'type',
                key: 'type',
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
                                    onClick={() => this.delete(record.rightId)}>D</Button>
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
