import React from 'react';
import {Button, Input, Table} from 'antd';
import {withRouter} from "react-router-dom";
import CommonListPage from "../../common/CommonListPage";
import {CommonApiUrlModel} from "../../../entities/CommonApiUrlModel";
import {ApiUrlConfig} from "../../../config/api.url";

const { Search } = Input;


class TagList extends CommonListPage {
    constructor(props) {
        super(props, 'TagList');
        const commonApiUrlModel: CommonApiUrlModel = {
            listUrl: ApiUrlConfig.QUERY_TAG_LIST_URL,
            saveUrl: ApiUrlConfig.SAVE_TAG_URL,
            deleteUrl: ApiUrlConfig.DELETE_TAG_URL,
            loadUrl: ApiUrlConfig.LOAD_TAG_URL,
            editUrl: '/tagedit/:id'
        };
        this.commonApiUrlModel = commonApiUrlModel;
        this.modelType = 'tags';
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
                sorter: ()=>{},
                render: (text, record) => <Button
                    className="padding-left0"
                    size="small"
                    type="link"
                    onClick={() => this.edit(record.id)}>{text}</Button>,
            },{
                title: '项目名称',
                dataIndex: 'projectName',
                sorter: ()=>{},
                render: text => <span>{text}</span>,
            },{
                title: '操作',
                fixed: 'right',
                width: 200,
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
            <div className="card-header card-header-divider">标签<span className="card-subtitle">自动化用例标签配置</span>
            </div>
            <div className="card-body">
                <div className="list-toolbar">
                    <Search placeholder="Id或者名称" onSearch={this.onSearch} enterButton style={{ width: 400,}}/>
                    <Button type="primary" onClick={() => this.edit(0)}>添加标签</Button>
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

export default withRouter(TagList);
