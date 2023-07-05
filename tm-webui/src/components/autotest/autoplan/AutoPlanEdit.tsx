import React, {useEffect, useState} from "react";
import {Button, Form, Input, message, Select, Tabs, Tooltip, Radio, Modal} from "antd";
import {ArrowLeftOutlined} from "@ant-design/icons";
import {DataTypeEnum} from "../../../entities/DataTypeEnum";
import {CommonRemoteSearchMultiSelect} from "../../common/components/CommonRemoteSearchMultiSelect";
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {RunEnvSelect} from "../../testmanage/runenv/RunEnvSelect";
import {PlanCaseEdit} from "./PlanCaseEdit";
import {ValueItem} from "../../../entities/common/ValueItem";
import {KeyValueEditor} from "../case-editor/editor/KeyValueEditor";
import {KeyValueRow} from "../case-editor/entities/KeyValueRow";
import {WindowTopUtils} from "../../../utils/WindowTopUtils";

const Option = Select.Option;

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
    const [visiblePlanGlobalVariableModal, setVisiblePlanGlobalVariableModal] = useState(false);
    const [planVariables, setPlanVariables] = useState<KeyValueRow[]>([]);
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
                message.error('保存失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
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
                message.error('加载失败');
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
                            envId: ret.data.envId == null ? '' : ret.data.envId + '',
                            failContinue: ret.data.failContinue === 0 ? 0 : 1
                        });
                        setRunEnvId(ret.data.envId == null ? '': ret.data.envId.toString());
                        if(ret.data.planVariables) {
                            setPlanVariables(JSON.parse(ret.data.planVariables));
                        }else{
                            setPlanVariables([]);
                        }
                        WindowTopUtils.expandLeftTree(ret.data);
                    }
                    if(ret.data.mailList !== '' && ret.data.mailList !== null && ret.data.mailList !== undefined) {
                        const tempList: string[] = ret.data.mailList?.split(';') || [];
                        const tempValueItemList: ValueItem[] = [];
                        tempList.map(row => {
                            tempValueItemList.push({value: row, label: row});
                        });

                        setMailList(tempValueItemList);
                    }
                }
            }
        });
    }

    function checkPlanResult() {
        window.open("#/planresult/" + id + "/1/0");
    }

    function runPlan() {
        if (!window.confirm('确定运行该计划吗？')) {
            return;
        }

        onRun(1);
    }

    function runPlanWithGroup() {
        if (!window.confirm('确定运行该计划吗？')) {
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
                message.error('运行失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
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

    function savePlanVariables() {
        axios.post(ApiUrlConfig.SAVE_AUTO_PLAN_VARIABLES_URL, {id: id, planVariables: JSON.stringify(planVariables)}).then(resp => {
            if (resp.status !== 200) {
                message.error('操作失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                }
            }
        });
    }

    return (<div className="card">
        <div className="card-header card-header-divider">
            编辑自动化计划
            <Tooltip title="返回">
                <Button onClick={back} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined/>}/>
            </Tooltip>
            <span style={{float: 'right'}}>
                <Button type="primary" size="small" onClick={() => {
                    setVisiblePlanGlobalVariableModal(true);
                }}>计划变量配置</Button>
                <Button className="margin-left5" onClick={runPlan} loading={running1} type="default" size="small">运行计划</Button>
                <Button className="margin-left5" onClick={runPlanWithGroup} loading={running2} type="primary" size="small">组合运行</Button>
                <Button className="margin-left5" onClick={checkPlanResult} type="default" size="small">查看运行结果</Button>
            </span>
        </div>
        <div className="card-body">
            <Tabs defaultActiveKey="1" onChange={onChangeTab} items={[
                {label: '基本信息', key: '1', children: (<div className="card-body stretch-left">
                        <Form
                            {...layout}
                            name="autoplan"
                            form={form}
                            initialValues={initialValues}
                            onFinish={onFinish}
                        >
                            <Form.Item
                                label="名称"
                                name="name"
                                rules={[{required: true, message: '请输入名称!'}]}
                            >
                                <Input/>
                            </Form.Item>

                            <Form.Item
                                label="描述"
                                name="description"
                                rules={[{required: false}]}
                            >
                                <Input.TextArea rows={4}/>
                            </Form.Item>

                            <Form.Item
                                label="邮件接收"
                                name="mailList"
                                rules={[{required: false}]}
                            >
                                <CommonRemoteSearchMultiSelect onChange={setMailList} valueList={mailList}
                                                               type={'user'}></CommonRemoteSearchMultiSelect>
                            </Form.Item>

                            <Form.Item
                                label="类型"
                                name="type"
                                rules={[{required: true, message: '请选择计划类型!'}]}
                            >
                                <Select style={{width: 150}}>
                                    <Option value="1">普通计划</Option>
                                </Select>
                            </Form.Item>

                            <Form.Item
                                label="最大并发数"
                                name="maxOccurs"
                                rules={[{required: true, message: '请输入最大并发数!'}]}
                            >
                                <Input style={{width: 120}} type="number" min={0} max={200}/>
                            </Form.Item>

                            <Form.Item
                                label="运行次数"
                                name="runs"
                                rules={[{required: true, message: '请输入计划运行次数!'}]}
                            >
                                <Input style={{width: 120}} type="number" min={1} max={100}/>
                            </Form.Item>

                            <Form.Item
                                label="用例失败计划是否继续运行"
                                name="failContinue"
                                rules={[{required: true, message: '请输入计划运行次数!'}]}
                            >
                                <Radio.Group>
                                    <Radio value={1}>是</Radio>
                                    <Radio value={0}>否</Radio>
                                </Radio.Group>
                            </Form.Item>

                            <Form.Item
                                label="运行环境"
                                name="envId"
                                rules={[{required: false}]}
                            >
                                <RunEnvSelect onChange={setRunEnvId} style={{width: '200px'}}
                                              value={runEnvId}></RunEnvSelect>
                            </Form.Item>

                            <Form.Item {...tailLayout}>
                                <div className="fixed-bottom">
                                    <Button type="primary" htmlType="submit" loading={saving} style={{left: '70%'}}>
                                        保存
                                    </Button>
                                </div>
                            </Form.Item>
                        </Form>
                    </div>)},
                {label: '计划用例', key: '2', children: (<PlanCaseEdit projectId={projectId} planId={id} planCaseType={0}></PlanCaseEdit>)},
                {label: '计划执行前用例', key: '3', children: (<PlanCaseEdit projectId={projectId} planId={id} planCaseType={1}></PlanCaseEdit>)},
                {label: '计划执行后用例', key: '4', children: (<PlanCaseEdit projectId={projectId} planId={id} planCaseType={2}></PlanCaseEdit>)}
            ]}>
            </Tabs>
        </div>
        <Modal
            title="计划全局变量"
            open={visiblePlanGlobalVariableModal}
            onOk={() => {
                setVisiblePlanGlobalVariableModal(false);
                savePlanVariables();
            }}
            onCancel={() => setVisiblePlanGlobalVariableModal(false)}
            width={900}
        >
            <div>
                <KeyValueEditor rows={planVariables} type={'plan-variable'} onChange={setPlanVariables}></KeyValueEditor>
            </div>
        </Modal>
    </div>)
}
export {AutoPlanEdit}
