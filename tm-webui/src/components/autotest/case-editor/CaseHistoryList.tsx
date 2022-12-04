import React, {useEffect, useState} from "react";
import {Button, message, Space, Table} from "antd";
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {StrUtils} from "../../../utils/StrUtils";

interface IState {
    caseId: number|null|undefined;
    saveAutoCase?: any;
}

const CaseHistoryList: React.FC<IState> = (props) => {
    const [caseId, setCaseId] = useState(props.caseId);
    const [total, setTotal] = useState(0);
    const [loading, setLoading] = useState(false);
    const [rows, setRows] = useState<any[]>([]);
    const {saveAutoCase} = props;

    const [pagination, setPagination] = useState({
        current: 1,
        pageNum: 1,
        pageSize: 10,
        total: 0,
    });

    if(caseId !== props.caseId) {
        setCaseId(props.caseId);
    }

    useEffect(() => {
        load();
    }, [caseId]);

    function load() {
        setLoading(true);
        const data: any = {
            'page[totals]': '',
            'page[number]': pagination.current,
            'page[size]': pagination.pageSize,
            'sort': '-id',
            'filter[auto_case_history]': 'autoCaseId==' + caseId,
        }

        axios.get(ApiUrlConfig.QUERY_CASE_HISTORY_LIST_URL + '?' + StrUtils.getGetParams(data),).then(resp => {
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
                setRows(rows);
                setTotal(total);
                setPagination({
                    ...pagination,
                    total: total
                });
            }
        }).finally(() => {
            setLoading(false);
        });
    }

    function rollback(record: any) {
        if(!window.confirm('确认恢复吗？')) {
            return;
        }
        if(saveAutoCase) {
            saveAutoCase(record.steps, record.groupVariables, true);
        }
    }

    function onChangePagination(pagination) {
        setPagination({
            ...pagination,
            pageNum: pagination.current,
            pageSize: pagination.pageSize,
            current: pagination.current,
        });
    }

    const columns: any[] = [{
        ellipsis: true,
        title: '操作用户',
        dataIndex: 'addUser',
        key: 'id',
    },{
        ellipsis: true,
        title: '操作时间',
        dataIndex: 'addTime',
        key: 'id',
    }, {
        fixed: 'right',
        title: '操作',
        width: 120,
        key: 'action',
        render: (text, record) => (
            <Space size="middle">
                <a onClick={() => {rollback(record)}}>恢复</a>
            </Space>
        ),
    },];

    function refresh() {
        load();
    }

    return (<div>
        <div style={{paddingBottom: '5px'}}>
            <Button size={'small'} type="primary" onClick={()=>{refresh();}}>刷新</Button>
        </div>
        <Table
            footer={() => '共' + total + '条数据'}
            tableLayout="fixed"
            size="small"
            pagination={pagination}
            onChange={onChangePagination}
            columns={columns}
            dataSource={rows}
            loading={loading}/>
    </div>);
}

export {CaseHistoryList};
