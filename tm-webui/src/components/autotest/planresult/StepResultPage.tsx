import React, {useState} from "react";
import {Button, Descriptions, message, Tag} from "antd";
import {PlanResultStatusUtils} from "../../../utils/PlanResultStatusUtils";
import {CaseResultStatusEnum} from "../../../entities/CaseResultStatusEnum";
import copy from "copy-to-clipboard";

interface IState {
    stepResult: any,
    isInModal: boolean
}

const StepResultPage: React.FC<IState> = (props) => {
    const [stepResult, setStepResult] = useState<any>(props.stepResult);

    if(stepResult !== props.stepResult) {
        setStepResult(props.stepResult);
    }

    function renderResultStatus(record: any) {
        const status = PlanResultStatusUtils.getCaseResultStatusDescription(record?.resultStatus);
        if(record?.resultStatus === CaseResultStatusEnum.FAIL) {
            return <Tag color="#f50">{status}</Tag>
        }else{
            return <Tag color="#87d068">{status}</Tag>
        }
    }

    function onCopyResultInfo() {
        if(copy(stepResult?.resultInfo)) {
            message.success('复制成功');
        }else{
            message.error('复制失败');
        }
    }
    const title = props.isInModal ? '' : '步骤结果';
    let resultPage;
    if(stepResult) {
        resultPage = (<Descriptions title={title} size="small" bordered>
            <Descriptions.Item label="步骤名称" labelStyle={{width: 100, minWidth: 100}} span={3}>{stepResult?.name}</Descriptions.Item>
            <Descriptions.Item label="步骤描述" labelStyle={{width: 100, minWidth: 100}} span={3}>{stepResult?.description}</Descriptions.Item>
            <Descriptions.Item label="结果状态" labelStyle={{width: 100, minWidth: 100}} span={3}>{renderResultStatus(stepResult)}</Descriptions.Item>
            <Descriptions.Item label="开始时间" labelStyle={{width: 100, minWidth: 100}} span={3}>{stepResult?.startTimestamp}</Descriptions.Item>
            <Descriptions.Item label="结束时间" labelStyle={{width: 100, minWidth: 100}} span={3}>{stepResult?.endTimestamp}</Descriptions.Item>
            <Descriptions.Item label="耗时(秒)" labelStyle={{width: 100, minWidth: 100}} span={3}>{stepResult?.costTime}</Descriptions.Item>
            <Descriptions.Item label="结果信息" labelStyle={{width: 100, minWidth: 100}} span={3}>
                <div>
                    <div style={{paddingBottom: 5}}>
                        <Button size="small" type="primary" onClick={() => {
                            onCopyResultInfo();
                        }}>复制结果信息</Button>
                    </div>
                    <div style={{overflow: "auto"}}>
                        <pre style={{whiteSpace: 'pre-wrap', wordWrap: 'break-word', width: 'calc(100vw - 600px)'}}>{stepResult?.resultInfo}</pre>
                    </div>
                </div>
            </Descriptions.Item></Descriptions>)
    } else {
        resultPage = (<div>
            <Tag color="#f50">没有相关结果</Tag>
        </div>)
    }

    return (<div>
        {resultPage}
    </div>)
}

export {StepResultPage}
