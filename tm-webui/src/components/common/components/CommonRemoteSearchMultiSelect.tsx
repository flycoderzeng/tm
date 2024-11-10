import {message, Select, Spin} from 'antd';
import {SelectProps} from 'antd/es/select';
import debounce from 'lodash/debounce';
import React, {useState} from "react";
import {ApiUrlConfig} from "../../../config/api.url";
import axios from "axios";
import {ValueItem} from "../../../entities/common/ValueItem";

export interface DebounceSelectProps<ValueType = any>
    extends Omit<SelectProps<ValueType>, 'options' | 'children'> {
    fetchOptions: (search: string) => Promise<ValueType[]>;
    debounceTimeout?: number;
}

function DebounceSelect<
    ValueType extends { key?: string; label: React.ReactNode; value: string | number } = any
    >({ fetchOptions, debounceTimeout = 800, ...props }: DebounceSelectProps) {
    const [fetching, setFetching] = React.useState(false);
    const [options, setOptions] = React.useState<ValueType[]>([]);
    const fetchRef = React.useRef(0);

    const debounceFetcher = React.useMemo(() => {
        const loadOptions = (value: string) => {
            fetchRef.current += 1;
            const fetchId = fetchRef.current;
            setOptions([]);
            setFetching(true);

            fetchOptions(value).then(newOptions => {
                if (fetchId !== fetchRef.current) {
                    // for fetch callback order
                    return;
                }

                setOptions(newOptions);
                setFetching(false);
            });
        };

        return debounce(loadOptions, debounceTimeout);
    }, [fetchOptions, debounceTimeout]);

    return (
        <Select<ValueType>
            labelInValue
            filterOption={false}
            onSearch={debounceFetcher}
            notFoundContent={fetching ? <Spin size="small" /> : null}
            {...props}
            options={options}
        />
    );
}

interface IState {
    valueList: ValueItem[];
    type: 'user'|'resource';
    dataTypeId?: number;
    mode?: "multiple" | "tags" | undefined;
    onChange: any;
}

const CommonRemoteSearchMultiSelect: React.FC<IState> = (props) => {
    const [value, setValue] = useState<ValueItem[]>(props.valueList);
    const {onChange} = props;
    let placeholder;
    const [type, setType] = useState(props.type);
    const [dataTypeId, setDataTypeId] = useState(props.dataTypeId);
    const [mode, setMode] = useState<"multiple" | "tags" | undefined>(props.mode || 'multiple');

    if(type !== props.type) {
        setType(props.type);
    }
    if(JSON.stringify(value) !== JSON.stringify(props.valueList)) {
        setValue(props.valueList);
    }

    if(dataTypeId !== props.dataTypeId) {
        setDataTypeId(props.dataTypeId);
    }

    if(mode !== (props.mode||'multiple')) {
        setMode(props.mode);
    }

    if(type === 'user') {
        placeholder = '输入英文名或中文名搜索';
    }else if(type === 'resource') {
        placeholder = '输入ID或名称搜索';
    }

    async function fetchDataList(searchValue: string): Promise<ValueItem[]> {
        let json: any = {pageNum: 1, pageSize: 50,
            linkOperator:"or", filterConditionList:[{columnName: "username", operator: "like", value: searchValue},
                {columnName:"chinese_name", operator: "like", value: searchValue}]};

        let url = ApiUrlConfig.FIND_USER_URL;
        if(type === 'resource') {
            json = {
                pageSize:50,
                pageNum:1,
                dataTypeId: dataTypeId,
                linkOperator: "or",
                filterConditionList:[
                    {
                        columnName: "name",
                        operator: "like",
                        value: searchValue
                    },
                    {columnName: "id",
                        operator: "=",
                        value: searchValue}]
            };
            url = ApiUrlConfig.QUERY_NODE_LIST_URL;
        }
        return await new Promise<ValueItem[]>((resolve,reject) => {
            axios.post(url, json).then((res) => {
                if(res.data.code !== 0) {
                    message.error(res.data.message);
                    resolve([]);
                }else if(type === 'user') {
                    resolve(res.data.data.map(
                        (row) => ({
                            label: `${row.username}(${row.chineseName})`,
                            value: `${row.username}(${row.chineseName})`,
                        })));
                }else if(type === 'resource') {
                    const rows = (res.data.data && res.data.data.rows) || [];
                    resolve(rows.map(
                        (row) => ({
                            label: `${row.id}(${row.name})`,
                            value: `${row.id}`,
                        })));
                }
            }).catch((error) => {
                resolve([]);
            });
        });
    }

    function onChangeSelectValue(newValue) {
        setValue(newValue);
        onChange(newValue);
    }

    return (
        <DebounceSelect
            mode={mode}
            value={value}
            placeholder={placeholder}
            fetchOptions={fetchDataList}
            onChange={onChangeSelectValue}
            style={{ width: '100%' }}
        />
    );
};

export {CommonRemoteSearchMultiSelect};
