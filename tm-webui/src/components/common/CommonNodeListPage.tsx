import React, {forwardRef, useEffect, useImperativeHandle, useState} from "react";
import {DataTypeEnumDescription} from "../../entities/DataTypeEnumDescription";
import {Button, message, Table, Tag} from "antd";
import {
    CheckCircleOutlined, CheckCircleTwoTone
} from '@ant-design/icons';
import {Input} from 'antd';
import axios from "axios";
import moment from "moment";
import {ApiUrlConfig} from "../../config/api.url";
import {DataNodeModel} from "../../entities/DataNodeModel";

const {Search} = Input;

interface IState {
    dataTypeId: number | null,
    projectId: number | null,
    setRenderRightFlag: any,
    setNodeId: any,
    isResourceSelect?: boolean;
    setSelectedResourceIdList?: any;
    ref?: any;
}

const rootTitle = {
    2: DataTypeEnumDescription.APP_API,
    3: DataTypeEnumDescription.AUTO_SHELL,
    4: DataTypeEnumDescription.GLOBAL_VARIABLE,
    5: DataTypeEnumDescription.PLATFORM_API,
    6: DataTypeEnumDescription.AUTO_CASE,
    7: DataTypeEnumDescription.AUTO_PLAN
}

const rootSubTitle = {
    2: DataTypeEnumDescription.APP_API2,
    3: DataTypeEnumDescription.AUTO_SHELL2,
    4: DataTypeEnumDescription.GLOBAL_VARIABLE2,
    5: DataTypeEnumDescription.PLATFORM_API2,
    6: DataTypeEnumDescription.AUTO_CASE2,
    7: DataTypeEnumDescription.AUTO_PLAN2
}

