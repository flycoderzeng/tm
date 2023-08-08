import React, {useEffect, useState} from "react";
import {Button, Descriptions, message, Modal, Progress, Tabs, Tag} from "antd";
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {DateUtils} from "../../../utils/DateUtils";
import {PlanResultStatusUtils} from "../../../utils/PlanResultStatusUtils";
import {PlanResultList} from "./PlanResultList";
import {CaseResultList} from "./CaseResultList";
import {CaseResultPage} from "./CaseResultPage";

interface IState {
    planOrCaseId: number|null|undefined;
    //1-计划 2-用例 3-定时计划
    fromType: number|null|undefined;
    planCaseType: number|null|undefined;
    planResultId: number|null|undefined;
}
const { TabPane } = Tabs;

const PlanResultPage: React.FC<IState> = (props) => {
    const planOrCaseId = (props as any).match.params.planOrCaseId;
    const fromType = (props as any).match.params.fromType;
    const planCaseType = (props as any).match.params.planCaseType;
    let resultId = (props as any).match.params.planResultId;
    const [planResultId, setPlanResultId] = useState(resultId);
    const [planSetupResultId, setPlanSetupResultId] = useState<number|null>(-1);
    const [planTeardownResultId, setTeardownSetupResultId] = useState<number|null>(-1);

    const [planOrCaseName, setPlanOrCaseName] = useState('');
    const [submitInfo, setSubmitInfo] = useState('');
    const [runTimeInfo, setRunTimeInfo] = useState('');
    const [resultStatusInfo, setResultStatusInfo] = useState('');
    const [costTime, setCostTIme] = useState('');
    const [completeInfo, setCompleteInfo] = useState('');
    const [progress, setProgress] = useState<number>(0);
    const [successRate, setSuccessRate] = useState('0%');
    const [successRateColor, setSuccessRateColor] = useState('#87d068');
    const [visible, setVisible] = useState(false);
    const [activeKey, setActiveKey] = useState('1');
    const [panes, setPanes] = useState([{ title: '用例列表', key: '1', closable: false, record: {} }]);

    if(resultId && resultId !== planResultId) {
        setPlanResultId(resultId);
    }

    useEffect(() => {
        load();
    }, [planOrCaseId, fromType, planResultId]);

    function renderBasicInfo(data: any) {
        setPlanResultId(data.id);
        setPlanOrCaseName(data.planOrCaseName + '---' + data.envName + '环境');
        setPlanSetupResultId(data.planSetupResultId);
        setTeardownSetupResultId(data.planTeardownResultId);
        let submitInfoTemp = data.submitter;
        submitInfoTemp += " 提交于 " + DateUtils.format(data.submitTimestamp);
        setSubmitInfo(submitInfoTemp);
        let runTimeInfoTemp = "开始时间: " + DateUtils.format(data.startTimestamp);
        runTimeInfoTemp += ", 结束时间: " + DateUtils.format(data.endTimestamp);
        setRunTimeInfo(runTimeInfoTemp);

        let costTime = '';
        if(data.endTimestamp) {
            costTime = '耗时: ' + ((data.endTimestamp - data.startTimestamp) / 1000) + '秒';
        }
        setCostTIme(costTime);

        let resultStatusInfo = PlanResultStatusUtils.getPlanResultStatusDescription(data.resultStatus);
        setResultStatusInfo(resultStatusInfo);
        let completeInfoTemp = "总数: " + data.total;
        completeInfoTemp += ", 成功: " + data.successCount;
        completeInfoTemp += ", 失败: " + data.failCount;
        setCompleteInfo(completeInfoTemp);
        setProgress(parseFloat(((data.successCount + data.failCount)*100 / data.total).toFixed(2)));
        if(data.failCount > 0) {
            setSuccessRateColor('#f50');
        }else{
            setSuccessRateColor('#87d068');
        }
        if(data.total) {
            setSuccessRate((data.successCount * 100/data.total).toFixed(2) + "%");
        }else{
            setSuccessRate('0%');
        }
    }

    function load() {
        if(!planResultId) {
            axios.post(ApiUrlConfig.GET_NEWEST_PLAN_EXECUTE_RESULT_URL,
                {planOrCaseId: planOrCaseId, fromType: fromType}).then(resp => {
                if (resp.status !== 200) {
                    message.error('加载失败');
                } else {
                    const ret = resp.data;
                    if (ret.code !== 0) {
                        message.error(ret.message);
                    } else if(!ret.data) {
                        message.info('计划没有执行过');
                    } else {
                        renderBasicInfo(ret.data);
                    }
                }
            });
        }else{
            axios.post(ApiUrlConfig.GET_SINGLE_PLAN_EXECUTE_RESULT_URL,
                {id: planResultId}).then(resp => {
                if (resp.status !== 200) {
                    message.error('加载失败');
                } else {
                    const ret = resp.data;
                    if (ret.code !== 0) {
                        message.error(ret.message);
                    } else {
                        renderBasicInfo(ret.data);
                    }
                }
            });
        }
    }

    function onEdit(targetKey, action) {
        if(targetKey === '1') {
            return ;
        }
        if(action === 'remove') {
            remove(targetKey);
        }
    }

    function remove(targetKey) {
        const panes2 = panes.filter(pane => pane.key !== targetKey);
        setPanes(panes2);
        setActiveKey('1');
    }

    function renderPane(pane: any) {
        if(pane.key === '1') {
            return <CaseResultList viewCaseResult={viewCaseResult} planResultId={planResultId}></CaseResultList>
        }
        return (<CaseResultPage caseResult={pane.record}></CaseResultPage>)
    }

    function viewCaseResult(record: any) {
        const key = record.planResultId + "-" + record.caseId + "-" + record.groupNo;
        for (let i = 0; i < panes.length; i++) {
            if(panes[i].key === key) {
                setActiveKey(key);
                return ;
            }
        }
        panes.push({closable: true, key: key, title: record.caseId + "-" + record.groupNo + '-' + record.name, record: record});
        setActiveKey(key);
    }

    function onChange(activeKey) {
        setActiveKey(activeKey);
    }

    function checkSetupResult() {
        if(!planSetupResultId) {
            message.info('没有计划前执行结果');
            return ;
        }
        window.open("#/planresult/" + planOrCaseId + "/" + fromType + "/1/" + planSetupResultId);
    }

    function checkTeardownResult() {
        if(!planTeardownResultId) {
            message.info('没有计划后执行结果');
            return ;
        }
        window.open("#/planresult/" + planOrCaseId + "/" + fromType + "/2/" + planTeardownResultId);
    }

    let checkHistoryResultBtn;
    if(planCaseType === 0 || planCaseType === '0') {
        checkHistoryResultBtn = <Button type="primary" size="small" onClick={()=>{setVisible(true);}}>查看历史结果</Button>
    }

    return (<div className="card">
        <div className="card-header card-header-divider">
            计划运行结果
        </div>
        <div className="card-body">
            <Descriptions size="small" title="基本信息" extra={<div className="tool-bar">
                {checkHistoryResultBtn}
                <Button type="default" size="small" onClick={()=>{checkSetupResult();}}>查看计划前执行结果</Button>
                <Button type="dashed" size="small" onClick={()=>{checkTeardownResult();}}>查看计划后执行结果</Button>
                {/*<Button type="default" size="small">发送邮件</Button>*/}
                {/*<Button type="primary" size="small">再次执行</Button>*/}
            </div>} bordered>
                <Descriptions.Item label="计划名称">{planOrCaseName}</Descriptions.Item>
                <Descriptions.Item label="提交信息">{submitInfo}</Descriptions.Item>
                <Descriptions.Item label="开始结束时间">{runTimeInfo}</Descriptions.Item>
                <Descriptions.Item label="状态">
                    <Tag color="orange">{resultStatusInfo}</Tag>
                    <span>{costTime}</span>
                </Descriptions.Item>
                <Descriptions.Item label="进度">
                    <div style={{display: "flex", alignItems: 'center'}}>
                        <Progress percent={progress} style={{marginRight: '30px'}} />
                        <Tag color="blue">{completeInfo}</Tag>
                        <Tag color={successRateColor}>{successRate}</Tag>
                    </div>
                </Descriptions.Item>
            </Descriptions>
            <Descriptions size="small" title="用例结果">
                <Descriptions.Item label="">
                    <Tabs defaultActiveKey="1" type="editable-card" onEdit={onEdit} activeKey={activeKey} onChange={onChange} hideAdd>
                        {panes.map(pane => (
                            <TabPane tab={pane.title} key={pane.key} closable={pane.closable}>
                                {renderPane(pane)}
                            </TabPane>
                        ))}
                    </Tabs>
                </Descriptions.Item>
            </Descriptions>
        </div>
        <Modal
            title="历史执行结果"
            open={visible}
            onOk={() => setVisible(false)}
            onCancel={() => setVisible(false)}
            width={'80%'}
            footer={null}
        >
            <div>
                <PlanResultList planOrCaseId={planOrCaseId} fromType={fromType}></PlanResultList>
            </div>
        </Modal>
    </div>)
}

export {PlanResultPage}
