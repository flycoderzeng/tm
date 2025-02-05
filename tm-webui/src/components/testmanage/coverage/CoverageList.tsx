import React, {ChangeEvent, useEffect, useState} from "react";
import {Button, Form, Input, message, Modal, Progress, Table} from "antd";
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {FormInstance} from "antd/lib/form";
import {StrUtils} from "../../../utils/StrUtils";
import {MathUtils} from "../../../utils/MathUtils";

interface IState {
}

const CoverageList: React.FC<IState> = (props) => {
    const [rows, setRows] = useState<any[]>([]);
    const [loading, setLoading] = useState(false);
    const [loading2, setLoading2] = useState(false);
    const [total, setTotal] = useState(0);
    const [visible, setVisible] = useState(false);
    const [searchValue, setSearchValue] = useState('');
    const [pagination, setPagination] = useState({
        current: 1,
        pageNum: 1,
        pageSize: 10,
        total: 0,
    });
    const [file, setFile] = useState<File>();
    const [ref] = useState(React.createRef<FormInstance>());

    useEffect(() => {
        load();
    }, [pagination.pageSize, pagination.pageNum]);

    function onChangePagination(pagination) {
        setPagination({
            ...pagination,
            pageNum: pagination.current,
            pageSize: pagination.pageSize,
            current: pagination.current,
        });
    }

    function execSearch(data: any) {
        setLoading(true);
        axios.get(ApiUrlConfig.QUERY_COVERAGE_INFO_LIST_URL + '?' + StrUtils.getGetParams(data)).then(resp => {
            if (resp.status !== 200) {
                message.error('加载列表失败');
            } else {
                const ret = resp.data;
                const rows: any[] = [];
                if (ret.data) {
                    ret.data.map((v: any) => {
                        v.attributes.id = v.id;
                        v.attributes.key = v.id;
                        v.attributes.instructionCover = (v.attributes.coveredInstructions*100/(v.attributes.coveredInstructions+v.attributes.missedInstructions)).toFixed(2);
                        v.attributes.instructionCoverInfo = v.attributes.coveredInstructions + ' of ' + (v.attributes.coveredInstructions+v.attributes.missedInstructions);
                        v.attributes.branchCover = (v.attributes.coveredBranches*100/(v.attributes.coveredBranches+v.attributes.missedBranches)).toFixed(2);
                        v.attributes.branchCoverInfo = v.attributes.coveredBranches + ' of ' + (v.attributes.coveredBranches+v.attributes.missedBranches);
                        v.attributes.cxtyCover = (v.attributes.coveredCxty*100/(v.attributes.coveredCxty+v.attributes.missedCxty)).toFixed(2);
                        v.attributes.cxtyCoverInfo = v.attributes.coveredCxty + ' of ' + (v.attributes.coveredCxty+v.attributes.missedCxty);
                        v.attributes.classCover = (v.attributes.coveredClasses*100/(v.attributes.coveredClasses+v.attributes.missedClasses)).toFixed(2);
                        v.attributes.classCoverInfo = v.attributes.coveredClasses + ' of ' + (v.attributes.coveredClasses+v.attributes.missedClasses);
                        v.attributes.methodCover = (v.attributes.coveredMethods*100/(v.attributes.coveredMethods+v.attributes.missedMethods)).toFixed(2);
                        v.attributes.methodCoverInfo = v.attributes.coveredMethods + ' of ' + (v.attributes.coveredMethods+v.attributes.missedMethods);
                        v.attributes.lineCover = (v.attributes.coveredLines*100/(v.attributes.coveredLines+v.attributes.missedLines)).toFixed(2);
                        v.attributes.lineCoverInfo = v.attributes.coveredLines + ' of ' + (v.attributes.coveredLines+v.attributes.missedLines);
                        rows.push(v.attributes);
                    });
                }
                let total: number = 0;
                if (ret.meta && ret.meta.page) {
                    total = ret.meta.page.totalRecords;
                }
                setRows(rows);
                setTotal(total);
            }
        }).catch(resp => {
            message.error(resp.message);
        }).finally(() => {
            setLoading(false);
        });
    }

    function load() {
        const data: any = {
            'page[totals]': '',
            'page[number]': pagination.current,
            'page[size]': pagination.pageSize,
            'sort': '-id'
        }
        let filter = 'status==0';
        if(searchValue) {
            if(filter !== '') {
                filter += ';';
            }
            filter += '(';
            if(MathUtils.isNumberSequence(searchValue)) {
                filter += 'id==' + searchValue + ',';
            }
            filter += 'name==*' + searchValue + '*';
            filter += ')';
        }
        if(filter !== '') {
            data['filter[coverage_info]'] = filter;
        }
        execSearch(data);
    }

    const onSearch = (value) => {
        setSearchValue(value);
        const data: any = {
            'page[totals]': '',
            'page[number]': pagination.current,
            'page[size]': pagination.pageSize,
            'sort': '-id'
        }
        let filter = 'status==0';
        if(value) {
            if(filter !== '') {
                filter += ';';
            }
            filter += '(';
            if(MathUtils.isNumberSequence(value)) {
                filter += 'id==' + value + ',';
            }
            filter += 'name==*' + value + '*';
            filter += ')';
        }
        if(filter !== '') {
            data['filter[coverage_info]'] = filter;
        }
        execSearch(data);
    }

    function addCoverage() {
        ref.current?.setFieldsValue({name: '', gitUrl: '', branch: '', branch2: '',
            gitCommitId1: '', gitCommitId2: '', file: null});
        setVisible(true);
    }

    function viewCoverage(record: any) {
        window.open("#/coverage/" + record.id);
    }

    const columns: any[] = [
        {
            title: '结果ID',
            dataIndex: 'id',
            key: 'id',
            width: 80,
        },
        {
            ellipsis: true,
            title: '名称',
            dataIndex: 'name',
            key: 'name',
            render: (text, record) => <a onClick={() => {viewCoverage(record);}}>{text}</a>,
        },
        {
            ellipsis: true,
            title: 'git地址',
            width: 200,
            dataIndex: 'gitUrl',
            key: 'gitUrl',
        },
        {
            ellipsis: true,
            title: 'branch',
            dataIndex: 'branch',
            key: 'branch',
        },
        {
            title: '指令覆盖',
            width: 200,
            dataIndex: 'instructionCover',
            key: 'instructionCover',
            render: (text, record) => (<div style={{display: 'flex', flexDirection: 'column'}}><Progress size="small" percent={record.instructionCover} strokeColor={record.instructionCover < 80 ? 'red' : 'green'} /><span style={{color: '#1890ff'}}>{record.instructionCoverInfo}</span></div>)
        },
        {
            title: '分支覆盖',
            width: 200,
            dataIndex: 'branchCover',
            key: 'branchCover',
            render: (text, record) => (<div style={{display: 'flex', flexDirection: 'column'}}><Progress size="small" percent={record.branchCover} strokeColor={record.branchCover < 80 ? 'red' : 'green'} /><span style={{color: '#1890ff'}}>{record.branchCoverInfo}</span></div>)
        },
        {
            title: '复杂度覆盖',
            width: 200,
            dataIndex: 'cxtyCover',
            key: 'cxtyCover',
            render: (text, record) => (<div style={{display: 'flex', flexDirection: 'column'}}><Progress size="small" percent={record.cxtyCover} strokeColor={record.cxtyCover < 80 ? 'red' : 'green'} /><span style={{color: '#1890ff'}}>{record.cxtyCoverInfo}</span></div>)
        },
        {
            title: '类覆盖',
            width: 200,
            dataIndex: 'classCover',
            key: 'classCover',
            render: (text, record) => (<div style={{display: 'flex', flexDirection: 'column'}}><Progress size="small" percent={record.classCover} strokeColor={record.classCover < 80 ? 'red' : 'green'} /><span style={{color: '#1890ff'}}>{record.classCoverInfo}</span></div>)
        },
        {
            title: '方法覆盖',
            width: 200,
            dataIndex: 'methodCover',
            key: 'methodCover',
            render: (text, record) => (<div style={{display: 'flex', flexDirection: 'column'}}><Progress size="small" percent={record.methodCover} strokeColor={record.methodCover < 80 ? 'red' : 'green'} /><span style={{color: '#1890ff'}}>{record.methodCoverInfo}</span></div>)
        },
        {
            title: '行覆盖',
            width: 200,
            dataIndex: 'lineCover',
            key: 'lineCover',
            render: (text, record) => {
                const div = <>
                    <div style={{display: 'flex', flexDirection: 'column'}}><Progress size="small"
                                                                                      percent={record.lineCover}
                                                                                      strokeColor={record.lineCover < 80 ? 'red' : 'green'}/><span
                        style={{color: '#1890ff'}}>{record.lineCoverInfo}</span></div>
                </>;
                return div;
            }
        },
        {
            title: '创建者',
            dataIndex: 'addUser',
            key: 'addUser',
        },
        {
            title: '创建时间',
            dataIndex: 'addTime',
            key: 'addTime',
        },
    ];
    const handleOk = () => {
        if(ref.current != null) {
            // ref.current.validateFields(['name', 'gitUrl', 'branch', 'file'], {}).then(resp => {});
            const values = ref.current.getFieldsValue();
            if(!values.name || values.name.trim() === '') {
                message.info('请输入名称!');
                return;
            }
            if(!values.gitUrl || values.gitUrl.trim() === '') {
                message.info('请输入代码的git地址!');
                return;
            }
            if(!values.branch || values.branch.trim() === '') {
                message.info('请输入代码分支名称!');
                return;
            }
            if(!values.file) {
                message.info('请上传jacoco覆盖率结果文件(xml格式)!!');
                return;
            }
            const formData = new FormData();
            const coverageInfo = {name: values.name, gitUrl: values.gitUrl, branch: values.branch, branch2: values.branch2,
                gitCommitId1: values.gitCommitId1, gitCommitId2: values.gitCommitId2};
            formData.append('coverageInfo', JSON.stringify(coverageInfo));
            formData.append('file', file as Blob, file?.name);
            setLoading2(true);
            axios.post(ApiUrlConfig.UPLOAD_WITH_COVERAGE_INFO_URL, formData).then(resp => {
                if (resp.status !== 200) {
                    message.error('生成覆盖率结果失败');
                } else {
                    const ret = resp.data;
                    if (ret.code !== 0) {
                        message.error(ret.message);
                    } else {
                        message.success('操作成功');
                        setVisible(false);
                        load();
                    }
                }
            }).catch(resp => {
                message.error(resp.message);
            }).finally(() => {
                setLoading2(false);
            });
        }
    };
    const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
        if (!e.target.files) {
            return;
        }
        setFile(e.target.files[0]);
    };

    return (<div className="card">
        <div className="card-header card-header-divider">覆盖率分析结果<span className="card-subtitle">代码覆盖率分析，为精准测试、健壮性测试提供指导。</span>
        </div>
        <div className="card-body">
            <div className="list-toolbar">
                <Input.Search placeholder="Id或者名称" onSearch={onSearch} enterButton style={{ width: 400, marginRight: '5px'}}/>
                <Button type="primary" onClick={()=>{addCoverage();}}>添加覆盖率分析结果</Button>
            </div>
            <Table
                footer={() => '共' + total + '条数据'}
                tableLayout="fixed"
                size="small"
                scroll={{ x: 'max-content' }}
                pagination={pagination}
                onChange={onChangePagination}
                columns={columns}
                dataSource={rows}
                loading={loading}/>
        </div>
        <Modal
            title="添加覆盖率分析结果"
            open={visible}
            onOk={() => setVisible(false)}
            onCancel={() => setVisible(false)}
            width={800}
            footer={[
                <Button key="back" onClick={() => setVisible(false)}>
                    取消
                </Button>,
                <Button key="submit" type="primary" htmlType="submit" loading={loading2} onClick={handleOk}>
                    提交
                </Button>,
            ]}
        >
            <div>
                <Form
                    name="basic"
                    labelCol={{ span: 8 }}
                    wrapperCol={{ span: 16 }}
                    initialValues={{name: '', gitUrl: '', branch: '', branch2: '',
                        gitCommitId1: '', gitCommitId2: '',}}
                    ref={ref}
                    autoComplete="off"
                >
                    <Form.Item
                        label="名称"
                        name="name"
                        rules={[{ required: true, message: '请输入名称!' }]}
                    >
                        <Input />
                    </Form.Item>
                    <Form.Item
                        label="Git URL"
                        name="gitUrl"
                        rules={[{ required: true, message: '请输入代码的git地址!' }]}
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        label="Branch"
                        name="branch"
                        rules={[{ required: true, message: '请输入代码分支名称!' }]}
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        label="Branch"
                        name="branch2"
                        rules={[{ required: false}]}
                    >
                        <Input placeholder={'两个分支之间比较输入此项!'} />
                    </Form.Item>

                    <Form.Item
                        label="git commit id 1"
                        name="gitCommitId1"
                        rules={[{ required: false}]}
                    >
                        <Input placeholder={'同一分支两个commit之间比较输入此项!'} />
                    </Form.Item>

                    <Form.Item
                        label="git commit id 2"
                        name="gitCommitId2"
                        rules={[{ required: false}]}
                    >
                        <Input placeholder={'同一分支两个commit之间比较输入此项!'} />
                    </Form.Item>

                    <Form.Item
                        label="覆盖率结果文件"
                        name="file"
                        rules={[{ required: true, message: '请选择jacoco覆盖率结果文件(xml格式)!'}]}
                    >
                        <Input onChange={handleFileChange} type={'file'} accept={'.xml'} placeholder={'jacoco覆盖率结果文件(xml格式)!'} />
                    </Form.Item>
                </Form>
            </div>
        </Modal>
    </div>)
}
export {CoverageList}