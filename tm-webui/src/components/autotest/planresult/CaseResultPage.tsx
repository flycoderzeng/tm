import React, {useEffect, useState} from "react";
import {message, Modal, Space, Table, Tag, Tree} from "antd";
import { CloseCircleTwoTone } from '@ant-design/icons';
import {TreeUtils} from "../../../utils/TreeUtils";
import {PlanResultStatusUtils} from "../../../utils/PlanResultStatusUtils";
import {CaseResultStatusEnum} from "../../../entities/CaseResultStatusEnum";
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {DateUtils} from "../../../utils/DateUtils";
import {StepNode} from "../case-editor/entities/StepNode";
import {StepResultPage} from "./StepResultPage";

interface IState {
    caseResult: any
}

const CaseResultPage: React.FC<IState> = (props) => {
    const [stepsStr, setStepsStr] = useState(props.caseResult.steps);
    const [errorStepKey, setErrorStepKey] = useState(props.caseResult.errorStepKey);
    const [stepResult, setStepResult] = useState<any>(null);
    const [steps, setSteps] = useState<any[]>([]);
    const [expandedKeys, setExpandedKeys] = useState<string[]>([]);
    const [currStepNode, setCurrStepNode] = useState<StepNode|null>(null);
    const [visible, setVisible] = useState(false);
    const [total, setTotal] = useState(0);
    const [pagination, setPagination] = useState({
        current: 1,
        pageNum: 1,
        pageSize: 10,
        total: 0,
    });
    const [rows, setRows] = useState<any[]>([]);
    const [loading, setLoading] = useState(false);
    const [stepKey, setStepKey] = useState('');

    if(stepsStr !== props.caseResult.steps) {
        setStepsStr(props.caseResult.steps);
    }

    if(errorStepKey !== props.caseResult.errorStepKey) {
        setErrorStepKey(props.caseResult.errorStepKey);
    }

    useEffect(() => {
        changeSteps();
    }, [stepsStr, stepKey]);

    useEffect(() => {
        load();
    }, [pagination.pageSize, pagination.pageNum]);

    function changeSteps() {
        try {
            let parse: any[] = JSON.parse(stepsStr);
            setSteps(parse);
            setExpandedKeys(TreeUtils.getAllNonLeafKeys(parse));
            load();
        } catch (e) {
            message.error('解析用例结构失败');
        }
    }

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
        const tempRows = data.rows || [];
        setRows(tempRows);
        setTotal(data.total);
        if(tempRows.length === 1) {
            setStepResult(tempRows[0]);
        }else if(tempRows.length === 0) {
            setStepResult(null);
        }
    }

    function load() {
        setLoading(true);
        axios.post(ApiUrlConfig.GET_CASE_STEP_RESULT_LIST_URL, {
            planResultId: props.caseResult.planResultId,
            caseId: props.caseResult.caseId,
            groupNo: props.caseResult.groupNo,
            stepKey: stepKey,
            pageNum: pagination.pageNum,
            pageSize: pagination.pageSize,
        }).then(resp => {
            if (resp.status !== 200) {
                message.error('加载步骤结果列表失败');
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

    function onChangePagination(pagination) {
        setPagination({
            ...pagination,
            pageNum: pagination.current,
            pageSize: pagination.pageSize,
            current: pagination.current,
        });
    }

    function viewStepResult(record: any) {
        setStepResult(record);
        setVisible(true);
    }

    function renderResultStatus(record: any) {
        const status = PlanResultStatusUtils.getCaseResultStatusDescription(record.resultStatus);
        if(record.resultStatus === CaseResultStatusEnum.FAIL) {
            return <Tag color="#f50">{status}</Tag>
        }else{
            return <Tag color="#87d068">{status}</Tag>
        }
    }

    function onExpand(keys, {expanded: bool, node}) {
        //console.log(expandedKeys);
        //console.log(bool);
        //console.log(node);
        if (!bool) {
            const ind = keys.indexOf(node.key);
            if (ind > -1) {
                keys.shift(ind, 1);
            }
        }
        setExpandedKeys([...keys]);
    }

    function onSelect(selectedKeys, e: { selected: boolean, selectedNodes, node, event }) {
        setCurrStepNode(e.node);
        if(e.node.key === '1') {
            setStepKey('');
        }else{
            setStepKey(e.node.key);
        }
    }

    const columns: any[] = [
        {
            width: 100,
            title: 'id',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: '步骤名称',
            width: 250,
            dataIndex: 'name',
            ellipsis: true,
            key: 'id',
            render: (text, record) => <a onClick={() => {viewStepResult(record);}}>{text}</a>,
        },
        {
            ellipsis: true,
            width: 250,
            title: '描述',
            dataIndex: 'description',
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
            ellipsis: true,
            title: '结果信息',
            dataIndex: 'resultInfo',
            render: (text) => <pre>{text}</pre>,
            key: 'id',
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
            width: 120,
            fixed: 'right',
            title: '操作',
            key: 'action',
            render: (text, record) => (
                <Space size="middle">
                    <a onClick={() => {viewStepResult(record)}}>查看详细</a>
                </Space>
            ),
        },
    ];

    function renderRight() {
        if(rows && rows.length > 1) {
            return <Table
                scroll={{ x: 1500 }}
                footer={() => '共' + total + '条数据'}
                tableLayout="fixed"
                size="small"
                pagination={pagination}
                onChange={onChangePagination}
                columns={columns}
                dataSource={rows}
                loading={loading}/>
        }else{
            return <StepResultPage stepResult={stepResult} isInModal={false}></StepResultPage>
        }
    }

    return (<div style={{display: 'flex', flexWrap: 'nowrap'}}>
        <div style={{width: 400, overflow: 'auto'}}>
            <Tree
                style={{background: 'aliceblue'}}
                expandedKeys={expandedKeys}
                titleRender={(nodeData) => {
                    if(nodeData.key === errorStepKey) {
                        return (<span><CloseCircleTwoTone twoToneColor="#f50" />{nodeData.seq + ': ' + nodeData.title}</span>)
                    }
                    return (<span>{nodeData.seq + ': ' + nodeData.title}</span>)
                }}
                onSelect={onSelect}
                onExpand={onExpand}
                treeData={steps}
            />
        </div>
        <div style={{paddingLeft: 5, width: "calc(100vw - 450px)"}}>
            {renderRight()}
        </div>
        <Modal
            title="步骤结果"
            open={visible}
            onOk={() => setVisible(false)}
            onCancel={() => setVisible(false)}
            width={1200}
            footer={null}
        >
            <div style={{maxHeight: 'calc(100vh - 300px)', overflow: 'auto'}}>
                <StepResultPage stepResult={stepResult} isInModal={true}></StepResultPage>
            </div>
        </Modal>
    </div>)
}

export {CaseResultPage}
