import React, {forwardRef, useEffect, useImperativeHandle, useState} from "react";
import {DataTypeEnumDescription} from "../../entities/DataTypeEnumDescription";
import {Button, Checkbox, message, Table, Tag} from "antd";
import {
    CheckCircleOutlined, CheckCircleTwoTone
} from '@ant-design/icons';
import {Input} from 'antd';
import axios from "axios";
import moment from "moment";
import {ApiUrlConfig} from "../../config/api.url";
import {DataNodeModel} from "../../entities/DataNodeModel";
import {WindowTopUtils} from "../../utils/WindowTopUtils";
import {LocalStorageUtils} from "../../utils/LocalStorageUtils";

const {Search} = Input;

interface IState {
    dataTypeId: number | null,
    projectId: number | null,
    setRenderRightFlag: any,
    treeParentId?: number;
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
    const [currFolderChecked, setCurrFolderChecked] = useState<boolean>(false);
    const [totalSelect, setTotalSelect] = useState<number>(0);
    const [selectedIdList, setSelectedList] = useState<number[]>([]);

    const [pagination, setPagination] = useState({
        current: 1,
        pageNum: 1,
        pageSize: 20,
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
        initCurrDirOnly();
        setProjectId(props.projectId);
    }

    if (dataTypeId !== props.dataTypeId) {
        initCurrDirOnly();
        setDataTypeId(props.dataTypeId);
    }

    if (projectId !== props.projectId || dataTypeId !== props.dataTypeId) {
        initCurrDirOnly();
        setData([]);
    }

    useEffect(() => {
        initCurrDirOnly();
        loadDataList(true);
    }, [projectId, dataTypeId, props.treeParentId]);

    useImperativeHandle(ref, () => ({
        setSelectedList: setSelectedList,
        setTotalSelect: setTotalSelect,
    }));


    function initCurrDirOnly() {
        const item = LocalStorageUtils.getItem(LocalStorageUtils.__CURR_DIR_ONLY);
        if (item === 'true') {
            setCurrFolderChecked(true);
        } else {
            setCurrFolderChecked(false);
        }
    }

    function edit(id: number) {
        if (!setRenderRightFlag) {
            return;
        }
        setRenderRightFlag(Number(dataTypeId));
        setNodeId(id);
    }

    function remove(id: number) {
        // eslint-disable-next-line no-restricted-globals
        if (!confirm('确定删除吗?')) {
            return;
        }
        axios.post(ApiUrlConfig.DELETE_NODE_URL,
            {dataTypeId: dataTypeId, projectId: projectId, id: id}).then(resp => {
            if (resp.status !== 200) {
                message.error('操作失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
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
            parentId: null,
            filterConditionList: page.filterConditionList
        };
        if(currFolderChecked) {
            const currDataNode = WindowTopUtils.getWindowTopObject(WindowTopUtils.object_currDataNode);
            data.parentId = currDataNode?.id || 1;
        }
        axios.post(ApiUrlConfig.QUERY_NODE_LIST_URL, data).then(resp => {
            if (resp.status !== 200) {
                message.error('加载列表失败');
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

    function onChangeCurrFolder(e) {
        setCurrFolderChecked(e.target.checked);
        LocalStorageUtils.setItem(LocalStorageUtils.__CURR_DIR_ONLY, e.target.checked);
    }

    let operateColumn;
    if (!props.isResourceSelect) {
        operateColumn = function (text, record) {
            return (
                <div>
                    <Button className="padding-left0" size="small" type="link"
                            onClick={() => edit(record.id)}>修改</Button>
                    <Button danger size="small" type="link" onClick={() => remove(record.id)}>删除</Button>
                </div>
            )
        }
    } else {
        operateColumn = function (text, record) {
            return (
                <div>
                    <Button className="padding-left0" size="small" type="link"
                            onClick={() => selectResource(record)}>选择</Button>
                    {renderSelectTag(record)}
                </div>
            )
        }
    }
    const descriptionColumnJson = {
        title: '描述',
        dataIndex: 'description',
        ellipsis: true,
        render: text => <span>{text}</span>,
    };
    const addUserColumnJson = {
        title: '创建者',
        dataIndex: 'addUser',
        render: text => <span>{text}</span>,
    };
    const addTimeColumnJson = {
        title: '创建时间',
        dataIndex: 'addTime',
        render: text => <span>{text}</span>,
    };
    const lastModifyUserColumnJson = {
        title: '修改者',
        dataIndex: 'lastModifyUser',
        render: text => <span>{text}</span>,
    };
    const lastModifyTimeColumnJson = {
        title: '修改时间',
        dataIndex: 'lastModifyTime',
        render: text => <span>{text}</span>,
    };
    const operateColumnJson = {
        width: 120,
        title: '操作',
        fixed: 'right',
        render: operateColumn,
        filteredValue: showColumnValue,
        filters: [
            {
                text: '描述',
                value: 'description',
            }, {
                text: '创建者',
                value: 'addUser',
            }, {
                text: '创建时间',
                value: 'addTime',
            }, {
                text: '修改者',
                value: 'lastModifyUser',
            }, {
                text: '修改时间',
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
            title: '名称',
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
    let currFolderCheck;
    if (!props.isResourceSelect) {
        cardHeader =
            <div className="card-header card-header-divider">{title}<span className="card-subtitle">{subtitle}</span>
            </div>;
        currFolderCheck = <Checkbox onChange={onChangeCurrFolder} checked={currFolderChecked}>当前目录下</Checkbox>
    } else {
        countTag = <Tag icon={<CheckCircleOutlined/>} color="success">
            选择了 {totalSelect} 项
        </Tag>
    }
    return (<div className="card">
        {cardHeader}
        <div className="card-body">
            <div className="list-toolbar">
                <Search placeholder="Id或者名称" onSearch={onSearch} enterButton style={{width: 400,}}/>
                {currFolderCheck}
                {countTag}
            </div>
            <Table columns={columns}
                   dataSource={data}
                   size="small"
                   footer={() => '共' + pagination.total + '条数据(不包含目录)'}
                   loading={loading}
                   pagination={pagination}
                   onChange={onChange}
            />
        </div>
    </div>)
})


export {CommonNodeListPage};
