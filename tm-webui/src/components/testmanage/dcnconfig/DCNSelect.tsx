import React, {useState} from "react";
import {message, Select} from 'antd';
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";

interface IState {
    value: number|string|null|undefined;
    style?: any;
    onChange?: any;
    size?: any;
}

let loaded = false;
let allDcns: any[] = [];

const DCNSelect: React.FC<IState> = (props) => {
    const [value, setValue] = useState(props.value);
    const [options, setOptions] = useState(allDcns);
    const {onChange} = props;
    const {size} = props;

    if(value !== props.value) {
        setValue(props.value);
    }

    if(!loaded) {
        loaded = true;
        renderOptions().then(values => {
            allDcns = values;
            setOptions(values);
        });
    }

    async function renderOptions() {
        const resp = await axios.get(ApiUrlConfig.LOAD_ALL_DCN_URL);
        if (resp.status !== 200) {
            message.error('加载分布式节点列表失败');
        } else {
            const ret = resp.data;
            if(ret.data) {
                return ret.data.map(v => {
                    return {label: v.attributes.dcnName, value: v.id+''};
                });
            }
        }
        return [];
    }

    function handleChange(v) {
        setValue(v);
        if(onChange) {
            onChange(v);
        }
    }

    return (<Select
        showSearch
        allowClear
        defaultValue={value || undefined}
        value={value || undefined}
        placeholder="请选择分布式节点"
        style={props.style}
        defaultActiveFirstOption={false}
        optionFilterProp="children"
        showArrow={false}
        size={size}
        onChange={handleChange}
        notFoundContent={null}
        options={options}
    >
    </Select>)
}

export {DCNSelect};
