import React, {useEffect, useRef, useState} from "react";
import {Input, Button, Table, message, Modal} from 'antd';
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {CommonNodeListPage} from "../../common/CommonNodeListPage";
import {DataTypeEnum} from "../../../entities/DataTypeEnum";
import moment from "moment";
import {MathUtils} from "../../../utils/MathUtils";
import {DataNodeTreeSelect} from "../../common/DataNodeTreeSelect";
import {DateUtils} from "../../../utils/DateUtils";

export interface PlanCaseModel {
    id: number;
    planId: number;
    caseId: number;
    seq: number;
}

interface IState {
    planId: number | null | undefined;
    projectId: number | null;
    planCaseType: number;
}

const initialPagination: any = {
    current: 1,
    pageNum: 1,
    pageSize: 20,
    total: 0,
};
const PlanCaseEdit: React.FC<IState> = (props) => {
    const ref = useRef<{ setSelectedList, setTotalSelect }>(null);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [confirmLoading2, setConfirmLoading2] = useState(false);
    const [searchValue, setSearchValue] = useState('');
    const [rows, setRows] = useState<PlanCaseModel[]>([]);
    const [total, setTotal] = useState(0);
    const [loading, setLoading] = useState(false);
    const [planId, setPlanId] = useState(props.planId);
    const [selectedResourceIdList, setSelectedResourceIdList] = useState([]);
    const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
    const [currPlanCase, setCurrPlanCase] = useState<PlanCaseModel>();
    const [seqValue, setSeqValue] = useState('');

    const [pagination, setPagination] = useState({
        current: 1,
        pageNum: 1,
        pageSize: 20,
        total: 0,
    });
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [isModalVisible2, setIsModalVisible2] = useState(false);
    const [isChangeCaseSeqModalVisible, setIsChangeCaseSeqModalVisible] = useState(false);
    const [projectId, setProjectId] = useState(props.projectId);

    useEffect(() => {
        setPlanId(props.planId);
        setProjectId(props.projectId);
    }, [props.planId, props.projectId]);

    useEffect(() => {
        setPagination(prevState => {return {...prevState, ...initialPagination};});
    }, [props.planId, props.projectId]);

    useEffect(() => {
        onSearch();
    }, [planId, pagination.pageSize, pagination.pageNum]);

    function onSearch() {
        setLoading(true);
        const filterConditionList =
            [{"columnName": "case_id", "operator": "=", "value": searchValue},
                {"columnName": "case_name", "value": searchValue}];
        const data = {
            pageNum: pagination.pageNum,
            pageSize: pagination.pageSize,
            order: 'seq', sort: 'asc', planId: planId,
            planCaseType: props.planCaseType
        };
        if (searchValue.trim() !== '') {
            data['filterConditionList'] = filterConditionList;
        }
        axios.post(ApiUrlConfig.QUERY_PLAN_CASE_LIST_URL, data).then(resp => {
            if (resp.status !== 200) {
                message.error('加载失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    if (!ret.data) {
                        return;
                    }
                    const respRows = ret.data.rows || [];
                    for (let i = 0; i < respRows.length; i++) {
                        respRows[i].key = respRows[i].id;
                        respRows[i].caseCreateTime =
                            respRows[i].caseCreateTime
                            && DateUtils.format(new Date(respRows[i].caseCreateTime));
                        respRows[i].addTime =
                            respRows[i].addTime
                            && DateUtils.format(new Date(respRows[i].addTime));
                    }
                    setRows(respRows);
                    setTotal(ret.data.total);
                    pagination.total = ret.data.total;
                    setPagination(pagination);
                }
            }
        }).finally(() => {
            setLoading(false);
        });
    }

    function onPressEnter(e) {
        onSearch();
    }

    function onChange(e) {
        setSearchValue(e.target.value);
    }

    function onChangeSeqValue(e) {
        setSeqValue(e.target.value);
    }

    function changeCaseSeq(planCase: PlanCaseModel) {
        setCurrPlanCase(planCase);
        setSeqValue('');
        setIsChangeCaseSeqModalVisible(true);
    }

    function handleOk() {
        if (selectedResourceIdList.length < 1) {
            setIsModalVisible(false);
            return;
        }
        setConfirmLoading(true);
        axios.post(ApiUrlConfig.ADD_CASE_TO_PLAN_URL,
            {planId: planId, caseIdList: selectedResourceIdList, type: props.planCaseType}).then(resp => {
            if (resp.status !== 200) {
                message.error('加载失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    setIsModalVisible(false);
                    onSearch();
                }
            }
        }).finally(() => {
            setConfirmLoading(false);
        });
    }

    function handleOk2() {
        if (selectedResourceIdList.length < 1) {
            setIsModalVisible2(false);
            return;
        }
        setConfirmLoading2(true);
        axios.post(ApiUrlConfig.ADD_CASE_TREE_TO_PLAN_URL,
            {planId: planId, caseIdList: selectedResourceIdList, type: props.planCaseType}).then(resp => {
            if (resp.status !== 200) {
                message.error('加载失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    setIsModalVisible2(false);
                    onSearch();
                }
            }
        }).finally(() => {
            setConfirmLoading2(false);
        });
    }

    function handleCancel() {
        setIsModalVisible(false);
        setSelectedResourceIdList([]);
    }

    function handleCancel2() {
        setIsModalVisible2(false);
        setSelectedResourceIdList([]);
    }

    function editCase(record: PlanCaseModel) {

    }

    const columns: any[] = [
        {
            title: '序号',
            dataIndex: 'seq',
            render: text => <span>{text}</span>,
        }, {
            title: '用例id',
            dataIndex: 'caseId',
            render: text => <span>{text}</span>,
        }, {
            title: '名称',
            dataIndex: 'caseName',
            render: text => <span>{text}</span>,
        }, {
            title: '创建者',
            dataIndex: 'caseCreateUsername',
            render: text => <span>{text}</span>,
        }, {
            title: '用例创建时间',
            dataIndex: 'caseCreateTime',
            render: text => <span>{text}</span>,
        }, {
            title: '添加到计划时间',
            dataIndex: 'addTime',
            render: text => <span>{text}</span>,
        }, {
            title: '操作',
            fixed: 'right',
            render: (text, record) => (
                <div>
                    <Button className="padding-left0" size="small" type="link"
                            onClick={() => changeCaseSeq(record)}>调整序号</Button>
                    <Button size="small" type="link" onClick={() => editCase(record)}>编辑</Button>
                </div>
            ),
        },
    ];
    const rowSelection = {
        onChange: (selectedRowKeys: React.Key[], selectedRows: PlanCaseModel[]) => {
            //console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
            setSelectedRowKeys(selectedRowKeys);
        },
    };

    function onChangePagination(pagination) {
        setPagination({
            ...pagination,
            pageNum: pagination.current,
            pageSize: pagination.pageSize,
            current: pagination.current,
        });
    }

    function onAddCase() {
        setIsModalVisible(true);
        if (ref && ref.current) {
            ref.current?.setSelectedList([]);
            ref.current?.setTotalSelect(0);
        }
    }

    function onAddCase2() {
        setIsModalVisible2(true);
    }

    function onClearPlanCase() {
        if (!window.confirm('确定清空吗?')) {
            return;
        }
        axios.post(ApiUrlConfig.CLEAR_PLAN_CASE_URL, {planId: planId, type: props.planCaseType}).then(resp => {
            if (resp.status !== 200) {
                message.error('清空失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    onSearch();
                }
            }
        });
    }

    function onDeletePlanCase() {
        if(selectedRowKeys && selectedRowKeys.length < 1) {
            message.info('请勾选要删除的用例');
            return;
        }
        if (!window.confirm('确定删除吗?')) {
            return;
        }
        axios.post(ApiUrlConfig.DELETE_PLAN_CASE_URL,
            {planId: planId, idList: selectedRowKeys, type: props.planCaseType}).then(resp => {
            if (resp.status !== 200) {
                message.error('删除失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    onSearch();
                }
            }
        });
    }

    function handleOkChangeCaseSeq() {
        if(!MathUtils.isNumberSequence(seqValue)) {
            message.info('请输入数字序号');
            return ;
        }
        axios.post(ApiUrlConfig.CHANGE_CASE_SEQ_URL,
            {planId: planId, caseId: currPlanCase?.caseId, seq: seqValue, type: props.planCaseType}).then(resp => {
            if (resp.status !== 200) {
                message.error('调整失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                    onSearch();
                }
            }
        });
        handleCancelChangeCaseSeq();
    }

    function handleCancelChangeCaseSeq() {
        setIsChangeCaseSeqModalVisible(false);
    }

    return (<div>
        <div className="list-toolbar">
            <Input placeholder="用例id/名称" onPressEnter={onPressEnter}
                   onChange={onChange} value={searchValue} style={{width: 300,}}/>
            <Button className="margin-left5" type="primary" onClick={onSearch}>搜索</Button>
            <Button className="margin-left5" type="primary" onClick={onAddCase}>添加用例</Button>
            <Button className="margin-left5" type="primary" danger={true} onClick={onDeletePlanCase}>删除用例</Button>
            <Button className="margin-left5" type="primary" danger={true} onClick={onClearPlanCase}>清空用例</Button>
            <Button className="margin-left5" type="primary" onClick={onAddCase2}>树形选择</Button>
        </div>
        <div>
            <Table columns={columns}
                   rowSelection={{
                       type: 'checkbox',
                       ...rowSelection,
                   }}
                   dataSource={rows}
                   size="small"
                   footer={() => '共' + total + '条数据'}
                   loading={loading}
                   pagination={pagination}
                   onChange={onChangePagination}
            />
        </div>
        <Modal width={'80%'}
               title="用例选择"
               confirmLoading={confirmLoading}
               open={isModalVisible}
               onOk={handleOk}
               onCancel={handleCancel}>
            <CommonNodeListPage
                ref={ref}
                setSelectedResourceIdList={setSelectedResourceIdList}
                isResourceSelect={true}
                setNodeId={null}
                setRenderRightFlag={null}
                projectId={projectId}
                dataTypeId={DataTypeEnum.AUTO_CASE}>
            </CommonNodeListPage>
        </Modal>
        <Modal width={800}
               title="用例选择"
               confirmLoading={confirmLoading2}
               open={isModalVisible2}
               onOk={handleOk2}
               onCancel={handleCancel2}>
            <DataNodeTreeSelect
                onChange={setSelectedResourceIdList}
                projectId={projectId}
                observerId={planId}
                dataTypeId={DataTypeEnum.AUTO_CASE}>
            </DataNodeTreeSelect>
        </Modal>
        <Modal title="调整用例位置" open={isChangeCaseSeqModalVisible}
               onOk={handleOkChangeCaseSeq}
               onCancel={handleCancelChangeCaseSeq}>
            <Input placeholder="输入用例位置" type="number" value={seqValue} onChange={onChangeSeqValue}/>
        </Modal>
    </div>)
}

export {PlanCaseEdit}
