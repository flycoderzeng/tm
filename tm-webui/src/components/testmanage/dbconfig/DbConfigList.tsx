import CommonListPage from "../../common/CommonListPage";
import {withRouter} from "react-router-dom";
import {CommonApiUrlModel} from "../../../entities/CommonApiUrlModel";
import {ApiUrlConfig} from "../../../config/api.url";
import {Button, Input, message, Modal, Radio, Table} from "antd";
import {OptionsConfig} from "../../../config/options.config";
import React from "react";
import {CommonBatchCopyConfig} from "../../common/components/CommonBatchCopyConfig";
import axios from "axios";

const { Search } = Input;

class DbConfigList extends CommonListPage {
    batchCopyConfigValues = {srcEnvId: null, srcDcnId: null, desEnvId: null, ip: '', port: '', schemaName: ''};
    constructor(props) {
        super(props, 'DbConfigList');
        const commonApiUrlModel: CommonApiUrlModel = {
            listUrl: ApiUrlConfig.QUERY_DB_CONFIG_LIST_URL,
            saveUrl: ApiUrlConfig.SAVE_DB_CONFIG_URL,
            deleteUrl: ApiUrlConfig.DELETE_DB_CONFIG_URL,
            loadUrl: ApiUrlConfig.LOAD_DB_CONFIG_URL,
            editUrl: '/dbconfigedit/:id'
        };
        this.commonApiUrlModel = commonApiUrlModel;
        this.modelType = 'db_config';
    }

    componentDidMount() {
        this.loadDataList(this.state.pagination);
    }

    onSearch = (value) => {
        let filterConditionList =
            [{"columnName": "db_name", "value":value},
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
        this.updateCommonState(this.constructor.name, filters, sorter);
    }

    onChangeBatchCopyConfigValues = (key, value) => {
        this.batchCopyConfigValues[key] = value;
    }

    handleOk = () => {
        if(!window.confirm('确定复制配置吗?')) {
            return;
        }
        axios.post(ApiUrlConfig.BATCH_COPY_DB_CONFIG_URL, this.batchCopyConfigValues).then(resp => {
            if (resp.status !== 200) {
                message.error('操作失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    this.loadDataList(this.state.pagination);
                }
            }
        });
        this.handleCancel();
    }

    handleCancel = () => {
        this.setState({isModalVisible: false});
    }

    openModal = () => {
        this.setState({isModalVisible: true});
    }

    render() {
        const columns: any[] = [
            {
                title: 'ID',
                dataIndex: 'id',
                render: text => <span>{text}</span>,
            },{
                title: '数据库名',
                dataIndex: 'dbName',
                key: 'db_name',
                sorter: ()=>{},
                render: (text, record) => <Button
                    className="padding-left0"
                    size="small"
                    type="link"
                    onClick={() => this.edit(record.id)}>{text}</Button>,
            },{
                title: 'schema',
                dataIndex: 'schemaName',
                sorter: ()=>{},
                render: text => <span>{text}</span>,
            },{
                title: '数据库类型',
                dataIndex: 'type',
                render: text => {
                    if(text === 1)
                        return <span>{'MySQL'}</span>
                    if(text === 2)
                        return <span>{'Postgresql'}</span>
                    if(text === 3)
                        return <span>{'达梦'}</span>
                },
            },{
                title: 'ip',
                dataIndex: 'ip',
                sorter: ()=>{},
                render: text => <span>{text}</span>,
            },{
                title: 'port',
                dataIndex: 'port',
                render: text => <span>{text}</span>,
            },{
                title: '数据库用户名',
                dataIndex: 'username',
                render: text => <span>{text}</span>,
            },{
                title: '环境',
                dataIndex: 'envName',
                render: text => <span>{text}</span>,
            },{
                title: '分布式节点名称',
                dataIndex: 'dcnName',
                render: text => <span>{text}</span>,
            },{
                title: '操作',
                fixed: 'right',
                filteredValue: this.state.filteredValue,
                filters: this.columnFilters,
                onFilter: (value, record) => this.onFilter(value,record),
                render: (text, record) => (
                    <div>
                        <Button className="padding-left0" size="small" type="link" onClick={() => this.edit(record.id)}>修改</Button>
                        <Button className="padding-left0" size="small" type="link" onClick={() => this.copy(record.id)}>复制</Button>
                        <Button danger size="small" type="link" onClick={() => this.delete(record.id)}>删除</Button>
                    </div>
                ),
            },
        ];
        const {area} = this.state.queryInfo;
        this.showCommonColumns(columns);

        return (<div className="card">
            <div className="card-header card-header-divider">数据库配置
                <span className="card-subtitle">数据库ip、端口配置</span>
            </div>
            <div className="card-body">
                <div className="list-toolbar">
                    <Search placeholder="Id或者db名称" onSearch={this.onSearch} enterButton
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
                    <Button type="primary" onClick={() => this.edit(0)} style={{ marginRight: '5px'}}>添加配置</Button>
                    <Button type="primary" onClick={() => this.openModal()}>复制配置到新环境</Button>
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

            <Modal title="复制数据库配置到新环境" open={this.state.isModalVisible}
                   onOk={this.handleOk}
                   onCancel={this.handleCancel}>
                <CommonBatchCopyConfig type={'db'} onChange={this.onChangeBatchCopyConfigValues}></CommonBatchCopyConfig>
            </Modal>
        </div>)
    }
}


export default withRouter(DbConfigList);
