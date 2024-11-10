import React, {useCallback, useEffect, useRef, useState} from 'react';
import axios from "axios";
import {Button, Form, Input, message, Select, Space, Table, Tooltip} from 'antd';
import {ArrowLeftOutlined, CloseOutlined, PlusOutlined} from '@ant-design/icons';
import {ParameterDefineRow} from "../../../entities/ParameterDefineRow";
import {ApiUrlConfig} from "../../../config/api.url";
import {DndProvider, useDrag, useDrop} from 'react-dnd';
import {HTML5Backend} from 'react-dnd-html5-backend';
import update from 'immutability-helper';
import {DataTypeEnum} from "../../../entities/DataTypeEnum";
import {RandomUtils} from "../../../utils/RandomUtils";

const { Option } = Select;
interface IState {
    id: number|null|undefined;
    setRenderRightFlag: any;
}

const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
};
const tailLayout = {
    wrapperCol: { offset: 8, span: 16 },
};


const type = 'DraggableBodyRow';

const DraggableBodyRow = ({ index, moveRow, className, style, ...restProps }) => {
    const ref: any = useRef();
    const [{ isOver, dropClassName }, drop] = useDrop({
        accept: type,
        collect: monitor => {
            const { index: dragIndex }: any = monitor.getItem() || {};
            if (dragIndex === index) {
                return {};
            }
            return {
                isOver: monitor.isOver(),
                dropClassName: dragIndex < index ? ' drop-over-downward' : ' drop-over-upward',
            };
        },
        drop: (item: any) => {
            moveRow(item.index, index);
        },
    });
    const [, drag] = useDrag({
        type,
        item: { index },
        collect: monitor => ({
            isDragging: monitor.isDragging(),
        }),
    });
    drop(drag(ref));

    return (
        <tr
            ref={ref}
            className={`${className}${isOver ? dropClassName : ''}`}
            style={{ cursor: 'move', ...style }}
            {...restProps}
        />
    );
};
const initialValues = {
    name: '',
    description: '',
};

