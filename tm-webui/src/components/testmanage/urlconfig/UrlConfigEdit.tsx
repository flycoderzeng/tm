import React, {useEffect, useState} from "react";
import {Button, Form, Input, message, Tooltip} from "antd";
import {ArrowLeftOutlined} from "@ant-design/icons";
import {FormInstance} from "antd/lib/form";
import {RunEnvSelect} from "../runenv/RunEnvSelect";
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {DCNSelect} from "../dcnconfig/DCNSelect";
const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
};

const tailLayout = {
    wrapperCol: { offset: 8, span: 16 },
};
const initialValues = {};

interface IState {

}
const UrlConfigEdit: React.FC<IState> = (props) => {
    const configId = (props as any).match.params.id;
    const copyFlag = (props as any).match.params.copy;
    const [id, setId] = useState(configId);
    const [saving, setSaving] = useState(false);
    const [ref] = useState(React.createRef<FormInstance>());
    const [runEnvId, setRunEnvId] = useState<string>('');
    const [dcnId, setDcnId] = useState<string|number|null|undefined>('');

    useEffect(() => {
        load();
    }, [id]);
    function back() {
        window.history.go(-1);
    }

    function load() {
        if (!id || id < 1 || id === '0') {
            return;
        }
        axios.get(ApiUrlConfig.LOAD_URL_CONFIG_URL + id).then(resp => {
            if (resp.status !== 200) {
                message.error('加载失败');
            } else {
                const ret = resp.data;
                if(ret.data) {
                    ref.current?.setFieldsValue({
                        name: ret.data.attributes.name,
                        url: ret.data.attributes.url,
                        ip: ret.data.attributes.ip,
                        port: ret.data.attributes.port,
                        envId: ret.data.attributes.envId + '',
                        dcnId: !ret.data.attributes.dcnId ? null : ret.data.attributes.dcnId+'',
                    });
                    setRunEnvId(ret.data.attributes.envId + '');
                    setDcnId(!ret.data.attributes.dcnId ? null : ret.data.attributes.dcnId+'');
                }
            }
        });
        if(copyFlag === 1 || copyFlag === '1') {
            setId(0);
        }
    }

    function onFinish(values) {
        const data = {
            "data": {
                "type": "api_ip_port_config",
                "attributes": {
                    name: values.name,
                    url: values.url,
                    ip: values.ip,
                    port: values.port,
                },
                "relationships": {
                    "runEnv": {
                        "data": {
                            "id": runEnvId,
                            "type": "run_env"
                        }
                    }
                }
            }
        }
        if(dcnId) {
            data['data']['relationships']['dcnConfig'] = {
                "data": {
                    "id": dcnId || null,
                    "type": "dcn_config"
                }
            };
        }else{
            if (id >= 1) {
                axios.post(ApiUrlConfig.SAVE_URL_CONFIG_DCN_ID_TO_NULL_URL, {id: id},
                    {headers: {"Content-Type": "application/json"}}).then(resp => {
                    if (resp.status !== 200) {
                        message.error('操作失败');
                    }
                });
            }
        }
        setSaving(true);
        if(!id || id < 1 || id === '0') {
            axios.post(ApiUrlConfig.SAVE_URL_CONFIG_URL, data,
                {headers: {"Content-Type": "application/vnd.api+json"}}).then(resp => {
                if (resp.status !== 201) {
                    message.error('操作失败');
                } else {
                    const ret = resp.data;
                    setId(ret.data.id);
                    message.success('操作成功');
                    back();
                }
            }).finally(() => {
                setSaving(false);
            });
        } else {
            data["data"]["id"] = id;
            axios.patch(ApiUrlConfig.SAVE_URL_CONFIG_URL + '/' + id, data,
                {headers: {"Content-Type": "application/vnd.api+json"}}).then(resp => {
                if (resp.status !== 204) {
                    message.error('操作失败');
                } else {
                    message.success('操作成功');
                    back();
                }
            }).finally(() => {
                setSaving(false);
            });
        }
    }

    return (<div className="card">
        <div className="card-header card-header-divider">
            接口地址配置
            <Tooltip title="返回">
                <Button onClick={() => back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
            </Tooltip>
            <span className="card-subtitle">接口路径、IP和端口配置</span>
        </div>
        <div className="card-body">
            <Form
                {...layout}
                name="ipPortConfig"
                ref={ref}
                initialValues={initialValues}
                onFinish={onFinish}
            >
                <Form.Item
                    label="名称"
                    name="name"
                    rules={[{required: true, message: '请输入接口名称!'}]}
                >
                    <Input style={{width: '500px'}} placeholder="如：开户"/>
                </Form.Item>
                <Form.Item
                    label="路径"
                    name="url"
                    rules={[{required: true, message: '请输入接口路径!'}]}
                >
                    <Input style={{width: '500px'}} placeholder="如：/user/save"/>
                </Form.Item>

                <Form.Item
                    label="IP"
                    name="ip"
                    rules={[{required: true, message: '请输入ip!'}]}
                >
                    <Input style={{width: '200px'}} placeholder="如：192.168.1.121"/>
                </Form.Item>

                <Form.Item
                    label="端口"
                    name="port"
                    rules={[{required: false, message: '请输入端口!'}]}
                >
                    <Input style={{width: '150px'}} placeholder="如：8080"/>
                </Form.Item>

                <Form.Item
                    label="运行环境"
                    name="envId"
                    rules={[{required: true, message: '请选择环境!'}]}
                >
                    <RunEnvSelect onChange={setRunEnvId} style={{width: '200px'}} value={runEnvId}></RunEnvSelect>
                </Form.Item>

                <Form.Item
                    label="DCN"
                    name="dcnId"
                    rules={[{required: false, message: '请选择DCN!'}]}
                >
                    <DCNSelect onChange={setDcnId} style={{width: '200px'}} value={dcnId}></DCNSelect>
                </Form.Item>

                <Form.Item {...tailLayout}>
                    <div className="fixed-bottom">
                        <Button type="primary" htmlType="submit" loading={saving} style={{left: '70%'}}>
                            保存
                        </Button>
                    </div>
                </Form.Item>
            </Form>
        </div>
    </div>);
}
export {UrlConfigEdit}
