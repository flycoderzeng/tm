import React from 'react';
import type {TableColumnsType} from 'antd';
import {Button, Input, message, Space, Table, Tag} from 'antd';
import {withRouter} from "react-router-dom";
import CommonListPage from "../../common/CommonListPage";
import {CommonApiUrlModel} from "../../../entities/CommonApiUrlModel";
import {ApiUrlConfig} from "../../../config/api.url";
import axios from "axios";
import {StrUtils} from "../../../utils/StrUtils";
import {LocalStorageUtils} from "../../../utils/LocalStorageUtils";

const { Search } = Input;


class MockInstancesList extends CommonListPage {
    constructor(props) {
        super(props);
        const commonApiUrlModel: CommonApiUrlModel = {
            listUrl: ApiUrlConfig.QUERY_MOCK_INSTANCE_LIST_URL,
            saveUrl: '',
            deleteUrl: '',
            loadUrl: '',
            editUrl: '/mockinstanceedit/:id'
        };
        this.commonApiUrlModel = commonApiUrlModel;

        this.state = {
            data: [],
            loading: false,
            isModalVisible: false,
            pagination: {
                current: 1,
                pageNum: 1,
                pageSize: 10,
                linkOperator: 'or',
                total: 0,
                filterConditionList: []
            },
            sortedInfo: {},
            queryInfo: {
                area: '1',
                searchValue: ''
            },
            filteredValue: LocalStorageUtils.getFilteredValue(null),
            rows: []
        };
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

    onExpand = (expanded: boolean, record: any) => {
        if(expanded) {
            const params: any = {
                'page[totals]': '',
                'page[number]': 1,
                'page[size]': 100,
                'sort': '-status',
                'filter[mock_agent_instance]': 'name==' + record.name
            }
            const rows: any[] = [];
            axios.get(ApiUrlConfig.QUERY_MOCK_INSTANCE_LIST_LC_URL + '?' + StrUtils.getGetParams(params),).then(resp => {
                if (resp.status !== 200) {
                    message.error('加载mock实例列表失败');
                } else {
                    const ret = resp.data;
                    if (ret.data) {
                        ret.data.map((v: any) => {
                            v.attributes.id = v.id;
                            v.attributes.key = v.id;
                            rows.push(v.attributes);
                        });
                    }
                    this.setState({
                        rows: rows,
                    });
                }
            });
        }
    }

    expandedMockInstancesRender = () => {
        const columns: TableColumnsType<any> = [
            { title: '应用名称', dataIndex: 'applicationName', key: 'id' },
            { title: 'IP', dataIndex: 'ip', key: 'id' },
            { title: '端口', dataIndex: 'port', key: 'id' },
            {
                title: '最近一次注册时间',
                dataIndex: 'lastRegisterTime',
                key: 'id',
            },
            {
                title: '状态',
                dataIndex: 'status',
                key: 'id',
                render: (v) => {
                    if(v === 1)
                        return (<Tag color="orange">在线</Tag>);
                    else
                        return (<Tag>离线</Tag>);
                }
            },
            {
                title: '操作',
                dataIndex: 'operation',
                key: 'operation',
                render: () => (
                    <Space size="middle">
                        <a>规则配置</a>
                    </Space>
                ),
            },
        ];
        return <Table columns={columns} dataSource={this.state.rows} pagination={false} />;
    };

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
                       expandable={{ expandedRowRender:this.expandedMockInstancesRender, onExpand: this.onExpand }}
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
