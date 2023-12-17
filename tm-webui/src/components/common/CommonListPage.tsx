import React from "react";
import axios from "axios";
import {message} from "antd";
import { RouteComponentProps } from "react-router-dom";
import {CommonApiUrlModel} from "../../entities/CommonApiUrlModel";
import {DateUtils} from "../../utils/DateUtils";
import {LocalStorageUtils} from "../../utils/LocalStorageUtils";
import {StrUtils} from "../../utils/StrUtils";
import {MathUtils} from "../../utils/MathUtils";

interface IProps {}

type CommonProps = IProps & RouteComponentProps;

interface IState {
    data: any[];
    loading: boolean;
    isModalVisible: boolean;
    pagination: any;
    sortedInfo: any;
    queryInfo: {
        area: string | number | null | undefined;
        searchValue: string | null | undefined;
    },
    filteredValue: string[];
    rows?: any[];
}

class CommonListPage extends React.Component<CommonProps, IState> {
    commonApiUrlModel: CommonApiUrlModel;
    modelType: string;
    columnFilters: any[];
    constructor(props: IState, className?: string) {
        super(props);
        this.state = {
            data: [],
            loading: false,
            isModalVisible: false,
            pagination: {
                current: 1,
                pageNum: 1,
                pageSize: 20,
                linkOperator: 'or',
                total: 0,
                filterConditionList: []
            },
            sortedInfo: {},
            queryInfo: {
                area: '1',
                searchValue: ''
            },
            filteredValue: [...LocalStorageUtils.getFilteredValue(className)]
        };
        this.commonApiUrlModel = {
            saveUrl: '',
            deleteUrl: '',
            listUrl: '',
            loadUrl: '',
            editUrl: ''
        };
        this.columnFilters = [
            {text: '描述', value: 'description'},
            {text: '创建者', value: 'addUser'},
            {text: '创建时间', value: 'addTime'},
            {text: '最后修改者', value: 'lastModifyUser'},
            {text: '最后修改时间', value: 'lastModifyTime'}
        ];
        this.modelType = '';
    }

    onFilter = (value, record) => {
        return true;
    }

    getFilteredValues(filters): string[] {
        if(filters && !filters.length) {
            const keys = Object.keys(filters);
            if(keys.length) {
                const filteredValue = filters[keys[0]];
                return filteredValue;
            }
        }
        return this.state.filteredValue;
    }

    isShowColumn (columnName: string) {
        if(this.state.filteredValue && !this.state.filteredValue.length) {
            const keys = Object.keys(this.state.filteredValue);
            if(keys.length) {
                const filteredValue = this.state.filteredValue[keys[0]];
                if(filteredValue && filteredValue.indexOf(columnName) > -1) {
                    return true;
                }
            }
        }else if(this.state.filteredValue && this.state.filteredValue.length) {
            if(this.state.filteredValue.indexOf(columnName) > -1) {
                return true;
            }
        }
        return false;
    }

    showCommonColumns(columns: any[]) {
        if(this.isShowColumn("description")) {
            columns.splice(2, 0,{
                title: '描述',
                dataIndex: 'description',
                key: 'description',
                ellipsis: true,
                render: text => <span>{text}</span>,
            });
        }
        if(this.isShowColumn("addUser")) {
            columns.splice(-1, 0,{
                title: '创建者',
                dataIndex: 'addUser',
                key: 'addUser',
                sorter: ()=>{},
                render: text => <span>{text}</span>,
            });
        }
        if(this.isShowColumn('addTime')) {
            columns.splice(-1, 0, {
                title: '创建时间',
                dataIndex: 'addTime',
                key: 'addTime',
                sorter: ()=>{},
                render: text => <span>{text}</span>,
            });
        }
        if(this.isShowColumn('lastModifyUser')) {
            columns.splice(-1, 0, {
                title: '最后修改者',
                dataIndex: 'lastModifyUser',
                key: 'lastModifyUser',
                sorter: ()=>{},
                render: text => <span>{text}</span>,
            });
        }
        if(this.isShowColumn('lastModifyTime')) {
            columns.splice(-1, 0, {
                title: '最后修改时间',
                dataIndex: 'lastModifyTime',
                key: 'lastModifyTime',
                sorter: ()=>{},
                render: text => <span>{text}</span>,
            });
        }
    }

