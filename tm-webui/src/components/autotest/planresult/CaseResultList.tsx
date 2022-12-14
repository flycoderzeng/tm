import React, {useEffect, useState} from "react";
import {PlanResultStatusUtils} from "../../../utils/PlanResultStatusUtils";
import {Button, message, Modal, Space, Table, Tag} from "antd";
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {DateUtils} from "../../../utils/DateUtils";
import {CaseResultStatusEnum} from "../../../entities/CaseResultStatusEnum";
import {VariableResultList} from "./VariableResultList";
import copy from "copy-to-clipboard";

interface IState {
    planResultId: number|null|undefined;
    viewCaseResult: any
}

const CaseResultList: React.FC<IState> = (props) => {
    const [planResultId, setPlanResultId] = useState(props.planResultId);
    const [rows, setRows] = useState<any[]>([]);
    const [loading, setLoading] = useState(false);
    const [total, setTotal] = useState(0);
    const [visible, setVisible] = useState(false);
    const [caseId, setCaseId] = useState(-1);
    const [groupNo, setGroupNo] = useState(-1);
    const [resultInfo, setResultInfo] = useState('');
    const [isModalVisible, setIsModalVisible] = useState(false);

    const [pagination, setPagination] = useState({
        current: 1,
        pageNum: 1,
        pageSize: 10,
        total: 0,
    });

    if(planResultId !== props.planResultId) {
        setPlanResultId(props.planResultId);
    }
    useEffect(() => {
        load();
    }, [planResultId, pagination.pageNum, pagination.pageSize]);

    function renderRows(data: any) {
        if (!data) {
            setRows([]);
            pagination.total = 0;
            setPagination(pagination);
            return;
        }
        pagination.total = data.total || 0;
        setPagination(pagination);
        data.rows?.map((r: any) => {
            r.key = r.id;
            if(r.endTimestamp) {
                r.costTime = (r.endTimestamp - r.startTimestamp) / 1000 + '';
            }else{
                r.costTime = '';
            }
            r.startTimestamp = DateUtils.format(r.startTimestamp);
            r.endTimestamp = DateUtils.format(r.endTimestamp);
            return null;
        });
        setRows(data.rows || []);
        setTotal(data.total);
    }

    function load() {
        if(!planResultId) {
            return ;
        }
        setLoading(true);
        axios.post(ApiUrlConfig.GET_PLAN_CASE_EXECUTE_RESULT_LIST_URL, {planResultId: planResultId,
            pageNum: pagination.pageNum,
            pageSize: pagination.pageSize}).then(resp => {
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

    function onClickViewCaseResult(record: any) {
        props.viewCaseResult(record);
    }

    function refresh() {
        load();
    }

    function onChangePagination(pagination) {
        setPagination({
            ...pagination,
            pageNum: pagination.current,
            pageSize: pagination.pageSize,
            current: pagination.current,
        });
    }

    function renderResultStatus(record: any) {
        const status = PlanResultStatusUtils.getCaseResultStatusDescription(record.resultStatus);
        if(record.resultStatus === CaseResultStatusEnum.FAIL) {
            return <Tag color="#f50">{status}</Tag>
        }else{
            return <Tag color="#87d068">{status}</Tag>
        }
    }

    function viewVariableResult(record: any) {
        setCaseId(record.caseId);
        setGroupNo(record.groupNo);
        setVisible(true);
    }

    function showResultInfo(record: any) {
        if(!record.resultInfo) {
            return ;
        }
        setResultInfo(record.resultInfo);
        setIsModalVisible(true);
    }

    function onCopyResultInfo() {
        if(copy(resultInfo)) {
            message.success('????????????');
        }else{
            message.error('????????????');
        }
    }

    const columns: any[] = [
        {
            width: 150,
            title: '??????id',
            dataIndex: 'caseId',
            key: 'id',
            render: (text, record) => <a onClick={() => {onClickViewCaseResult(record);}}>{text}</a>,
        },
        {
            ellipsis: true,
            title: '??????',
            dataIndex: 'name',
            key: 'id',
        },
        {
            width: 100,
            title: '????????????',
            dataIndex: 'groupNo',
            key: 'id',
        },
        {
            ellipsis: true,
            title: '????????????',
            dataIndex: 'groupName',
            key: 'id',
        },
        {
            width: 100,
            title: '??????',
            dataIndex: 'resultStatus',
            key: 'id',
            render: (text, record) => <span>{renderResultStatus(record)}</span>,
        },
        {
            width: 150,
            title: '????????????',
            dataIndex: 'startTimestamp',
            key: 'id',
        },
        {
            width: 150,
            title: '????????????',
            dataIndex: 'endTimestamp',
            key: 'id',
        },
        {
            width: 150,
            title: '??????(s)',
            dataIndex: 'costTime',
            key: 'id',
        },
        {
            ellipsis: true,
            title: '????????????',
            dataIndex: 'resultInfo',
            key: 'id',
            render: (text, record) => <span onClick={()=>{showResultInfo(record)}}>{text}</span>,
        },
        {
            width: 150,
            title: '?????????IP',
            dataIndex: 'workerIp',
            key: 'id',
        },
        {
            fixed: 'right',
            title: '??????',
            key: 'action',
            render: (text, record) => (
                <Space size="middle">
                    <a onClick={() => {viewVariableResult(record)}}>????????????</a>
                    <a>??????</a>
                </Space>
            ),
        },
    ];

    return (<div>
        <div style={{paddingBottom: '5px'}}>
            <Button type="primary" onClick={()=>{refresh();}} >??????</Button>
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
        <Modal
            title="??????????????????"
            open={visible}
            onOk={() => setVisible(false)}
            onCancel={() => setVisible(false)}
            width={1100}
            footer={null}
        >
            <div>
                <VariableResultList planResultId={planResultId} caseId={caseId} groupNo={groupNo}></VariableResultList>
            </div>
        </Modal>

        <Modal
            title="????????????"
            onOk={() => setIsModalVisible(false)}
            onCancel={() => setIsModalVisible(false)}
            open={isModalVisible}
            footer={null}
            width={1100}>
            <div style={{paddingBottom: 5}}>
                <Button size="small" type="primary" onClick={() => {
                    onCopyResultInfo();
                }}>??????????????????</Button>
            </div>
            <pre style={{maxHeight: 'calc(100vh - 300px)'}}>{resultInfo}</pre>
        </Modal>
    </div>)
}

export {CaseResultList}
