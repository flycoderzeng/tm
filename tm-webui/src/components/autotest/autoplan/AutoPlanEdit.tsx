import React, {useEffect, useState} from "react";
import {Button, Form, Input, message, Select, Tabs, Tooltip, Radio} from "antd";
import {ArrowLeftOutlined} from "@ant-design/icons";
import {DataTypeEnum} from "../../../entities/DataTypeEnum";
import {CommonRemoteSearchMultiSelect} from "../../common/components/CommonRemoteSearchMultiSelect";
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {RunEnvSelect} from "../../testmanage/runenv/RunEnvSelect";
import {PlanCaseEdit} from "./PlanCaseEdit";
import {ValueItem} from "../../../entities/common/ValueItem";

const Option = Select.Option;
const {TabPane} = Tabs;

interface AutoPlanModel {
    name: string;
    description: string;
    type: string;
    maxOccurs: number;
    autoCaseRunTimeout: number;
    runs: number;
    envId: string | null;
    failContinue: 0 | 1;
}

interface IState {
    id: number | null | undefined;
    projectId: number | null;
    setRenderRightFlag: any;
}

const layout = {
    labelCol: {span: 6},
    wrapperCol: {span: 18},
};
const tailLayout = {
    wrapperCol: {offset: 8, span: 16},
};

const initialValues: AutoPlanModel = {
    name: '', description: '', type: "1",
    maxOccurs: 100, autoCaseRunTimeout: 120, runs: 1, envId: null, failContinue: 1
};

const AutoPlanEdit: React.FC<IState> = (props) => {
    const [saving, setSaving] = useState(false);
    const [projectId, setProjectId] = useState(props.projectId);
    const [mailList, setMailList] = useState<ValueItem[]>([]);
    const [id, setId] = useState(props.id);
    const [runEnvId, setRunEnvId] = useState('');
    const [running1, setRunning1] = useState(false);
    const [running2, setRunning2] = useState(false);
    const [form] = Form.useForm();
    if (id !== props.id) {
        setId(props.id);
    }
    useEffect(() => {
        load();
    }, [id]);


    function back() {
        props.setRenderRightFlag(DataTypeEnum.ALL);
    }

    function onFinish(values) {
        values.mailList = mailList.map((row) => row.value).join(';');
        values.id = id;
        setSaving(true);
        axios.post(ApiUrlConfig.SAVE_AUTO_PLAN_URL, values).then(resp => {
            if (resp.status !== 200) {
                message.error('????????????');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('????????????');
                }
            }
        }).finally(() => {
            setSaving(false)
        });
    }

    function onChangeTab() {

    }

    function load() {
        axios.post(ApiUrlConfig.LOAD_AUTO_PLAN_URL, {id: id}).then(resp => {
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
                    if (form) {
                        form.setFieldsValue({
                            name: ret.data.name,
                            description: ret.data.description,
                            type: ret.data.type.toString(),
                            maxOccurs: ret.data.maxOccurs || 100,
                            runs: ret.data.runs || 1,
                            envId: ret.data.envId+'',
                            failContinue: ret.data.failContinue === 0 ? 0 : 1
                        });
                        setRunEnvId(ret.data.envId.toString() || undefined);
                    }
                    const tempList: string[] = ret.data.mailList?.split(';') || [];
                    const tempValueItemList: ValueItem[] = [];
                    tempList.map(row => {
                        tempValueItemList.push({value: row, label: row});
                    });

                    setMailList(tempValueItemList);
                }
            }
        });
    }

    function checkPlanResult() {
        window.open("/planresult/" + id + "/" + "1");
    }

    function runPlan() {
        if (!window.confirm('???????????????????????????')) {
            return;
        }

        onRun(1);
    }

    function runPlanWithGroup() {
        if (!window.confirm('???????????????????????????')) {
            return;
        }
        onRun(2);
    }

    function onRun(runType) {
        if (runType === 1) {
            setRunning1(true);
        }
        if (runType === 2) {
            setRunning2(true);
        }
        axios.post(ApiUrlConfig.RUN_PLAN_URL,
            {runType: runType, planId: id}).then(resp => {
            if (resp.status !== 200) {
                message.error('????????????');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('????????????');
                }
            }
        }).catch(reason => {
            message.error(reason);
        }).finally(() => {
            if (runType === 1) {
                setRunning1(false);
            }
            if (runType === 2) {
                setRunning2(false);
            }
        });
    }

    return (<div className="card">
        <div className="card-header card-header-divider">
            ?????????????????????
            <Tooltip title="??????">
                <Button onClick={back} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined/>}/>
            </Tooltip>
            <span style={{float: 'right'}}>
                <Button type="primary" size="small">??????????????????</Button>
                <Button className="margin-left5" onClick={runPlan} loading={running1} type="default" size="small">????????????</Button>
                <Button className="margin-left5" onClick={runPlanWithGroup} loading={running2} type="primary" size="small">????????????</Button>
                <Button className="margin-left5" onClick={checkPlanResult} type="default" size="small">??????????????????</Button>
            </span>
        </div>
        <div className="card-body">
            <Tabs defaultActiveKey="1" onChange={onChangeTab}>
                <TabPane tab="????????????" key="1">
                    <div className="card-body stretch-left">
                        <Form
                            {...layout}
                            name="autoplan"
                            form={form}
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
                                rules={[{required: false}]}
                            >
                                <Input.TextArea rows={4}/>
                            </Form.Item>

                            <Form.Item
                                label="????????????"
                                name="mailList"
                                rules={[{required: false}]}
                            >
                                <CommonRemoteSearchMultiSelect onChange={setMailList} valueList={mailList}
                                                               type={'user'}></CommonRemoteSearchMultiSelect>
                            </Form.Item>

                            <Form.Item
                                label="??????"
                                name="type"
                                rules={[{required: true, message: '?????????????????????!'}]}
                            >
                                <Select style={{width: 150}}>
                                    <Option value="1">????????????</Option>
                                </Select>
                            </Form.Item>

                            <Form.Item
                                label="???????????????"
                                name="maxOccurs"
                                rules={[{required: true, message: '????????????????????????!'}]}
                            >
                                <Input style={{width: 120}} type="number" min={0} max={200}/>
                            </Form.Item>

                            <Form.Item
                                label="????????????"
                                name="runs"
                                rules={[{required: true, message: '???????????????????????????!'}]}
                            >
                                <Input style={{width: 120}} type="number" min={1} max={100}/>
                            </Form.Item>

                            <Form.Item
                                label="????????????????????????????????????"
                                name="failContinue"
                                rules={[{required: true, message: '???????????????????????????!'}]}
                            >
                                <Radio.Group>
                                    <Radio value={1}>???</Radio>
                                    <Radio value={0}>???</Radio>
                                </Radio.Group>
                            </Form.Item>

                            <Form.Item
                                label="????????????"
                                name="envId"
                                rules={[{required: false}]}
                            >
                                <RunEnvSelect onChange={setRunEnvId} style={{width: '200px'}}
                                              value={runEnvId}></RunEnvSelect>
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
                </TabPane>
                <TabPane tab="????????????" key="2">
                    <PlanCaseEdit projectId={projectId} planId={id}></PlanCaseEdit>
                </TabPane>
            </Tabs>

        </div>
    </div>)
}
export {AutoPlanEdit}
