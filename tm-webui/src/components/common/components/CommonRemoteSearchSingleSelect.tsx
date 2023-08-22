import React, {useState} from "react";

import {message, Select} from 'antd';
import {ValueItem} from "../../../entities/common/ValueItem";
import {ApiUrlConfig} from "../../../config/api.url";
import axios from "axios";

const { Option } = Select;


interface IState {
    type: 'user'|'resource';
    dataTypeId?: number;
    value: string|null|undefined|number;
    style?: any;
    onChange?: any;
}

const CommonRemoteSearchSingleSelect: React.FC<IState> = (props) => {
    const [value, setValue] = useState(props.value);
    let placeholder;
    const [type, setType] = useState(props.type);
    const [dataTypeId, setDataTypeId] = useState(props.dataTypeId);
    const [rows, setRows] = useState<ValueItem[]>([]);
    const {onChange} = props;

    if(type !== props.type) {
        setType(props.type);
    }

    if(dataTypeId !== props.dataTypeId) {
        setDataTypeId(props.dataTypeId);
    }

    if(type === 'user') {
        placeholder = '输入英文名或中文名搜索';
    }else if(type === 'resource') {
        placeholder = '输入id或名称搜索';
    }

    if(value && rows.length < 1) {
        handleSearch(value);
    }

    function handleSearch(searchValue) {
        let json: any = {pageNum: 1, pageSize: 50,
            linkOperator:"or", filterConditionList:[{columnName: "username", operator: "like", value: searchValue},
                {columnName:"chinese_name", operator: "like", value: searchValue}]};

        let url = ApiUrlConfig.FIND_USER_URL;
        if(type === 'resource') {
            json = {
                pageNum: 1,
                pageSize: 50,
                dataTypeId: dataTypeId,
                linkOperator: "or",
                filterConditionList: [
                    {
                        columnName: "name",
                        operator: "like",
                        value: searchValue
                    },
                    {
                        columnName: "id",
                        operator: "=",
                        value: searchValue
                    }]
            };
            url = ApiUrlConfig.QUERY_NODE_LIST_URL;
        }
        axios.post(url, json).then((res) => {
            if(res.data.code !== 0) {
                message.error(res.data.message);
                setRows([]);
            }else if(type === 'user') {
                const list = res.data.data.map(
                    (row) => ({
                        label: `${row.username}(${row.chineseName})`,
                        value: `${row.username}(${row.chineseName})`,
                    }));
                setRows(list);
            }else if(type === 'resource') {
                let list = (res.data.data && res.data.data.rows) || [];
                list = list.map(
                    (row) => ({
                        label: `${row.id}(${row.name})`,
                        value: `${row.id}`,
                    }));
                setRows(list);
            }
        });
    }

    function handleChange(value) {
        setValue(value);
        if(onChange) {
            onChange(value);
        }
    }

    function renderOptions() {
        let options = rows.map((row => {
            return <Option value={row.value} key={row.value}>{row.label}</Option>
        }));
        return options;
    }

    return (
        <Select
            showSearch
            allowClear
            value={value||undefined}
            placeholder={placeholder}
            style={props.style}
            defaultActiveFirstOption={false}
            showArrow={false}
            filterOption={false}
            onSearch={handleSearch}
            onChange={handleChange}
            notFoundContent={null}
        >
            {renderOptions()}
        </Select>
    );
}

export {CommonRemoteSearchSingleSelect}
