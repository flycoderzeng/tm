import React from 'react';
import {Table, Button, Radio} from 'antd';
import { Input } from 'antd';
import {withRouter} from "react-router-dom";
import CommonListPage from "../../common/CommonListPage";
import {CommonApiUrlModel} from "../../../entities/CommonApiUrlModel";
import {ApiUrlConfig} from "../../../config/api.url";
import {OptionsConfig} from "../../../config/options.config";

const { Search } = Input;


class HttpMockRuleList extends CommonListPage {
    constructor(props) {
        super(props, 'HttpMockRuleList');
        const commonApiUrlModel: CommonApiUrlModel = {
            listUrl: ApiUrlConfig.QUERY_HTTP_MOCK_RULE_LIST_URL,
            saveUrl: ApiUrlConfig.SAVE_HTTP_MOCK_RULE_URL,
            deleteUrl: ApiUrlConfig.DELETE_HTTP_MOCK_RULE_URL,
            loadUrl: ApiUrlConfig.LOAD_HTTP_MOCK_RULE_URL,
            editUrl: '/httpmockruleedit/:id'
        };
        this.commonApiUrlModel = commonApiUrlModel;
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

    onChangeQueryArea = (e) => {
        this._onChangeQueryArea(e);
        this._onSearch({searchValue: null, area: e.target.value}, null);
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
                title: 'uri',
                dataIndex: 'uri',
                render: text => <span>{text}</span>,
            },{
                title: '请求方法',
                dataIndex: 'method',
                width: 150,
                render: text => <span>{text === '1' ? 'GET' : 'POST'}</span>,
            },{
                title: '操作',
                fixed: 'right',
                render: (text, record) => (
                    <div>
                        <Button className="padding-left0" size="small" type="link" onClick={() => this.edit(record.id)}>修改</Button>
                        <Button danger size="small" type="link" onClick={() => this.delete(record.id)}>删除</Button>
                    </div>
                ),
                filters: this.columnFilters,
                filteredValue: this.state.filteredValue,
                onFilter: (value, record) => this.onFilter(value,record)
            },
        ];

        this.showCommonColumns(columns);

        const {area} = this.state.queryInfo;
        return (<div className="card">
            <div className="card-header card-header-divider">Http规则<span className="card-subtitle">Http规则展示、新增、修改、删除</span>
            </div>
            <div className="card-body">
                <div className="list-toolbar">
                    <Search placeholder="Id或者名称" onSearch={this.onSearch} enterButton style={{ width: 400, marginRight: '5px'}}/>
                    <Radio.Group
                        options={OptionsConfig.queryAreaOptions}
                        onChange={this.onChangeQueryArea}
                        value={area}
                        optionType="button"
                        buttonStyle="solid"
                        style={{marginRight: '5px'}}
                    />
                    <Button type="primary" onClick={() => this.edit(0)}>添加Http规则</Button>
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

export default withRouter(HttpMockRuleList);
