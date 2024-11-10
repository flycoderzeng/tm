import React from 'react';
import {Button, Input, Table} from 'antd';
import {withRouter} from "react-router-dom";
import CommonListPage from "../../common/CommonListPage";
import {CommonApiUrlModel} from "../../../entities/CommonApiUrlModel";
import {ApiUrlConfig} from "../../../config/api.url";

const { Search } = Input;


class RunEnvList extends CommonListPage {
    constructor(props) {
        super(props, 'RunEnvList');
        const commonApiUrlModel: CommonApiUrlModel = {
            listUrl: ApiUrlConfig.QUERY_RUN_ENV_LIST_URL,
            saveUrl: ApiUrlConfig.SAVE_RUN_ENV_URL,
            deleteUrl: ApiUrlConfig.DELETE_RUN_ENV_URL,
            loadUrl: ApiUrlConfig.LOAD_RUN_ENV_URL,
            editUrl: '/runenvedit/:id'
        };
        this.commonApiUrlModel = commonApiUrlModel;
        this.modelType = 'run_env';
        this.state.queryInfo.area = '-1';
    }

    componentDidMount() {
        this.loadDataListSort(this.state.pagination, null, null);
    }

    onSearch = (value) => {
        let filterConditionList =
            [{"columnName": "name", "value":value},
                {"columnName": "id", "operator": "=", "value":value}];
        this._onSearch({searchValue: value}, filterConditionList);
    }

    onChange = (pagination, filters, sorter, {action}) => {
        if(action !== 'filter') {
            this.loadDataListSort(pagination, filters, sorter);
        }
        this.updateCommonState(this.constructor.name, filters, sorter);
    }

    render() {
        const columns: any[] = [
            {
                title: 'ID',
                dataIndex: 'id',
                key: 'id',
                width: 150,
                sorter: ()=>{},
                render: text => <span>{text}</span>,
            },{
                title: '名称',
                dataIndex: 'name',
                key: 'name',
                sorter: ()=>{},
                render: (text, record) => <Button
                    className="padding-left0"
                    size="small"
                    type="link"
                    onClick={() => this.edit(record.id)}>{text}</Button>,
            },{
                title: '操作',
                fixed: 'right',
                filters: this.columnFilters,
                filteredValue: this.state.filteredValue,
                onFilter: (value, record) => this.onFilter(value,record),
                render: (text, record) => (
                    <div>
                        <Button className="padding-left0" size="small" type="link" onClick={() => this.edit(record.id)}>修改</Button>
                        <Button danger size="small" type="link" onClick={() => this.delete(record.id)}>删除</Button>
                    </div>
                ),
            },
        ];

        this.showCommonColumns(columns);

        return (<div className="card">
            <div className="card-header card-header-divider">运行环境<span className="card-subtitle">计划运行环境配置</span>
            </div>
            <div className="card-body">
                <div className="list-toolbar">
                    <Search placeholder="Id或者名称" onSearch={this.onSearch} enterButton style={{ width: 400,}}/>
                    <Button type="primary" onClick={() => this.edit(0)}>添加运行环境</Button>
                </div>
                <Table columns={columns}
                       tableLayout="fixed"
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

export default withRouter(RunEnvList);
