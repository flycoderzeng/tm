import React from 'react';
import {Table, Button, Radio} from 'antd';
import { Input } from 'antd';
import {withRouter} from "react-router-dom";
import CommonListPage from "../../common/CommonListPage";
import {CommonApiUrlModel} from "../../../entities/CommonApiUrlModel";
import {ApiUrlConfig} from "../../../config/api.url";
import {OptionsConfig} from "../../../config/options.config";

const { Search } = Input;

class CronJobList extends CommonListPage {
    constructor(props) {
        super(props);
        const commonApiUrlModel: CommonApiUrlModel = {
            listUrl: ApiUrlConfig.QUERY_CRON_JOB_LIST_URL,
            saveUrl: ApiUrlConfig.SAVE_CRON_JOB_URL,
            deleteUrl: ApiUrlConfig.DELETE_CRON_JOB_URL,
            loadUrl: '',
            editUrl: '/cronjobedit/:id'
        };
        this.commonApiUrlModel = commonApiUrlModel;
        this.modelType = 'plan_cron_job';
    }

    componentDidMount() {
        this.loadDataList(this.state.pagination);
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
                render: text => <span>{text}</span>,
            }, {
                title: '名称',
                dataIndex: 'name',
                render: (text, record) => <Button
                    className="padding-left0"
                    size="small"
                    type="link"
                    onClick={() => this.edit(record.id)}>{text}</Button>,
            }, {
                title: '定时表达式',
                dataIndex: 'cronExpression',
                render: text => <span>{text}</span>,
            }, {
                title: '创建者',
                dataIndex: 'addUser',
                render: text => <span>{text}</span>,
            }, {
                title: '创建时间',
                dataIndex: 'addTime',
                key: 'add_time',
                sorter: ()=>{},
                render: text => <span>{text}</span>,
            }, {
                title: '最近运行时间',
                dataIndex: 'lastRunTime',
                key: 'last_run_time',
                render: text => <span>{text}</span>,
            }, {
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
        const {area} = this.state.queryInfo;
        this.showCommonColumns(columns);

        return (<div className="card">
            <div className="card-header card-header-divider">定时任务<span className="card-subtitle">定时执行自动化计划</span>
            </div>
            <div className="card-body">
                <div className="list-toolbar">
                    <Search placeholder="Id或者名称" onSearch={this.onSearch} enterButton
                            onChange={this.onChangeSearchValue}
                            style={{ width: 400,marginRight: '5px'}}/>
                    <Radio.Group
                        options={OptionsConfig.queryAreaOptions}
                        onChange={this.onChangeQueryArea}
                        value={area}
                        optionType="button"
                        buttonStyle="solid"
                        style={{marginRight: '5px'}}
                    />
                    <Button type="primary" onClick={() => this.edit(0)}>添加定时任务</Button>
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

export default withRouter(CronJobList);