const PlatformApiEdit: React.FC<IState> = (props) => {
    const [id, setId] = useState(props.id);
    const [rows, setRows] = useState<ParameterDefineRow[]>([]);
    const [saving, setSaving] = useState(false);
    const [form] = Form.useForm();
    if(id !== props.id) {
        setId(props.id);
    }
    useEffect(() => {
        load();
    }, [id]);

    function load() {
        axios.post(ApiUrlConfig.LOAD_PLATFORM_API_URL, {id: id}).then(resp => {
            if (resp.status !== 200) {
                message.error('加载失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    if (!ret.data) {
                        return;
                    }
                    let rows: ParameterDefineRow[] = [];
                    if(ret.data.rows) {
                        rows = ret.data.rows || [];
                    }else{
                        rows = [];
                    }
                    if(rows.length < 1) {
                        rows.push({name: '', description: '', inout: '1', type: '1', defaultValue: ''});
                    }
                    for (let i = 0; i < rows.length; i++) {
                        rows[i]['key'] = RandomUtils.getKey();
                    }
                    if(form) {
                        form.setFieldsValue({
                            name: ret.data.name,
                            description: ret.data.description,
                        });
                    }
                    setRows(rows);
                }
            }
        });
    }

    function back() {
        props.setRenderRightFlag(DataTypeEnum.ALL);
    }

    function onFinish(values) {
        if (id && id > 0) {
            values['id'] = id;
        }
        values['defineJson'] = JSON.stringify(rows);
        if(rows.length === 0 || rows[0].name === '') {
            values['defineJson'] = '';
        }
        setSaving(true);
        axios.post(ApiUrlConfig.SAVE_PLATFORM_API_URL, values).then(resp => {
            if (resp.status !== 200) {
                message.error('保存失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    message.success('操作成功');
                }
            }
        }).finally(() => {
            setSaving(false);
        });
    }

    function onFinishFailed() {

    };

    function addRow(index) {
        const row = {name: '', description: '', inout: '1', type: '1', defaultValue: ''};
        row['key'] = RandomUtils.getKey();
        rows.splice(index+1, 0, row);
        setRows([...rows]);
    }

    function deleteRow(index) {
        if(rows.length < 2) {
            return;
        }
        rows.splice(index, 1);
        setRows([...rows]);
    }

    const columns: any[] = [
        {
            title: '参数名称',
            dataIndex: 'name',
            width: '210px',
            render: (text, record) => <Input defaultValue={record.name} onChange={(e)=>{record.name=e.target.value;}}/>,
        },
        {
            title: '描述',
            dataIndex: 'description',
            render: (text, record) => <Input defaultValue={record.description} onChange={(e)=>{record.description=e.target.value;}}/>,
        },
        {
            title: '类型',
            width: '110px',
            dataIndex: 'type',
            render: (text, record) => <Select defaultValue={record.type} style={{ width: 100 }} onChange={(value)=>{record.type=value;}}>
                <Option value="1" key={1}>字符串</Option>
                <Option value="2" key={2}>数字</Option>
                <Option value="3" key={3}>布尔型</Option>
            </Select>,
        },
        {
            title: '输入输出',
            width: '110px',
            dataIndex: 'inout',
            render: (text, record) => <Select defaultValue={record.inout} style={{ width: 100 }} onChange={(value)=>{record.inout=value;}}>
                <Option value="1" key={1}>输入参数</Option>
                <Option value="2" key={2}>输出参数</Option>
            </Select>,
        },
        {
            title: '默认值',
            width: '200px',
            dataIndex: 'defaultValue',
            render: (text, record) => <Input defaultValue={record.defaultValue} onChange={(e)=>{record.description=e.target.value;}}/>,
        },
        {
            title: '操作',
            fixed: 'right',
            key: 'action',
            render: (text, record, index) => (
                <Space size="middle">
                    <a onClick={() => {addRow(index);}}><PlusOutlined /></a>
                    <a onClick={() => {deleteRow(index);}}><CloseOutlined /></a>
                </Space>
            ),
        },
    ];

    const components = {
        body: {
            row: DraggableBodyRow,
        },
    };

    const moveRow = useCallback(
        (dragIndex, hoverIndex) => {
            const dragRow = rows[dragIndex];
            setRows(
                update(rows, {
                    $splice: [
                        [dragIndex, 1],
                        [hoverIndex, 0, dragRow],
                    ],
                }),
            );
        },
        [rows],
    );

    return (<div className="card stretch-left">
        <div className="card-header card-header-divider">
            编辑平台API
            <Tooltip title="返回">
                <Button onClick={() => back()} type="primary" size="small" shape="circle"
                        icon={<ArrowLeftOutlined/>}/>
            </Tooltip>
            <span className="card-subtitle">自动化测试平台内置提供的方法</span>
        </div>
        <div className="card-body">
            <Form
                className=""
                {...layout}
                name="shell"
                form={form}
                initialValues={initialValues}
                onFinish={onFinish}
                onFinishFailed={onFinishFailed}
            >
                <Form.Item
                    label="名称"
                    name="name"
                    rules={[{required: true, message: '请输入名称!'}]}
                >
                    <Input/>
                </Form.Item>

                <Form.Item
                    label="描述"
                    name="description"
                    rules={[{required: false, message: '请输入描述!'}]}
                >
                    <Input.TextArea rows={4} />
                </Form.Item>

                <Form.Item
                    label="参数配置"
                    name="parameter"
                    rules={[{required: false}]}
                >
                    <DndProvider backend={HTML5Backend}>
                        <Table
                            columns={columns}
                            dataSource={rows}
                            bordered
                            pagination={{ defaultPageSize: 100 }}
                            components={components}
                            onRow={(record, index) => ({
                                index,
                                moveRow,
                            }) as any}
                            size="small"
                        />
                    </DndProvider>
                </Form.Item>

                <Form.Item {...tailLayout}>
                    <div className="fixed-bottom">
                        <Button type="primary" htmlType="submit" loading={saving} style={{left: '70%'}}>
                            保存
                        </Button>
                    </div>
                </Form.Item>
            </Form>
        </div>
    </div>
    )
}

export {PlatformApiEdit};
