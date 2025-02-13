import CommonListPage from "../../common/CommonListPage";
import {withRouter} from "react-router-dom";
import {CommonApiUrlModel} from "../../../entities/CommonApiUrlModel";
import {ApiUrlConfig} from "../../../config/api.url";
import {Button, Input, Radio, Table} from "antd";
import {OptionsConfig} from "../../../config/options.config";
import React from "react";

const { Search } = Input;

class DcnConfigList extends CommonListPage {
    constructor(props) {
        super(props, 'DcnConfigList');
        const commonApiUrlModel: CommonApiUrlModel = {
            listUrl: ApiUrlConfig.QUERY_DCN_CONFIG_LIST_URL,
            saveUrl: ApiUrlConfig.SAVE_DCN_CONFIG_URL,
            deleteUrl: ApiUrlConfig.DELETE_DCN_CONFIG_URL,
            loadUrl: ApiUrlConfig.LOAD_DCN_CONFIG_URL,
            editUrl: '/dcnconfigedit/:id'
        };
        this.commonApiUrlModel = commonApiUrlModel;
        this.modelType = 'dcn_config';
    }

    componentDidMount() {
        this.loadDataList(this.state.pagination);
    }

    onSearch = (value) => {
        let filterConditionList =
            [{"columnName": "dcn_name", "value":value},
                {"columnName": "id", "operator": "=", "value":value}];
        this._onSearch({searchValue: value}, filterConditionList);
    }

    onChangeQueryArea = (e) => {
        this._onChangeQueryArea(e);
        this._onSearch({searchValue: null, area: e.target.value}, null);
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
                render: text => <span>{text}</span>,
            },{
                title: 'DCN名称',
                dataIndex: 'dcnName',
                key: 'dcn_name',
                sorter: ()=>{},
                render: (text, record) => <Button
                    className="padding-left0"
                    size="small"
                    type="link"
                    onClick={() => this.edit(record.id)}>{text}</Button>,
            },{
                title: 'DCN描述',
                dataIndex: 'dcnDescription',
                key: 'dcn_description',
                sorter: ()=>{},
                render: text => <span>{text}</span>,
            },{
                title: '创建者',
                dataIndex: 'addUser',
                render: text => <span>{text}</span>,
            },{
                title: '创建时间',
                dataIndex: 'addTime',
                key: 'addTime',
                sorter: ()=>{},
                render: text => <span>{text}</span>,
            },{
                title: '修改者',
                dataIndex: 'lastModifyUser',
                render: text => <span>{text}</span>,
            },{
                title: '修改时间',
                dataIndex: 'lastModifyTime',
                key: 'lastModifyTime',
                sorter: ()=>{},
                render: text => <span>{text}</span>,
            },{
                title: '操作',
                render: (text, record) => (
                    <div>
                        <Button className="padding-left0" size="small" type="link" onClick={() => this.edit(record.id)}>修改</Button>
                        <Button danger size="small" type="link" onClick={() => this.delete(record.id)}>删除</Button>
                    </div>
                ),
            },
        ];
        const {area} = this.state.queryInfo;
        return (<div className="card">
            <div className="card-header card-header-divider">DCN配置
                <span className="card-subtitle">DCN名称、描述配置</span>
            </div>
            <div className="card-body">
                <div className="list-toolbar">
                    <Search placeholder="Id或者DCN名称" onSearch={this.onSearch} enterButton
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
                    <Button type="primary" onClick={() => this.edit(0)}>添加配置</Button>
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


export default withRouter(DcnConfigList);