const CommonNodeListPage: React.FC<IState> = forwardRef((props, ref) => {
    const {setRenderRightFlag} = props;
    const {setSelectedResourceIdList} = props;
    const {setNodeId} = props;
    const [projectId, setProjectId] = useState(props.projectId);
    const [dataTypeId, setDataTypeId] = useState(props.dataTypeId);
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [totalSelect, setTotalSelect] = useState<number>(0);
    const [selectedIdList, setSelectedList] = useState<number[]>([]);
    const [pagination, setPagination] = useState({
        current: 1,
        pageNum: 1,
        pageSize: 10,
        linkOperator: 'or',
        total: 0,
        filterConditionList: []
    });
    const title = (props.dataTypeId && rootTitle[props.dataTypeId]) || '';
    const subtitle = (props.dataTypeId && rootSubTitle[props.dataTypeId]) || '';

    let defaultShowColumn;
    if (props.isResourceSelect) {
        defaultShowColumn = ['description', 'addUser', 'addTime'];
    } else {
        defaultShowColumn = ['description', 'addUser', 'addTime', 'lastModifyUser', 'lastModifyTime'];
    }
    const [showColumnValue, setShowColumnValue] = useState<any>(defaultShowColumn);

    if (projectId !== props.projectId) {
        setProjectId(props.projectId);
    }
    if (dataTypeId !== props.dataTypeId) {
        setDataTypeId(props.dataTypeId);
    }
    if (projectId !== props.projectId || dataTypeId !== props.dataTypeId) {
        setData([]);
    }
    useEffect(() => {
        loadDataList(true);
    }, [projectId, dataTypeId]);
    useImperativeHandle(ref, () => ({
        setSelectedList: setSelectedList,
        setTotalSelect: setTotalSelect,
    }));

    function edit(id: number) {
        if (!setRenderRightFlag) {
            return;
        }
        setRenderRightFlag(Number(dataTypeId));
        setNodeId(id);
    }

    function remove(id: number) {
        // eslint-disable-next-line no-restricted-globals
        if (!confirm('????????????????')) {
            return;
        }
        axios.post(ApiUrlConfig.DELETE_NODE_URL,
            {dataTypeId: dataTypeId, projectId: projectId, id: id}).then(resp => {
            if (resp.status !== 200) {
                message.error('????????????');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('????????????');
                    loadDataList(false);
                }
            }
        });
    }

    function onSearch(value) {
        let filterConditionList =
            [
                {"columnName": "name", "value": value},
                {"columnName": "id", "operator": "=", "value": value}];
        if (!value) {
            filterConditionList = [];
        }
        _onSearch(filterConditionList);
    }

    function onChange(pageCondition, filters) {
        const keys = Object.keys(filters);
        setShowColumnValue(filters[keys[0]] || []);
        loadDataList(false, pageCondition);
    }

    function _onSearch(filterConditionList) {
        const pageCondition = {
            ...pagination,
            pageNum: pagination.current,
            pageSize: pagination.pageSize,
            current: pagination.current,
            linkOperator: pagination.linkOperator,
            filterConditionList: filterConditionList
        };
        setPagination(pageCondition)
        loadDataList(true, pageCondition);
    }

    function selectResource(record: DataNodeModel) {
        if (selectedIdList.indexOf(record.id) < 0) {
            selectedIdList.push(record.id);
        } else {
            selectedIdList.splice(selectedIdList.indexOf(record.id), 1);
        }
        setSelectedResourceIdList(selectedIdList);
        setSelectedList(selectedIdList);
        setTotalSelect(selectedIdList.length);
    }

    function loadDataList(reset ?: boolean, pageCondition?: any) {
        let page = pagination;
        if (pageCondition) {
            page = pageCondition;
        }
        if (reset) {
            page.current = 1;
        }
        setLoading(true);
        const data = {
            pageNum: page.current,
            pageSize: page.pageSize,
            dataTypeId: dataTypeId,
            projectId: projectId,
            linkOperator: page.linkOperator,
            filterConditionList: page.filterConditionList
        };
        axios.post(ApiUrlConfig.QUERY_NODE_LIST_URL, data).then(resp => {
            if (resp.status !== 200) {
                message.error('??????????????????');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    ret.data.rows.map(v => {
                        v.key = v.id;
                        if (v.addTime) {
                            v.addTime = moment(new Date(v.addTime)).format('YYYY-MM-DD HH:mm:ss');
                        }
                        if (v.lastModifyTime) {
                            v.lastModifyTime = moment(new Date(v.lastModifyTime)).format('YYYY-MM-DD HH:mm:ss');
                        }

                        return null;
                    });
                    setData(ret.data.rows);
                    setPagination({
                        ...page,
                        total: ret.data.total
                    });
                }
            }
        }).finally(() => {
            setLoading(false);
        });
        return true;
    }

    function renderSelectTag(record: DataNodeModel) {
        let selectTag;
        if (selectedIdList.indexOf(record.id) > -1) {
            selectTag = <CheckCircleTwoTone twoToneColor="#52c41a"/>
        }
        return selectTag;
    }

    let operateColumn;
    if (!props.isResourceSelect) {
        operateColumn = function (text, record) {
            return (
                <div>
                    <Button className="padding-left0" size="small" type="link"
                            onClick={() => edit(record.id)}>??????</Button>
                    <Button danger size="small" type="link" onClick={() => remove(record.id)}>??????</Button>
                </div>
            )
        }
    } else {
        operateColumn = function (text, record) {
            return (
                <div>
                    <Button className="padding-left0" size="small" type="link"
                            onClick={() => selectResource(record)}>??????</Button>
                    {renderSelectTag(record)}
                </div>
            )
        }
    }
    const descriptionColumnJson = {
        title: '??????',
        dataIndex: 'description',
        ellipsis: true,
        render: text => <span>{text}</span>,
    };
    const addUserColumnJson = {
        title: '?????????',
        dataIndex: 'addUser',
        render: text => <span>{text}</span>,
    };
    const addTimeColumnJson = {
        title: '????????????',
        dataIndex: 'addTime',
        render: text => <span>{text}</span>,
    };
    const lastModifyUserColumnJson = {
        title: '?????????',
        dataIndex: 'lastModifyUser',
        render: text => <span>{text}</span>,
    };
    const lastModifyTimeColumnJson = {
        title: '????????????',
        dataIndex: 'lastModifyTime',
        render: text => <span>{text}</span>,
    };
    const operateColumnJson = {
        width: 120,
        title: '??????',
        fixed: 'right',
        render: operateColumn,
        filteredValue: showColumnValue,
        filters: [
            {
                text: '??????',
                value: 'description',
            }, {
                text: '?????????',
                value: 'addUser',
            }, {
                text: '????????????',
                value: 'addTime',
            }, {
                text: '?????????',
                value: 'lastModifyUser',
            }, {
                text: '????????????',
                value: 'lastModifyTime',
            },
        ],
    };

    const columns: any[] = [
        {
            title: 'ID',
            dataIndex: 'id',
            render: (text, record) => <span><a onClick={() => edit(record.id)}>{text}</a></span>,
        }, {
            title: '??????',
            dataIndex: 'name',
            ellipsis: true,
            render: text => <span>{text}</span>,
        },
    ];
    if (showColumnValue.indexOf('description') > -1) {
        columns.push(descriptionColumnJson);
    }
    if (showColumnValue.indexOf('addUser') > -1) {
        columns.push(addUserColumnJson);
    }
    if (showColumnValue.indexOf('addTime') > -1) {
        columns.push(addTimeColumnJson);
    }
    if (showColumnValue.indexOf('lastModifyUser') > -1) {
        columns.push(lastModifyUserColumnJson);
    }
    if (showColumnValue.indexOf('lastModifyTime') > -1) {
        columns.push(lastModifyTimeColumnJson);
    }

    columns.push(operateColumnJson);

    let cardHeader;
    let countTag;
    if (!props.isResourceSelect) {
        cardHeader =
            <div className="card-header card-header-divider">{title}<span className="card-subtitle">{subtitle}</span>
            </div>;
    } else {
        countTag = <Tag icon={<CheckCircleOutlined/>} color="success">
            ????????? {totalSelect} ???
        </Tag>
    }
    return (<div className="card">
        {cardHeader}
        <div className="card-body">
            <div className="list-toolbar">
                <Search placeholder="Id????????????" onSearch={onSearch} enterButton style={{width: 400,}}/>
                {countTag}
            </div>
            <Table columns={columns}
                   dataSource={data}
                   size="small"
                   footer={() => '???' + pagination.total + '?????????'}
                   loading={loading}
                   pagination={pagination}
                   onChange={onChange}
            />
        </div>
    </div>)
})


export {CommonNodeListPage};
