import React, {useEffect, useState} from "react";
import {Button, Col, Form, Input, message, Row, Tooltip} from "antd";
import {ArrowLeftOutlined, QuestionCircleOutlined} from "@ant-design/icons";
import {FormInstance} from "antd/lib/form";
import {RunEnvSelect} from "../runenv/RunEnvSelect";
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
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
    const [id, setId] = useState(configId);
    const [saving, setSaving] = useState(false);
    const [ref] = useState(React.createRef<FormInstance>());
    const [runEnvId, setRunEnvId] = useState('');

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
        axios.post(ApiUrlConfig.LOAD_URL_CONFIG_URL, {id: id}).then(resp => {
            if (resp.status !== 200) {
                message.error('加载失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    ref.current?.setFieldsValue({
                        url: ret.data.url,
                        ip: ret.data.ip,
                        port: ret.data.port,
                        envId: ret.data.envId
                    });
                    setRunEnvId(ret.data.envId.toString() || undefined);
                }
            }
        });
    }

    function onFinish(values) {
        values.id = id;
        setSaving(true);
        axios.post(ApiUrlConfig.SAVE_URL_CONFIG_URL, values).then(resp => {
            if (resp.status !== 200) {
                message.error('操作失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    setId(ret.data);
                    message.success('操作成功');
                }
            }
        }).finally(() => {
            setSaving(false);
        });
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
                name="cronjob"
                ref={ref}
                initialValues={initialValues}
                onFinish={onFinish}
            >
                <Form.Item
                    label="接口路径"
                    name="url"
                    rules={[{required: true, message: '请输入接口路径!'}]}
                >
                    <Input style={{width: '300px'}} placeholder="如：/user/save"/>
                </Form.Item>

                <Form.Item
                    label="ip"
                    name="ip"
                    rules={[{required: true, message: '请输入ip!'}]}
                >
                    <Input style={{width: '200px'}} placeholder="如：192.168.1.121"/>
                </Form.Item>

                <Form.Item
                    label="port"
                    name="port"
                    rules={[{required: true, message: '请输入端口!'}]}
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
