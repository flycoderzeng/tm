import React, {useEffect, useState} from "react";
import {Button, Form, Input, Tooltip, Row, Col, Modal, message, Select, Popconfirm} from "antd";
import {ArrowLeftOutlined, QuestionCircleOutlined} from "@ant-design/icons";
import {FormInstance} from "antd/lib/form";
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {CommonRemoteSearchSingleSelect} from "../../common/components/CommonRemoteSearchSingleSelect";
import {DataTypeEnum} from "../../../entities/DataTypeEnum";
import {RunEnvSelect} from "../runenv/RunEnvSelect";
import {RandomUtils} from "../../../utils/RandomUtils";
const Option = Select.Option;

interface CronJobPlanRelation {
    id: string|number|undefined|null;
    planCronJobId: string|number|undefined|null;
    planId: string|number|undefined|null;
    envId: string|number|undefined|null;
    runType: string|number|undefined|null;
    key?: string;
}

interface IState {
    id: number|null|undefined;
}

const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
};

const tailLayout = {
    wrapperCol: { offset: 8, span: 16 },
};

const initialValues = {};
const leftSpanSize = 8;
const rightSpanSize = 16;

const CronJobEdit: React.FC<IState> = (props) => {
    const jobId = (props as any).match.params.id;

    const [id, setId] = useState(jobId);
    const [saving, setSaving] = useState(false);
    const [visibleCronExpressionDescription, setVisibleCronExpressionDescription] = useState(false);
    const [visibleExpressionDates, setVisibleExpressionDates] = useState(false);
    const [ref] = useState(React.createRef<FormInstance>());
    const [expression, setExpression] = useState('');
    const [cronDates, setCronDates] = useState<string[]>([]);
    const [planRows, setPlanRows] = useState<CronJobPlanRelation[]>([]);

    useEffect(() => {
        load();
    }, [id]);

    function onFinish(values) {
        const data = {
            "data": {
                "type": "plan_cron_job",
                "attributes": {
                    name: values.name,
                    description: values.description,
                    expression: values.expression
                }
            }
        }

        setSaving(true);
        if (!id || id < 1) {
            axios.post(ApiUrlConfig.SAVE_CRON_JOB_URL, data,
                {headers: {"Content-Type": "application/vnd.api+json"}}).then(resp => {
                if (resp.status !== 201) {
                    message.error('????????????');
                } else {
                    const ret = resp.data;
                    setId(ret.data.id);
                    message.success('????????????');
                    savePlanRows(ret.data.id);
                }
            }).finally(() => {
                setSaving(false);
            });
        }else{
            data["data"]["id"] = id;
            axios.patch(ApiUrlConfig.SAVE_CRON_JOB_URL + '/' + id, data,
                {headers: {"Content-Type": "application/vnd.api+json"}}).then(resp => {
                if (resp.status !== 204) {
                    message.error('????????????');
                } else {
                    message.success('????????????');
                    savePlanRows(id);
                }
            }).finally(() => {
                setSaving(false);
            });
        }
    }

    function savePlanRows(id) {
        planRows.map(value => {
            if(!value.id) {
                const data = {
                    "data": {
                        "type": "cron_job_plan_relation",
                        "attributes": {
                            "envId": value.envId,
                            "planId": value.planId,
                            "runType": value.runType,
                            "status": 0
                        },
                        "relationships": {
                            "planCronJob": {
                                "data": {
                                    "id": id,
                                    "type": "plan_cron_job"
                                }
                            }
                        }
                    }
                }
                axios.post(ApiUrlConfig.SAVE_CRON_JOB_PLAN_RELATION_URL, data,
                    {headers: {"Content-Type": "application/vnd.api+json"}}).then(resp => {
                    if (resp.status !== 201) {
                        message.error('????????????');
                    } else {
                        const ret = resp.data;
                        value.id = ret.data.id;
                    }
                });
            } else {
                const data = {
                    "data": {
                        "type": "cron_job_plan_relation",
                        "id": value.id,
                        "attributes": {
                            "envId": value.envId,
                            "planId": value.planId,
                            "runType": value.runType,
                            "status": 0
                        },
                        "relationships": {
                            "planCronJob": {
                                "data": {
                                    "id": id,
                                    "type": "plan_cron_job"
                                }
                            }
                        }
                    }
                }
                axios.patch(ApiUrlConfig.SAVE_CRON_JOB_PLAN_RELATION_URL + '/' + value.id, data,
                    {headers: {"Content-Type": "application/vnd.api+json"}}).then(resp => {
                    if (resp.status !== 204) {
                        message.error('????????????');
                    }
                });
            }
            return null;
        });
    }

    function load() {
        if(!id || id < 1) {
            return ;
        }
        axios.get(ApiUrlConfig.LOAD_PLAN_CRON_JOB_URL + id + '?include=cronJobPlanRelations&sort=-cronJobPlanRelations.id').then(resp => {
            if (resp.status !== 200) {
                message.error('????????????');
            } else {
                const ret = resp.data;
                ref.current?.setFieldsValue({
                    name: ret.data.attributes.name,
                    description: ret.data.attributes.description
                });
                setExpression(ret.data.attributes.cronExpression);

                const rows: CronJobPlanRelation[] = [];
                if(ret.included) {
                    ret.included.map(v => {
                        rows.push({id: v.id, planCronJobId: id, envId: v.attributes.envId + '',
                            planId: v.attributes.planId, runType: v.attributes.runType, key: v.id+''});
                        return null;
                    });
                }
                setPlanRows(rows);
            }
        });
    }

    function back() {
        window.history.go(-1);
    }

    function onCheckExpression() {
        if(!expression) {
            message.info('??????????????????');
            return;
        }
        axios.post(ApiUrlConfig.CHECK_CRON_EXPRESSION_URL, {expression: expression}).then(resp => {
            if (resp.status !== 200) {
                message.error('????????????');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error('????????????: ' + ret.message);
                } else {
                    if (!ret.data) {
                        message.info('???????????????????????????');
                    }else{
                        setCronDates(ret.data);
                        setVisibleExpressionDates(true);
                    }
                }
            }
        });
    }

    function handleClose() {
        setVisibleCronExpressionDescription(false);
        setVisibleExpressionDates(false);
    }

    function showExpressionDesc() {
        setVisibleCronExpressionDescription(true);
    }

    function renderDates() {
        const ps = cronDates.map(function (row, index) {
           return <p key={index}>{row}</p>
        });
        return (<div>{ps}
        </div>);
    }

    function onAddPlan() {
        planRows.push({id: undefined, planId: undefined, planCronJobId: id || null,
            envId: '', runType: 1, key: RandomUtils.getKey()});
        setPlanRows([...planRows]);
    }

    function handleRunTypeChange(value, row: CronJobPlanRelation) {
        row.runType = value;
        setPlanRows([...planRows]);
    }

    function handlePlanChange(value, row: CronJobPlanRelation) {
        row.planId = value;
        setPlanRows([...planRows]);
    }

    function handleRunEnvChange(value, row: CronJobPlanRelation) {
        row.envId = value;
        setPlanRows([...planRows]);
    }

    function removeRow(index) {
        if(planRows[index] && planRows[index].id) {
            axios.delete(ApiUrlConfig.DELETE_CRON_JOB_PLAN_RELATION_URL + planRows[index].id).then(resp => {
                if (resp.status !== 204) {
                    message.error('????????????');
                } else {
                    planRows.splice(index, 1);
                    setPlanRows([...planRows]);
                }
            });
        }else if(planRows[index]) {
            planRows.splice(index, 1);
            setPlanRows([...planRows]);
        }
    }

    function renderPlanRows() {
        const rows = planRows && planRows.map(function (row, index) {
            let defaultValue = '1';
            if(row.runType) {
                defaultValue = row.runType.toString();
            }
            return (
            <Row className="description-row" key={row.id}>
                <Col span={8} className="padding-right5">
                    <CommonRemoteSearchSingleSelect
                        onChange={(v) => {handlePlanChange(v, row);}}
                        type={'resource'}
                        value={(row.planId && row.planId.toString()) || undefined}
                        dataTypeId={DataTypeEnum.AUTO_PLAN}></CommonRemoteSearchSingleSelect>
                </Col>
                <Col span={8} className="padding-right5">
                    <RunEnvSelect value={row.envId+''} onChange={(v) => {handleRunEnvChange(v, row);}}></RunEnvSelect>
                </Col>
                <Col span={8}>
                    <div style={{display: 'flex', alignItems: 'center'}}>
                        <Select
                            style={{marginRight: '5px'}}
                            defaultValue={defaultValue}
                            onChange={(value) => {handleRunTypeChange(value, row);}}>
                            <Option value="1">?????????</Option>
                            <Option value="2">??????</Option>
                        </Select>
                        <Popconfirm
                            title="????????????????"
                            onConfirm={() => {removeRow(index);}}
                            okText="Yes"
                            cancelText="No"
                        >
                            <a href="#" style={{width: '30px'}}>??????</a>
                        </Popconfirm>
                    </div>
                </Col>
            </Row>)
        });
        return (<div>{rows}</div>)
    }

    function onChangeExpression(e) {
        setExpression(e.target.value);
    }

    return (<div className="card">
        <div className="card-header card-header-divider">
            ??????????????????
            <Tooltip title="??????">
                <Button onClick={() => back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
            </Tooltip>
            <span className="card-subtitle">?????????????????????????????????</span>
        </div>
        <div className="card-body">
            <Form
                {...layout}
                name="cronjob"
                ref={ref}
                initialValues={initialValues}
                onFinish={onFinish}
            >
                <Form.Item
                    label="??????"
                    name="name"
                    rules={[{required: true, message: '???????????????!'}]}
                >
                    <Input/>
                </Form.Item>

                <Form.Item
                    label="??????"
                    name="description"
                    rules={[{required: true, message: '???????????????!'}]}
                >
                    <Input.TextArea rows={4}/>
                </Form.Item>

                <Form.Item
                    label="???????????????"
                    name="cronExpression"
                    rules={[{required: false, message: '????????????????????????!'}]}
                >
                    <Input placeholder="????????????3???15?????????0 15 3 * * ?" style={{width: '250px'}} value={expression} onChange={onChangeExpression}/>
                    <Button type="primary" className="margin-left5" onClick={onCheckExpression}>
                        ???????????????
                    </Button>
                    <span className="span-icon-btn" onClick={showExpressionDesc}><QuestionCircleOutlined /></span>
                </Form.Item>
                <Form.Item
                    label="????????????"
                    name="planList"
                    rules={[{required: false, message: '???????????????!'}]}
                >
                    <Input style={{display: 'none'}}/>
                    <div>
                        <Row className="common-header">
                            <Col span={8}>??????</Col>
                            <Col span={8}>??????</Col>
                            <Col span={8}>????????????
                                <Button onClick={onAddPlan} type="primary" size="small" style={{float: 'right'}}>????????????</Button>
                            </Col>
                        </Row>
                    </div>
                    {renderPlanRows()}
                </Form.Item>

                <Form.Item {...tailLayout}>
                    <div className="fixed-bottom">
                        <Button type="primary" htmlType="submit" loading={saving} style={{left: '70%'}}>
                            ??????
                        </Button>
                    </div>
                </Form.Item>
            </Form>
        </div>
        <Modal
            width={600}
            open={visibleCronExpressionDescription}
            onCancel={handleClose}
            title="???????????????"
            footer={[
                <Button key="close" onClick={handleClose}>
                    ??????
                </Button>,
            ]}
        >
            <h4>?????????</h4>
            <p>[???] [???] [??????] [???] [???] [???] [???]</p>
            <h4>?????????</h4>
            <div>
                <Row className="common-header">
                    <Col span={leftSpanSize}>?????????</Col>
                    <Col span={rightSpanSize}>??????</Col>
                </Row>
                <Row className="description-row">
                    <Col span={leftSpanSize}>0 0 12 * * ?</Col>
                    <Col span={rightSpanSize}>??????12?????????</Col>
                </Row>
                <Row className="description-row">
                    <Col span={leftSpanSize}>0 15 10 * * ?</Col>
                    <Col span={rightSpanSize}>??????10???15?????????</Col>
                </Row>
                <Row className="description-row">
                    <Col span={leftSpanSize}>0 */5 * * * ?</Col>
                    <Col span={rightSpanSize}>?????????5?????????0?????????</Col>
                </Row>
                <Row className="description-row">
                    <Col span={leftSpanSize}>0 0 */5 * * ?</Col>
                    <Col span={rightSpanSize}>?????????5????????????0???0?????????</Col>
                </Row>
                <Row className="description-row">
                    <Col span={leftSpanSize}>30 10 5,10,15 * * ?</Col>
                    <Col span={rightSpanSize}>?????????5??????10??????15???10???30?????????</Col>
                </Row>
                <Row className="description-row">
                    <Col span={leftSpanSize}>0 0-10 15 * * ?</Col>
                    <Col span={rightSpanSize}>?????????15:00???15:10????????????????????????</Col>
                </Row>
                <Row className="description-row">
                    <Col span={leftSpanSize}>0 10 15 15 * ?</Col>
                    <Col span={rightSpanSize}>??????15???15???10???????????????</Col>
                </Row>
            </div>
        </Modal>
        <Modal
            width={600}
            onCancel={handleClose}
            open={visibleExpressionDates}
            title="????????????????????????"
            footer={[
                <Button key="close" onClick={handleClose}>
                    ??????
                </Button>,
            ]}
        >
            {renderDates()}
        </Modal>
    </div>)
}
export {CronJobEdit}
