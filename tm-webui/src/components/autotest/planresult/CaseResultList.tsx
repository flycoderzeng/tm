import React, {useEffect, useState} from "react";
import {PlanResultStatusUtils} from "../../../utils/PlanResultStatusUtils";
import {Button, Input, message, Modal, Space, Table, Tag} from "antd";
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {DateUtils} from "../../../utils/DateUtils";
import {CaseResultStatusEnum} from "../../../entities/CaseResultStatusEnum";
import {VariableResultList} from "./VariableResultList";
import copy from "copy-to-clipboard";
const { Search } = Input;
interface IState {
    planResultId: number|null|undefined;
    viewCaseResult: any
}
let searchValue;

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
        pageSize: 20,
        total: 0,
    });

    useEffect(() => {
        setPlanResultId(props.planResultId);
        load(props.planResultId);
    }, [props.planResultId]);

    function renderRows(data: any, newPagination?: any) {
        if(!newPagination) {
            newPagination = pagination;
        }
        if (!data) {
            setRows([]);
            setPagination({
                ...newPagination,
                total: 0
            });
            return;
        }
        setPagination({
            ...newPagination,
            total: data.total || 0
        });

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

    function load(currentPlanResultId, newPagination?: any) {
        if(!newPagination) {
            newPagination = pagination;
        }
        let filterConditionList = getFilterConditionList(searchValue);
        if(!currentPlanResultId) {
            return ;
        }
        if(!filterConditionList) {
            filterConditionList = [];
        }
        setLoading(true);
        axios.post(ApiUrlConfig.GET_PLAN_CASE_EXECUTE_RESULT_LIST_URL, {
            planResultId: currentPlanResultId,
            pageNum: newPagination.pageNum,
            linkOperator: 'or',
            pageSize: newPagination.pageSize, filterConditionList: filterConditionList}).then(resp => {
            if (resp.status !== 200) {
                message.error('加载失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    renderRows(ret.data, newPagination);
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
        load(planResultId);
    }

    function onChangePagination(pagination2) {
        const newPagination = {
            ...pagination,
            pageNum: pagination2.current,
            pageSize: pagination2.pageSize,
            current: pagination2.current,
        };
        load(planResultId, newPagination);
    }

    function renderResultStatus(record: any) {
        const status = PlanResultStatusUtils.getCaseResultStatusDescription(record.resultStatus);
        if(record.resultStatus === CaseResultStatusEnum.FAIL) {
            return <Tag color="#f50">{status}</Tag>
        }else if(record.resultStatus === CaseResultStatusEnum.RUNNING) {
            return <Tag color="#108ee9">{status}</Tag>
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
            message.success('复制成功');
        }else{
            message.error('复制失败');
        }
    }

    function getFilterConditionList(value) {
        let filterConditionList =
            [
                {"columnName": "name", "value": value},
                {"columnName": "case_id", "operator": "=", "value": value}];
        if (!value) {
            filterConditionList = [];
        }
        return filterConditionList;
    }

    function onSearch(value) {
        searchValue = value;
        load(planResultId);
    }

    const columns: any[] = [
        {
            width: 150,
            title: '用例id',
            dataIndex: 'caseId',
            key: 'id',
            render: (text, record) => <a onClick={() => {onClickViewCaseResult(record);}}>{text}</a>,
        },
        {
            ellipsis: true,
            title: '名称',
            dataIndex: 'name',
            key: 'id',
        },
        {
            width: 100,
            title: '组合编号',
            dataIndex: 'groupNo',
            key: 'id',
        },
        {
            ellipsis: true,
            title: '组合描述',
            dataIndex: 'groupName',
            key: 'id',
        },
        {
            width: 100,
            title: '状态',
            dataIndex: 'resultStatus',
            key: 'id',
            render: (text, record) => <span>{renderResultStatus(record)}</span>,
        },
        {
            width: 150,
            title: '开始时间',
            dataIndex: 'startTimestamp',
            key: 'id',
        },
        {
            width: 150,
            title: '结束时间',
            dataIndex: 'endTimestamp',
            key: 'id',
        },
        {
            width: 150,
            title: '耗时(s)',
            dataIndex: 'costTime',
            key: 'id',
        },
        {
            ellipsis: true,
            title: '结果信息',
            dataIndex: 'resultInfo',
            key: 'id',
            render: (text, record) => <span onClick={()=>{showResultInfo(record)}}>{text}</span>,
        },
        {
            width: 150,
            title: '执行机IP',
            dataIndex: 'workerIp',
            key: 'id',
        },
        {
            fixed: 'right',
            title: '操作',
            key: 'action',
            render: (text, record) => (
                <Space size="middle">
                    <a onClick={() => {viewVariableResult(record)}}>查看变量</a>
                    <a>编辑</a>
                </Space>
            ),
        },
    ];

    return (<div>
        <div className="list-toolbar" style={{paddingBottom: '5px'}}>
            <Search placeholder="Id或者名称" onSearch={onSearch} enterButton style={{ width: 400,}}/>
            <Button type="primary" onClick={()=>{refresh();}} >刷新</Button>
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
        <Modal
            title="用例变量结果"
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
            title="结果信息"
            onOk={() => setIsModalVisible(false)}
            onCancel={() => setIsModalVisible(false)}
            open={isModalVisible}
            footer={null}
            width={1100}>
            <div style={{paddingBottom: 5}}>
                <Button size="small" type="primary" onClick={() => {
                    onCopyResultInfo();
                }}>复制结果信息</Button>
            </div>
            <pre style={{maxHeight: 'calc(100vh - 300px)'}}>{resultInfo}</pre>
        </Modal>
    </div>)
}

export {CaseResultList}
