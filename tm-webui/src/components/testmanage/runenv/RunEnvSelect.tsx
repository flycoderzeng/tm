import React, {useState} from "react";
import {message, Select} from 'antd';
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {RandomUtils} from "../../../utils/RandomUtils";

interface IState {
    value: number|string|null;
    style?: any;
    onChange?: any;
    size?: any;
}

const RunEnvSelect: React.FC<IState> = (props) => {
    let loaded = false;
    let allEnvs: any[] = [];
    const [value, setValue] = useState(props.value);
    const [options, setOptions] = useState(allEnvs);
    const {onChange} = props;
    const {size} = props;

    if(value !== props.value) {
        setValue(props.value);
    }

    if(!loaded) {
        loaded = true;
        renderOptions().then(values => {
            allEnvs = values;
            setOptions(values);
        });
    }

    async function renderOptions() {
        const resp = await axios.get(ApiUrlConfig.LOAD_ALL_RUN_ENV_URL);
        if (resp.status !== 200) {
            message.error('加载环境列表失败');
        } else {
            const ret = resp.data;
            if(ret.data) {
                return ret.data.map(v => {
                    return {label: v.attributes.name, value: v.id, key: RandomUtils.getKey()};
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
        defaultValue={value || undefined}
        value={value || undefined}
        placeholder="请选择环境"
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

export {RunEnvSelect};
