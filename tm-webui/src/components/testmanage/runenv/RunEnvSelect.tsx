import React, {useState} from "react";
import {message, Select} from 'antd';
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";


interface IState {
    value: number|string|null;
    style?: any;
    onChange?: any;
    size?: any;
}

interface RunEnv {
    id: number|string|null;
    name: string;
    description: string;
}

const { Option } = Select;

const RunEnvSelect: React.FC<IState> = (props) => {
    const [value, setValue] = useState(props.value);
    const {onChange} = props;
    const {size} = props;

    const [rows, setRows] = useState<RunEnv[]>([]);

    if(rows.length === 0) {
        getAllRunEnv();
    }

    if(value !== props.value) {
        setValue(props.value);
    }

    function renderOptions() {
        let options = rows.map((row => {
            return <Option value={row.id||''} key={row.id||''}>{row.name}</Option>
        }));
        return options;
    }

    function getAllRunEnv() {
        axios.get(ApiUrlConfig.LOAD_ALL_RUN_ENV_URL).then(resp => {
            if (resp.status !== 200) {
                message.error('加载环境列表失败');
            } else {
                const ret = resp.data;
                if(ret.data) {
                    const rows: RunEnv[] = [];
                    ret.data.map(v => {
                        rows.push({id: v.id + '', name: v.attributes.name, description: ''});
                        return null;
                    });
                    setRows(rows);
                }
            }
        });
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
    >
        {renderOptions()}
    </Select>)
}

export {RunEnvSelect};
