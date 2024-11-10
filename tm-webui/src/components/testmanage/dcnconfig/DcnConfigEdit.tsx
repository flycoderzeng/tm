import React, {useEffect, useState} from "react";
import {Button, Form, Input, message, Tooltip} from "antd";
import {ArrowLeftOutlined} from "@ant-design/icons";
import {FormInstance} from "antd/lib/form";
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

const DcnConfigEdit: React.FC<IState> = (props) => {
    const configId = (props as any).match.params.id;
    const [id, setId] = useState(configId);
    const [saving, setSaving] = useState(false);
    const [ref] = useState(React.createRef<FormInstance>());

    useEffect(() => {
        load();
    }, [id]);
    function back() {
        window.history.go(-1);
    }

    function load() {
        if (!id || id < 1) {
            return;
        }
        axios.get(ApiUrlConfig.LOAD_DCN_CONFIG_URL + id).then(resp => {
            if (resp.status !== 200) {
                message.error('加载失败');
            } else {
                const ret = resp.data;
                ref.current?.setFieldsValue({
                    dcnName: ret.data.attributes.dcnName,
                    dcnDescription: ret.data.attributes.dcnDescription
                });
            }
        });
    }

    function onFinish(values) {
        const data = {
            "data": {
                "type": "dcn_config",
                "attributes": {
                    dcnName: values.dcnName,
                    dcnDescription: values.dcnDescription
                },
            }
        }
        setSaving(true);
        if (!id || id < 1 || id === '0') {
            axios.post(ApiUrlConfig.SAVE_DCN_CONFIG_URL, data,
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
            axios.patch(ApiUrlConfig.SAVE_DCN_CONFIG_URL + '/' + id, data,
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
            DCN配置
            <Tooltip title="返回">
                <Button onClick={() => back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
            </Tooltip>
            <span className="card-subtitle">DCN名称和描述等配置</span>
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
                    label="DCN名称"
                    name="dcnName"
                    rules={[{required: true, message: '请输入DCN名称!'}]}
                >
                    <Input style={{width: '300px'}}/>
                </Form.Item>

                <Form.Item
                    label="DCN描述"
                    name="dcnDescription"
                    rules={[{required: true, message: '请输入DCN描述!'}]}
                >
                    <Input.TextArea style={{width: '500px'}} rows={5} />
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
export {DcnConfigEdit}
