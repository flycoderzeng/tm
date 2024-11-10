import React from 'react';
import {Button, Input, Table} from 'antd';
import {withRouter} from "react-router-dom";
import CommonListPage from "../../common/CommonListPage";
import {CommonApiUrlModel} from "../../../entities/CommonApiUrlModel";
import {ApiUrlConfig} from "../../../config/api.url";

const { Search } = Input;


class UserList extends CommonListPage {
    constructor(props) {
        super(props);
        const commonApiUrlModel: CommonApiUrlModel = {
            listUrl: ApiUrlConfig.QUERY_USER_LIST_URL,
            saveUrl: ApiUrlConfig.SAVE_USER_URL,
            deleteUrl: ApiUrlConfig.DELETE_USER_URL,
            loadUrl: ApiUrlConfig.LOAD_USER_URL,
            editUrl: '/useredit/:id'
        };
        this.commonApiUrlModel = commonApiUrlModel;
        this.modelType = 'user';
        this.state.queryInfo.area = '-1';
    }

    componentDidMount() {
        this.loadDataList(this.state.pagination);
    }

    onSearch = (value) => {
        let filterConditionList =
            [{"columnName": "username", "value":value},
                {"columnName": "chinese_name", "value":value}];
        this._onSearch({searchValue: value}, filterConditionList);
    }

    onChange = (pagination) => {
        this.loadDataList(pagination);
    }

    userRoleSetting(id) {
        this.props.history.push('/userrolesetting/' + id);
    }

    render() {
        const columns: any[] = [
            {
                title: 'ID',
                dataIndex: 'id',
                render: text => <span>{text}</span>,
            },{
                title: '英文名',
                dataIndex: 'username',
                render: (text, record) => <Button
                    className="padding-left0"
                    size="small"
                    type="link"
                    onClick={() => this.edit(record.id)}>{text}</Button>,
            },{
                title: '中文名',
                dataIndex: 'chineseName',
                render: text => <span>{text}</span>,
            },{
                title: '创建者',
                dataIndex: 'addUser',
                render: text => <span>{text}</span>,
            },{
                title: '创建时间',
                dataIndex: 'addTime',
                render: text => <span>{text}</span>,
            },{
                title: '修改者',
                dataIndex: 'lastModifyUser',
                render: text => <span>{text}</span>,
            },{
                title: '修改时间',
                dataIndex: 'lastModifyTime',
                render: text => <span>{text}</span>,
            },{
                title: '操作',
                fixed: 'right',
                render: (text, record) => (
                    <div>
                        <Button className="padding-left0" size="small" type="link" onClick={() => this.edit(record.id)}>修改</Button>
                        <Button danger size="small" type="link" onClick={() => this.delete(record.id)}>删除</Button>
                        <Button size="small" type="link" onClick={() => this.userRoleSetting(record.id)}>用户角色</Button>
                    </div>
                ),
            },
        ];
        return (<div className="card">
            <div className="card-header card-header-divider">用户<span className="card-subtitle">管理员可以配置</span>
            </div>
            <div className="card-body">
                <div className="list-toolbar">
                    <Search placeholder="英文名或中文名" onSearch={this.onSearch} enterButton style={{ width: 400,}}/>
                    <Button type="primary" onClick={() => this.edit(0)}>添加用户</Button>
                </div>
                <Table columns={columns}
                       dataSource={this.state.data}
                       size="small"
                       footer={() => '共' + this.state.pagination.total + '条数据'}
                       loading={this.state.loading}
                       pagination={this.state.pagination}
                       onChange={this.onChange}
                />
            </div>
        </div>)
    }
}

export default withRouter(UserList);
