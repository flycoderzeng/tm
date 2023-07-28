import React, {useState} from "react";
import {Form, Input} from "antd";
import {RunEnvSelect} from "../../testmanage/runenv/RunEnvSelect";
import {DCNSelect} from "../../testmanage/dcnconfig/DCNSelect";

interface IState {
    onChange?: any;
}
const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
};
const CommonBatchCopyConfig: React.FC<IState> = (props) => {
    const [srcEnvId, setSrcEnvId] = useState('');
    const [srcDcnId, setSrcDcnId] = useState<string|number|null|undefined>('');
    const [desEnvId, setDesEnvId] = useState('');
    const [ip, setIp] = useState('');
    const [port, setPort] = useState('');
    const {onChange} = props;

    function onChangeSrcEnvId(v) {
        setSrcEnvId(v);
        if(onChange) {
            onChange('srcEnvId', v);
        }
    }

    function onChangeSrcDcnId(v) {
        setSrcDcnId(v);
        if(onChange) {
            onChange('srcDcnId', v);
        }
    }

    function onChangeDesEnvId(v) {
        setDesEnvId(v);
        if(onChange) {
            onChange('desEnvId', v);
        }
    }

    function onChangeIp(e) {
        setIp(e.target.value);
        if(onChange) {
            onChange('ip', e.target.value);
        }
    }

    function onChangePort(e) {
        setPort(e.target.value);
        if(onChange) {
            onChange('port', e.target.value);
        }
    }

    const labelCol = {span: 6, offset: 1};

    return (<div>
        <Form.Item
            label="源环境"
            name="srcEnvId"
            labelCol={labelCol}
            rules={[{required: true}]}
        >
            <RunEnvSelect value={srcEnvId} onChange={onChangeSrcEnvId}></RunEnvSelect>
        </Form.Item>

        <Form.Item
            label="源DCN"
            name="srcDcnId"
            labelCol={labelCol}
            rules={[{required: false}]}
        >
            <DCNSelect value={srcDcnId} onChange={onChangeSrcDcnId}></DCNSelect>
        </Form.Item>

        <Form.Item
            label="目标环境"
            name="desEnvId"
            labelCol={labelCol}
            rules={[{required: true}]}
        >
            <RunEnvSelect value={desEnvId} onChange={onChangeDesEnvId}></RunEnvSelect>
        </Form.Item>

        <Form.Item
            label="新的IP地址"
            name="ip"
            labelCol={labelCol}
            rules={[{required: true, message: '请输入新的IP!' }]}
        >
            <Input value={ip} onChange={onChangeIp}/>
        </Form.Item>

        <Form.Item
            label="新的端口"
            name="port"
            labelCol={labelCol}
            rules={[{required: true, message: '请输入新的端口!' }]}
        >
            <Input value={port} onChange={onChangePort}/>
        </Form.Item>
    </div>)
}
export {CommonBatchCopyConfig}