    _onSearch = (queryInfo, filterConditionList) => {
        if(!filterConditionList) {
            filterConditionList =
                [{"columnName": "name",
                    "value": queryInfo.searchValue||this.state.queryInfo.searchValue},
                    {"columnName": "id", "operator": "=",
                        "value": queryInfo.searchValue||this.state.queryInfo.searchValue}];
        }
        if(!queryInfo.searchValue) {
            filterConditionList = [];
        }

        const data: any = {
            pageNum: 1,
            pageSize: this.state.pagination.pageSize,
            current: 1,
            linkOperator: this.state.pagination.linkOperator,
            filterConditionList: filterConditionList,
            area: queryInfo.area || this.state.queryInfo.area,
            searchValue: queryInfo.searchValue || this.state.queryInfo.searchValue
        };
        if(this.state.sortedInfo.columnKey) {
            data.order = this.state.sortedInfo.columnKey;
        }
        if(this.state.sortedInfo.order) {
            data.sort = this.state.sortedInfo.order;
        }
        this.loadDataListSort(data, null, this.state.sortedInfo);
    }

    loadDataListSort = (pagination, filters, sorter, reset ?: boolean) => {
        if(!pagination) {
            pagination = this.state.pagination;
        }
        if(sorter?.columnKey) {
            pagination.order = sorter.columnKey;
        }
        if(sorter?.order) {
            pagination.sort = sorter.order;
        }
        this.loadDataList(pagination, reset);
    }

    _onChangeQueryArea = (e) => {
        this.setState({queryInfo: {
                ...this.state.queryInfo,
                area: e.target.value
            }});
    }

    updateCommonState(className, filters, sorter) {
        const filteredValue = this.getFilteredValues(filters);
        LocalStorageUtils.saveFilteredValue(className, filteredValue);
        this.setState({sortedInfo: sorter, filteredValue: filteredValue});
    }

    onChangeSearchValue = (e) => {
        this.setState({queryInfo: {
                ...this.state.queryInfo,
                searchValue: e.target.value
            }});
    }

    loadDataList = (pagination, reset ?: boolean) => {
        if(!pagination) {
            pagination = this.state.pagination;
        }
        this.setPaginationState(pagination, reset);

        if(this.commonApiUrlModel.listUrl.startsWith("/lc/lc/json/api/")) {
            this.loadDataList1(pagination);
        } else {
            this.loadDataList2(pagination);
        }
    }

