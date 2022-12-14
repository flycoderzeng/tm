import React, {useEffect, useState} from "react";
import {EditorIState} from "../entities/EditorIState";
import {PlatformApiNode} from "../entities/PlatformApiNode";
import axios from "axios";
import {ApiUrlConfig} from "../../../../config/api.url";
import {AutoComplete, Col, Input, message, Modal, Row, Select, Space, Table, Tooltip} from "antd";
import {ParameterDefineRow} from "../../../../entities/ParameterDefineRow";
import {CommonNameComments} from "./CommonNameComments";
import {EditOutlined} from "@ant-design/icons";
import {ContentEditor} from "./ContentEditor";
import {RandomUtils} from "../../../../utils/RandomUtils";
const Option = Select.Option;

const PlatformApiEditor: React.FC<EditorIState<PlatformApiNode>> = (props) => {
    const [id, setId] = useState(props.define.platformApiId);
    const [parametricList, setParametricList] = useState(props.define.parametricList);
    const [parameterDefineRows, setParameterDefineRows] = useState<ParameterDefineRow[]>([]);
    const [isDetailVisible, setIsDetailVisible] = useState(false);
    const [currRow, setCurrRow] = useState<ParameterDefineRow|null>(null);
    const [currKey, setCurrKey] = useState('');
    const [currValue, setCurrValue] = useState('');

    if(JSON.stringify(parametricList) !== JSON.stringify(props.define.parametricList)) {
        setParametricList(props.define.parametricList);
    }
    if(id !== props.define.platformApiId) {
        setId(props.define.platformApiId);
    }
    useEffect(() => {
        load();
    }, [id]);

    function setDetail(record: ParameterDefineRow) {
        setIsDetailVisible(true);
        setCurrRow(record);
        setCurrKey(record.name);
        setCurrValue(record.defaultValue);
    }

    function onChangeParameterValue(record: ParameterDefineRow|null, value: string) {
        if(!record) {
            return ;
        }
        record['key'] = RandomUtils.getKey();
        record.defaultValue = value;
        let found = false;
        for (let i = 0; i < parametricList.length; i++) {
            if(parametricList[i].name === record.name) {
                parametricList[i].value = value;
                found = true;
                break;
            }
        }
        if(!found) {
            parametricList.push({name: record.name, value: value});
        }
        setParametricList(parametricList);
    }

    const options = props.userDefinedVariables?.map(v => {
        return {label: v.name, value: '${' + v.name + '}'};
    }) as any[];

    const columns: any[] = [
        {
            title: '????????????',
            dataIndex: 'name',
            width: '180px',
            key: 'key',
            render: (text, record) => <Input disabled={true} value={record.name} />,
        },
        {
            title: '??????',
            key: 'key',
            width: 300,
            dataIndex: 'description',
            render: (text, record) => <Input disabled={true} value={record.description} />,
        },
        {
            title: '??????',
            dataIndex: 'type',
            width: '110px',
            key: 'key',
            render: (text, record) => <Select disabled={true} value={record.type} style={{ width: 100 }} >
                <Option value="1" key={1}>?????????</Option>
                <Option value="2" key={2}>??????</Option>
                <Option value="3" key={3}>?????????</Option>
            </Select>,
        },
        {
            title: '????????????',
            dataIndex: 'inout',
            width: '110px',
            key: 'key',
            render: (text, record) => <Select disabled={true} value={record.inout} style={{ width: 100 }} >
                <Option value="1" key={1}>????????????</Option>
                <Option value="2" key={2}>????????????</Option>
            </Select>,
        },
        {
            title: '?????????',
            key: 'key',
            dataIndex: 'defaultValue',
            render: (text, record) => {
                return <AutoComplete
                    defaultValue={record.defaultValue} onChange={(value) => {
                        onChangeParameterValue(record, value);
                    }}
                    style={{width: '100%'}}
                    options={options}
                />;
            }
        },
        {
            title: '??????',
            fixed: 'right',
            key: 'key',
            width: '60px',
            render: (text, record, index) => (
                <Space size="middle">
                    <Tooltip placement="topLeft" title="????????????">
                        <a onClick={() => setDetail(record)}><EditOutlined /></a>
                    </Tooltip>
                </Space>
            ),
        },
    ];


    function handleOk() {
        setIsDetailVisible(false);
        onChangeParameterValue(currRow, currValue);
        setParameterDefineRows([...parameterDefineRows]);
    }

    function handleCancel() {
        setIsDetailVisible(false);
    }

    function refreshContent(value: string) {
        setCurrValue(value);
    }

    function load() {
        axios.post(ApiUrlConfig.LOAD_PLATFORM_API_URL, {id: id}).then(resp => {
            if (resp.status !== 200) {
                message.error('????????????api??????');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    const defineJson:ParameterDefineRow[] = JSON.parse(ret.data.defineJson || '[]');
                    for (let i = 0; i < defineJson.length; i++) {
                        defineJson[i]['key'] = RandomUtils.getKey();
                        for (let j = 0; j < parametricList.length; j++) {
                            if(defineJson[i].name === parametricList[j].name) {
                                defineJson[i].defaultValue = parametricList[j].value;
                                break;
                            }
                        }
                    }
                    setParameterDefineRows(defineJson);
                }
            }
        });
    }
    return (<div>
        <CommonNameComments refreshTree={props.refreshTree} stepNode={props.stepNode} define={props.define}></CommonNameComments>
        <div style={{paddingTop: '5px'}}>
            <Table
                columns={columns}
                dataSource={parameterDefineRows}
                pagination={false}
                bordered
                size="small"
            />
        </div>
        <Modal width={800} title="??????" open={isDetailVisible} onOk={handleOk} onCancel={handleCancel}>
            <Row style={{paddingBottom: '5px'}}>
                <Col flex="100px">????????????</Col>
                <Col flex="auto">
                    <Input value={currKey} disabled={true}/>
                </Col>
            </Row>
            <Row style={{paddingBottom: '5px'}}>
                <Col flex="100px">?????????</Col>
                <Col flex="auto">
                    <div>
                        <ContentEditor userDefinedVariables={props.userDefinedVariables} language={'sql'} content={currValue} refreshContent={refreshContent}></ContentEditor>
                    </div>
                </Col>
            </Row>
        </Modal>
    </div>)
};

export {PlatformApiEditor}
