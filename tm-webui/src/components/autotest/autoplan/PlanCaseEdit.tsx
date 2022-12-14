import React, {useEffect, useRef, useState} from "react";
import {Input, Button, Table, message, Modal} from 'antd';
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {CommonNodeListPage} from "../../common/CommonNodeListPage";
import {DataTypeEnum} from "../../../entities/DataTypeEnum";
import moment from "moment";

export interface PlanCaseModel {
    id: number;
    planId: number;
    caseId: number;
    seq: number;
}

interface IState {
    planId: number | null | undefined;
    projectId: number | null;
}

const PlanCaseEdit: React.FC<IState> = (props) => {
    const ref = useRef<{ setSelectedList, setTotalSelect }>(null);
    const [confirmLoading, setConfirmLoading] = useState(false);
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
        pageSize: 10,
        total: 0,
    });
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [isChangeCaseSeqModalVisible, setIsChangeCaseSeqModalVisible] = useState(false);
    const [projectId, setProjectId] = useState(props.projectId);

    if (planId !== props.planId) {
        setPlanId(props.planId);
    }
    if (projectId !== props.projectId) {
        setProjectId(props.projectId);
    }

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
            order: 'seq', sort: 'asc', planId: planId
        };
        if (searchValue.trim() !== '') {
            data['filterConditionList'] = filterConditionList;
        }
        axios.post(ApiUrlConfig.QUERY_PLAN_CASE_LIST_URL, data).then(resp => {
            if (resp.status !== 200) {
                message.error('????????????');
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
                            && moment(new Date(respRows[i].caseCreateTime)).format('YYYY-MM-DD HH:mm:ss');
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
            {planId: planId, caseIdList: selectedResourceIdList}).then(resp => {
            if (resp.status !== 200) {
                message.error('????????????');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('????????????');
                    setIsModalVisible(false);
                    onSearch();
                }
            }
        }).finally(() => {
            setConfirmLoading(false);
        });
    }

    function handleCancel() {
        setIsModalVisible(false);
        setSelectedResourceIdList([]);
    }

    function editCase(record: PlanCaseModel) {

    }

    const columns: any[] = [
        {
            title: '??????',
            dataIndex: 'seq',
            render: text => <span>{text}</span>,
        }, {
            title: '??????id',
            dataIndex: 'caseId',
            render: text => <span>{text}</span>,
        }, {
            title: '??????',
            dataIndex: 'caseName',
            render: text => <span>{text}</span>,
        }, {
            title: '?????????',
            dataIndex: 'caseCreateUsername',
            render: text => <span>{text}</span>,
        }, {
            title: '????????????',
            dataIndex: 'caseCreateTime',
            render: text => <span>{text}</span>,
        }, {
            title: '??????',
            fixed: 'right',
            render: (text, record) => (
                <div>
                    <Button className="padding-left0" size="small" type="link"
                            onClick={() => changeCaseSeq(record)}>????????????</Button>
                    <Button size="small" type="link" onClick={() => editCase(record)}>??????</Button>
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

    function onClearPlanCase() {
        if (!window.confirm('????????????????')) {
            return;
        }
        axios.post(ApiUrlConfig.CLEAR_PLAN_CASE_URL, {id: planId}).then(resp => {
            if (resp.status !== 200) {
                message.error('????????????');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('????????????');
                    onSearch();
                }
            }
        });
    }

    function onDeletePlanCase() {
        if(selectedRowKeys && selectedRowKeys.length < 1) {
            message.info('???????????????????????????');
            return;
        }
        if (!window.confirm('????????????????')) {
            return;
        }
        axios.post(ApiUrlConfig.DELETE_PLAN_CASE_URL,
            {planId: planId, idList: selectedRowKeys}).then(resp => {
            if (resp.status !== 200) {
                message.error('????????????');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('????????????');
                    onSearch();
                }
            }
        });
    }

    function handleOkChangeCaseSeq() {
        axios.post(ApiUrlConfig.CHANGE_CASE_SEQ_URL,
            {planId: planId, caseId: currPlanCase?.caseId, seq: seqValue}).then(resp => {
            if (resp.status !== 200) {
                message.error('????????????');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('????????????');
                    onSearch();
                }
            }
        });
    }

    function handleCancelChangeCaseSeq() {
        setIsChangeCaseSeqModalVisible(false);
    }

    return (<div>
        <div className="list-toolbar">
            <Input placeholder="??????id/??????" onPressEnter={onPressEnter}
                   onChange={onChange} value={searchValue} style={{width: 300,}}/>
            <Button className="margin-left5" type="primary" onClick={onSearch}>??????</Button>
            <Button className="margin-left5" type="primary" onClick={onAddCase}>????????????</Button>
            <Button className="margin-left5" type="primary" danger={true} onClick={onDeletePlanCase}>????????????</Button>
            <Button className="margin-left5" type="primary" danger={true} onClick={onClearPlanCase}>????????????</Button>
        </div>
        <div>
            <Table columns={columns}
                   rowSelection={{
                       type: 'checkbox',
                       ...rowSelection,
                   }}
                   dataSource={rows}
                   size="small"
                   footer={() => '???' + total + '?????????'}
                   loading={loading}
                   pagination={pagination}
                   onChange={onChangePagination}
            />
        </div>
        <Modal width={1200}
               title="????????????"
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
        <Modal title="??????????????????" open={isChangeCaseSeqModalVisible}
               onOk={handleOkChangeCaseSeq}
               onCancel={handleCancelChangeCaseSeq}>
            <Input placeholder="??????????????????" value={seqValue} onChange={onChangeSeqValue}/>
        </Modal>
    </div>)
}

export {PlanCaseEdit}