    private loadDataList1(pagination) {
        const data: any = {
            'page[totals]': '',
            'page[number]': pagination.current,
            'page[size]': pagination.pageSize
        }
        let sort = '';
        if(pagination.order) {
            sort += pagination.order;
        }
        if(!sort) {
            sort = 'id';
        }
        if(!pagination.sort || pagination.sort === 'descend') {
            sort = '-' + sort;
        }
        data['sort'] = sort;

        let filter = 'status==0';
        if(this.modelType === 'plan_cron_job') {
            filter = '';
        }
        if(pagination.area && pagination.area === '1') {
            if(filter !== '') {
                filter += ';';
            }
            filter += 'addUser==' + LocalStorageUtils.getLoginUsername();
        }else if(!pagination.area && this.state.queryInfo.area === '1') {
            if(filter !== '') {
                filter += ';';
            }
            filter += 'addUser==' + LocalStorageUtils.getLoginUsername();
        }
        if(pagination.searchValue) {
            if(filter !== '') {
                filter += ';';
            }
            filter += '(';
            if(MathUtils.isNumberSequence(pagination.searchValue)) {
                filter += 'id==' + pagination.searchValue + ',';
            }
            if(this.modelType === 'db_config') {
                filter += 'dbName==*' + pagination.searchValue + '*';
            }else if(this.modelType === 'menu') {
                filter += 'menuName==*' + pagination.searchValue + '*';
            }else {
                filter += 'name==*' + pagination.searchValue + '*';
            }
            if(this.modelType === 'api_ip_port_config') {
                filter += ',url==*' + pagination.searchValue + '*';
            }
            filter += ')';
        }
        if(filter !== '') {
            data['filter[' + this.modelType + ']'] = filter;
        }

        if(this.modelType === 'db_config') {
            data['fields[db_config]'] = 'addTime,addUser,lastModifyTime,lastModifyUser,ip,port,username,status,type,envId,envName,schemaName,dbName,dcnId,dcnName';
        }

        if(this.modelType === 'api_ip_port_config') {
            data['fields[api_ip_port_config]'] = 'addTime,addUser,lastModifyTime,lastModifyUser,name,url,ip,port,envId,envName,dcnId,dcnName';
        }
        axios.get(this.commonApiUrlModel.listUrl + '?' + StrUtils.getGetParams(data)).then(resp => {
            if (resp.status !== 200) {
                message.error('加载列表失败');
            } else {
                const ret = resp.data;
                const rows: any[] = [];
                if(ret.data) {
                    ret.data.map((v: any) => {
                        v.attributes.id = v.id;
                        v.attributes.key = v.id;
                        rows.push(v.attributes);
                    });
                }
                let total: number = 0;
                if(ret.meta && ret.meta.page) {
                    total = ret.meta.page.totalRecords;
                }
                this.setState({
                    data: rows,
                    pagination: {
                        ...this.state.pagination,
                        total: total
                    }
                });
            }
        }).finally(() => {
            this.setState({loading: false});
        });
    }


    private loadDataList2(pagination) {
        const data: any = {
            pageNum: pagination.current,
            pageSize: pagination.pageSize,
            linkOperator: pagination.linkOperator || this.state.pagination.linkOperator,
            filterConditionList: pagination.filterConditionList || this.state.pagination.filterConditionList,
            order: pagination.order,
            sort: pagination.sort,
            area: pagination.area || this.state.queryInfo.area
        };
        axios.post(this.commonApiUrlModel.listUrl, data).then(resp => {
            if (resp.status !== 200) {
                message.error('加载列表失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    ret.data.rows.map(v => {
                        v.key = v.id;
                        v.addTime = v.addTime && DateUtils.format(v.addTime);
                        v.lastModifyTime = v.lastModifyTime && DateUtils.format(v.lastModifyTime);
                        return null;
                    });
                    this.setState({
                        data: ret.data.rows,
                        pagination: {
                            ...this.state.pagination,
                            total: ret.data.total
                        }
                    });
                }
            }
        }).finally(() => {
            this.setState({loading: false});
        });
    }

    private setPaginationState(pagination, reset: boolean | undefined) {
        if(reset) {
            pagination.current = 1;
        }
        this.setState({
            loading: true, pagination: {
                ...this.state.pagination,
                pageNum: pagination.current,
                pageSize: pagination.pageSize,
                current: pagination.current,
            },
        });
    }

    delete = id => {
        let msg = '确定删除吗?';
        if(this.modelType === 'plan_cron_job') {
            msg = '确定禁用吗?'
        }
        if(!window.confirm(msg)) {
            return;
        }
        const data = {
            "data": {
                "type": this.modelType,
                "id": id,
                "attributes": {
                    "status": 1
                }
            }
        };
        axios.patch(this.commonApiUrlModel.deleteUrl + id, data,
            {headers: {"Content-Type": "application/vnd.api+json"}}).then(resp => {
            if (resp.status !== 204) {
                message.error('操作失败');
            } else {
                message.success('操作成功');
                this.loadDataListSort(null, null, null, true);
            }
        });
    }

    edit(id) {
        this.props.history.push(this.commonApiUrlModel.editUrl.replace(':id', id));
    }

    render() {
        return (<div></div>);
    }
}

export default CommonListPage;
