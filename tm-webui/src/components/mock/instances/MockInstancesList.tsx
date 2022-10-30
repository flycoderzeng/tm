import React from 'react';
import {Table, Button} from 'antd';
import { Input } from 'antd';
import {withRouter} from "react-router-dom";
import CommonListPage from "../../common/CommonListPage";
import {CommonApiUrlModel} from "../../../entities/CommonApiUrlModel";
import {ApiUrlConfig} from "../../../config/api.url";

const { Search } = Input;


class MockInstancesList extends CommonListPage {
    constructor(props) {
        super(props);
        const commonApiUrlModel: CommonApiUrlModel = {
            listUrl: ApiUrlConfig.QUERY_MOCK_INSTANCE_LIST_URL,
            saveUrl: '',
            deleteUrl: ApiUrlConfig.DELETE_MOCK_INSTANCE_URL,
            loadUrl: ApiUrlConfig.LOAD_MOCK_INSTANCE_URL,
            editUrl: '/mockinstanceedit/:id'
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

    onChange = (pagination, filters, sorter) => {
        this.loadDataListSort(pagination, filters, sorter);
        this.setState({sortedInfo: sorter});
    }

    render() {
        const columns: any[] = [
            {
                title: 'ID',
                dataIndex: 'id',
                key: 'id',
                sorter: ()=>{},
                render: text => <span>{text}</span>,
            },{
                title: '名称',
                dataIndex: 'name',
                key: 'id',
                sorter: ()=>{},
                render: (text, record) => <Button
                    className="padding-left0"
                    size="small"
                    type="link"
                    onClick={() => this.edit(record.id)}>{text}</Button>,
            },{
                title: '描述',
                dataIndex: 'description',
                render: text => <span>{text}</span>,
            },{
                title: '在线实例数/总实例数',
                dataIndex: 'onlineTotal',
                key: 'id',
                sorter: ()=>{},
                render: (text, record) => <span>{record.onlineTotal || 0}/{record.total}</span>,
            },{
                title: '操作',
                fixed: 'right',
                render: (text, record) => (
                    <div>
                        <Button className="padding-left0" size="small" type="link" onClick={() => this.edit(record.id)}>配置</Button>
                        <Button danger size="small" type="link" onClick={() => this.concern(record.id)}>关注</Button>
                    </div>
                ),
            },
        ];
        return (<div className="card">
            <div className="card-header card-header-divider">MOCK实例<span className="card-subtitle">MOCK实例列表</span>
            </div>
            <div className="card-body">
                <div className="list-toolbar">
                    <Search placeholder="Id或者名称" onSearch={this.onSearch} enterButton style={{ width: 400,}}/>
                    <Button type="primary" onClick={() => this.edit(0)}>添加MOCK规则</Button>
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

    private concern(id) {

    }
}

export default withRouter(MockInstancesList);
