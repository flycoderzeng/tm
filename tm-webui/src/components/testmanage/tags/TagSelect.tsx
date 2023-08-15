import React, {useEffect, useState} from "react";
import {message, Select} from 'antd';
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {RandomUtils} from "../../../utils/RandomUtils";

interface IState {
    value: any[];
    style?: any;
    onChange?: any;
    size?: any;
    projectId: number|string|null|undefined;
}
let allTags: any[] = [];
const TagSelect: React.FC<IState> = (props) => {

    const [value, setValue] = useState(props.value);
    const [projectId, setProjectId] = useState(props.projectId);
    const [options, setOptions] = useState(allTags);
    const {onChange} = props;
    const {size} = props;

    useEffect(() => {
        setProjectId(props.projectId);
        renderOptions().then(values => {
            allTags = values;
            setOptions(values);
        });
    },[props.projectId]);

    useEffect(() => {
        setValue(props.value);
    },[props.projectId]);


    async function renderOptions() {
        const url = ApiUrlConfig.LOAD_ALL_TAGS_URL.replaceAll('%23projectId%23', projectId+'');
        const resp = await axios.get(url);
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
        mode="multiple"
        defaultValue={value || undefined}
        value={value || undefined}
        placeholder="选择用例标签"
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

export {TagSelect};
