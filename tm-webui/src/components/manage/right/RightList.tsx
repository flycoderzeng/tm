import React from 'react';
import {Table, Button, Tooltip} from 'antd';
import { Input } from 'antd';
import {withRouter} from "react-router-dom";
import CommonListPage from "../../common/CommonListPage";
import {CommonApiUrlModel} from "../../../entities/CommonApiUrlModel";
import {ApiUrlConfig} from "../../../config/api.url";

const { Search } = Input;


class RightList extends CommonListPage {
    constructor(props) {
        super(props);
        const commonApiUrlModel: CommonApiUrlModel = {
            listUrl: ApiUrlConfig.QUERY_RIGHT_LIST_URL,
            saveUrl: ApiUrlConfig.SAVE_RIGHT_URL,
            deleteUrl: ApiUrlConfig.DELETE_RIGHT_URL,
            loadUrl: ApiUrlConfig.LOAD_RIGHT_URL,
            editUrl: '/rightedit/:id'
        };
        this.commonApiUrlModel = commonApiUrlModel;
    }

    componentDidMount() {
        this.loadDataList(this.state.pagination);
    }

    onSearch = (value) => {
        let filterConditionList =
            [{"columnName": "id", "operator": "=", "value":value},
                {"columnName": "name", "value":value}];
        this._onSearch({searchValue: value}, filterConditionList);
    }

    onChange = (pagination) => {
        this.loadDataList(pagination);
    }

    render() {
        const columns: any[] = [
            {
                title: 'ID',
                dataIndex: 'id',
                render: text => <span>{text}</span>,
            },{
                title: '名称',
                dataIndex: 'name',
                render: (text, record) => <Button
                    className="padding-left0"
                    size="small"
                    type="link"
                    onClick={() => this.edit(record.id)}>{text}</Button>,
            },{
                title: '路径',
                dataIndex: 'uri',
                render: text => <span>{text}</span>,
            },{
                title: '类型',
                dataIndex: 'type',
                render: text => <span>{text === 1 ? '系统类型' : '项目类型'}</span>,
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
                    </div>
                ),
            },
        ];
        return (<div className="card">
            <div className="card-header card-header-divider">权限<span className="card-subtitle">管理员可以配置</span>
            </div>
            <div className="card-body">
                <div className="list-toolbar">
                    <Search placeholder="id或名称" onSearch={this.onSearch} enterButton style={{ width: 400,}}/>
                    <Button type="primary" onClick={() => this.edit(0)}>添加权限</Button>
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

export default withRouter(RightList);
