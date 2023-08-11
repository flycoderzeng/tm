import React, {useEffect, useState} from "react";
import {message, Space, Table} from "antd";
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import copy from 'copy-to-clipboard';

interface IState {
    planResultId: number|null|undefined;
    caseId: number|null|undefined;
    groupNo: number|null|undefined;
}

const VariableResultList: React.FC<IState> = (props) => {
    const [planResultId, setPlanResultId] = useState(props.planResultId);
    const [caseId, setCaseId] = useState(props.caseId);
    const [groupNo, setGroupNo] = useState(props.groupNo);
    const [total, setTotal] = useState(0);
    const [rows, setRows] = useState<any[]>([]);
    const [loading, setLoading] = useState(false);

    if(planResultId !== props.planResultId) {
        setPlanResultId(props.planResultId);
    }
    if(caseId !== props.caseId) {
        setCaseId(props.caseId);
    }
    if(groupNo !== props.groupNo) {
        setGroupNo(props.groupNo);
    }

    useEffect(() => {
        load();
    }, [planResultId, caseId, groupNo]);

    function renderRows(data: any[]) {
        if(!data) {
            setTotal(0);
            setRows([]);
            return ;
        }
        data.map(r => {
            r.key = r.id;
            return null;
        });
        setTotal(data.length);
        setRows(data);
    }

    function load() {
        if(!planResultId || !caseId || (groupNo === null || groupNo === undefined)) {
            return ;
        }
        setLoading(true);
        axios.post(ApiUrlConfig.GET_CASE_VARIABLE_RESULT_LIST_URL, {
            planResultId: planResultId, caseId: caseId, groupNo: groupNo
        }).then(resp => {
            if (resp.status !== 200) {
                message.error('加载失败');
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

    function copyVariableResult(record: any) {
        if(copy(record.variableValue)) {
            message.success('复制成功');
        }else{
            message.error('复制失败');
        }
    }

    const columns: any[] = [
        {
            width: 300,
            title: '变量名称',
            dataIndex: 'variableName',
            key: 'id',
        },
        {
            ellipsis: true,
            title: '值',
            dataIndex: 'variableValue',
            key: 'id',
        },
        {
            width: 120,
            fixed: 'right',
            title: '操作',
            key: 'action',
            render: (text, record) => (
                <Space size="middle">
                    <a onClick={() => {copyVariableResult(record)}}>复制变量值</a>
                </Space>
            ),
        }];

    return (<div>
        <Table
            footer={() => '共' + total + '条数据'}
            tableLayout="fixed"
            size="small"
            columns={columns}
            dataSource={rows}
            loading={loading}/>
    </div>)
}

export {VariableResultList}
