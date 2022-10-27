import React, {useState} from "react";
import {Button, Tooltip} from "antd";
import {ArrowLeftOutlined} from "@ant-design/icons";

interface IState {
}

const HttpMockRuleEdit: React.FC<IState> = (props) => {
    const configId = (props as any).match.params.id;
    const [id, setId] = useState(configId);
    const [saving, setSaving] = useState(false);

    function back() {
        window.history.go(-1);
    }

    return (<div className="card">
        <div className="card-header card-header-divider">
            http接口mock规则配置
            <Tooltip title="返回">
                <Button onClick={() => back()} type="primary" size="small" shape="circle" icon={<ArrowLeftOutlined />} />
            </Tooltip>
            <span className="card-subtitle">配置http接口的响应码，响应体等</span>
        </div>
        <div className="card-body">
        </div>
    </div>)
}

export {HttpMockRuleEdit}
