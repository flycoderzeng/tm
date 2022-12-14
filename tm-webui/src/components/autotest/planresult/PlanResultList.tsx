import React, {useEffect, useState} from "react";
import {Button, message, Table, Tag} from "antd";
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {DateUtils} from "../../../utils/DateUtils";
import {PlanResultStatusUtils} from "../../../utils/PlanResultStatusUtils";
interface IState {
    planOrCaseId: number|null|undefined;
    fromType: number|null|undefined;
}

const PlanResultList: React.FC<IState> = (props) => {
    const [planOrCaseId, setPlanOrCaseId] = useState(props.planOrCaseId);
    const [fromType, setFromType] = useState(props.fromType);
    const [rows, setRows] = useState<any[]>([]);
    const [loading, setLoading] = useState(false);
    const [total, setTotal] = useState(0);
    const [pagination, setPagination] = useState({
        current: 1,
        pageNum: 1,
        pageSize: 10,
        total: 0,
    });
    if(planOrCaseId !== props.planOrCaseId) {
        setPlanOrCaseId(props.planOrCaseId);
    }
    if(fromType !== props.fromType) {
        setFromType(props.fromType);
    }

    useEffect(() => {
        load();
    }, [planOrCaseId, fromType, pagination.pageSize, pagination.pageNum]);

    function renderRows(data: any) {
        if(!data) {
            setRows([]);
            pagination.total = 0;
            setPagination(pagination);
            return;
        }
        pagination.total = data.total || 0;
        setPagination(pagination);
        data.rows?.map((r: any) => {
            r.key = r.id;
            r.submitTimestamp = DateUtils.format(r.submitTimestamp);
            r.completeInfo = r.total + "/" + r.successCount + "/" + r.failCount;
            r.runTimeInfo = DateUtils.format(r.startTimestamp) + " ~ " + DateUtils.format(r.endTimestamp);
            if(r.endTimestamp) {
                r.costTime = (r.endTimestamp - r.startTimestamp) / 1000 + '';
            }else{
                r.costTime = '';
            }
            if(r.total) {
                r.successRate = (r.successCount * 100 / r.total).toFixed(2) + '%';
            }else{
                r.successRate = '0%';
            }
            if(r.failCount > 0) {
                r.successRateColor = '#f50';
            }else{
                r.successRateColor = '#87d068';
            }
            return null;
        });
        setRows(data.rows || []);
        setTotal(data.total);
    }

    function onChangePagination(pagination) {
        setPagination({
            ...pagination,
            pageNum: pagination.current,
            pageSize: pagination.pageSize,
            current: pagination.current,
        });
    }

    function load() {
        if(!planOrCaseId || !fromType) {
            return ;
        }
        setLoading(true);
        axios.post(ApiUrlConfig.GET_PLAN_HISTORY_EXECUTE_RESULT_LIST_URL,
            {planOrCaseId: planOrCaseId, fromType: fromType, pageNum: pagination.pageNum,
                pageSize: pagination.pageSize, order: "id", sort: "desc"}).then(resp => {
            if (resp.status !== 200) {
                message.error('????????????');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    renderRows(ret.data);
                }
            }
        }).finally(() => {
            setLoading(false);
        });
    }

    function refresh() {
        load();
    }

    function viewPlanResult(record: any) {
        window.open("/planresult/" + record.planOrCaseId + "/" + record.fromType + "/" + record.id);
    }

    const columns: any[] = [
        {
            title: '??????id',
            dataIndex: 'id',
            key: 'id',
            render: (text, record) => <a onClick={() => {viewPlanResult(record);}}>{text}</a>,
        },
        {
            ellipsis: true,
            title: '??????',
            dataIndex: 'planOrCaseName',
            key: 'planOrCaseName',
        },
        {
            title: '??????',
            dataIndex: 'resultStatus',
            key: 'resultStatus',
            render: text => <span>{PlanResultStatusUtils.getPlanResultStatusDescription(text)}</span>,
        },
        {
            title: '?????????',
            dataIndex: 'submitter',
            key: 'submitter',
        },
        {
            title: '????????????',
            dataIndex: 'submitTimestamp',
            key: 'submitTimestamp',
        },
        {
            title: '??????/??????/??????/?????????',
            dataIndex: 'completeInfo',
            key: 'completeInfo',
            render: (text, record) => <span><span>{text}</span><Tag style={{marginLeft: '5px'}} color={record.successRateColor}>{record.successRate}</Tag></span>
        },
        {
            width: 280,
            title: '??????????????????',
            dataIndex: 'runTimeInfo',
            key: 'runTimeInfo',
        },
        {
            title: '??????(s)',
            dataIndex: 'costTime',
            key: 'costTime',
        },
    ];
    return (<div>
        <div style={{paddingBottom: '5px'}}>
            <Button type="primary" onClick={()=>{refresh();}}>??????</Button>
        </div>
        <Table
            footer={() => '???' + total + '?????????'}
            tableLayout="fixed"
            size="small"
            pagination={pagination}
            onChange={onChangePagination}
            columns={columns}
            dataSource={rows}
            loading={loading}/>
    </div>)
}
export {PlanResultList}
