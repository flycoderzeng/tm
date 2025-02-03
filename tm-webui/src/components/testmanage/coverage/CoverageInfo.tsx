import React, {useEffect, useState} from "react";
import type {DescriptionsProps, TableColumnsType} from 'antd';
import {Breadcrumb, Descriptions, message, Progress, Table} from 'antd';
import axios from "axios";
import {ApiUrlConfig} from "../../../config/api.url";
import {DateUtils} from "../../../utils/DateUtils";
import {RandomUtils} from "../../../utils/RandomUtils";

interface DataType {
    key: React.Key;
    name: string;
    desc?: string;
    type: string;
    instructionCover: number;
    instructionCoverInfo: string;
    branchCover: number;
    branchCoverInfo: string;
    cxtyCover: number;
    cxtyCoverInfo: string;
    classCover: number;
    classCoverInfo: string;
    methodCover: number;
    methodCoverInfo: string;
    lineCover: number;
    lineCoverInfo: string;
    methodCoverageResults?: DataType[];
    counters?: any[];
}
interface IState {
}

const CoverageInfo: React.FC<IState> = (props) => {
    const [id, setId] = useState((props as any).match.params.id);
    const [title, setTitle] = useState('覆盖率分析结果');
    const [hiddenClassCover, setHiddenClassCover] = useState(false);
    const [summaryTitle, setSummaryTitle] = useState('覆盖率分析结果');
    const [className, setClassName] = useState('');
    const [packageName, setPackageName] = useState('');
    const [breadcrumbItems, setBreadcrumbItems] = useState([
        {
            title: <a href="javascript:return;" onClick={() => load(id)}>{title}</a>,
        },
    ]);
    const [instructionCover, setInstructionCover] = useState(0);
    const [instructionCoverInfo, setInstructionCoverInfo] = useState('');
    const [branchCover, setBranchCover] = useState(0);
    const [branchCoverInfo, setBranchCoverInfo] = useState('');
    const [cxtyCover, setCxtyCover] = useState(0);
    const [cxtyCoverInfo, setCxtyCoverInfo] = useState('');
    const [classCover, setClassCover] = useState(0);
    const [classCoverInfo, setClassCoverInfo] = useState('');
    const [methodCover, setMethodCover] = useState(0);
    const [methodCoverInfo, setMethodCoverInfo] = useState('');
    const [lineCover, setLineCover] = useState(0);
    const [lineCoverInfo, setLineCoverInfo] = useState('');
    const [createInfo, setCreateInfo] = useState('');
    const [rows, setRows] = useState<DataType[]>([]);

    useEffect(() => {
        setId((props as any).match.params.id);
        load((props as any).match.params.id);
    }, [(props as any).match.params.id]);

    function load(infoId: number) {
        axios.post(ApiUrlConfig.GET_COVERAGE_INFO_URL, {id: infoId}).then(resp => {
            if (resp.status !== 200) {
                message.error('加载失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    setTitle(ret.data.name);
                    setSummaryTitle(ret.data.name);
                    setBreadcrumbItems([{
                        title: <a href="javascript:;" onClick={() => load(infoId)}>{ret.data.name}</a>,
                    },]);
                    if((ret.data.coveredInstructions+ret.data.missedInstructions) < 1) {
                        setInstructionCover(0);
                    }else {
                        setInstructionCover(Number((ret.data.coveredInstructions * 100 / (ret.data.coveredInstructions + ret.data.missedInstructions)).toFixed(2)));
                    }
                    setInstructionCoverInfo(ret.data.coveredInstructions + ' of ' + (ret.data.coveredInstructions+ret.data.missedInstructions));
                    if((ret.data.coveredBranches+ret.data.missedBranches) < 1) {
                        setBranchCover(0);
                    }else{
                        setBranchCover(Number((ret.data.coveredBranches * 100 / (ret.data.coveredBranches + ret.data.missedBranches)).toFixed(2)));
                    }
                    setBranchCoverInfo(ret.data.coveredBranches + ' of ' + (ret.data.coveredBranches+ret.data.missedBranches));
                    if((ret.data.coveredCxty+ret.data.missedCxty) < 1) {
                        setCxtyCover(0);
                    }else {
                        setCxtyCover(Number((ret.data.coveredCxty * 100 / (ret.data.coveredCxty + ret.data.missedCxty)).toFixed(2)));
                    }
                    setCxtyCoverInfo(ret.data.coveredCxty + ' of ' + (ret.data.coveredCxty+ret.data.missedCxty));
                    if((ret.data.coveredClasses+ret.data.missedClasses) < 1) {
                        setClassCover(0);
                    }else {
                        setClassCover(Number((ret.data.coveredClasses * 100 / (ret.data.coveredClasses + ret.data.missedClasses)).toFixed(2)));
                    }
                    setClassCoverInfo(ret.data.coveredClasses + ' of ' + (ret.data.coveredClasses+ret.data.missedClasses));
                    if((ret.data.coveredMethods+ret.data.missedMethods) < 1) {
                        setMethodCover(0);
                    }else {
                        setMethodCover(Number((ret.data.coveredMethods * 100 / (ret.data.coveredMethods + ret.data.missedMethods)).toFixed(2)));
                    }
                    setMethodCoverInfo(ret.data.coveredMethods + ' of ' + (ret.data.coveredMethods+ret.data.missedMethods));
                    if((ret.data.coveredLines+ret.data.missedLines) < 1) {
                        setLineCover(0);
                    }else {
                        setLineCover(Number((ret.data.coveredLines * 100 / (ret.data.coveredLines + ret.data.missedLines)).toFixed(2)));
                    }
                    setLineCoverInfo(ret.data.coveredLines + ' of ' + (ret.data.coveredLines+ret.data.missedLines));

                    setCreateInfo(ret.data.addUser + ' ' + DateUtils.format(ret.data.addTime));
                }
            }
        }).catch(resp => {
            message.error(resp.message);
        }).finally(() => {
            loadPackageCoverInfo(infoId);
        });
    }

    function loadPackageCoverInfo(infoId: number) {
        axios.post(ApiUrlConfig.GET_PACKAGE_COVER_INFO_URL, {id: infoId}).then(resp => {
            if (resp.status !== 200) {
                message.error('加载失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    for (let i = 0; i < ret.data.length; i++) {
                        const row = ret.data[i];
                        handleCounters(row);
                    }
                    setRows(ret.data);
                }
            }
        }).catch(resp => {
            message.error(resp.message);
        }).finally(() => {

        });
    }

    function open(record: DataType) {
        if(record.type === 'package') {
            setPackageName(record.name);
            setHiddenClassCover(false);
            openPackage(record);
        }else if(record.type === 'class') {
            setClassName(record.name);
            setHiddenClassCover(true);
            if(record.methodCoverageResults?.length === 0) {
                openSourceCode();
            }else {
                openClass(record);
            }
        }else if(record.type === 'method') {
            openSourceCode();
        }
    }

    function handleCounters(row): void {
        for (let j = 0; j < row.counters.length; j++) {
            if (row.counters[j].type === 'INSTRUCTION') {
                if ((row.counters[j].covered + row.counters[j].missed) < 1) {
                    row.instructionCover = 0;
                    row.instructionCoverInfo = '0 of 0';
                } else {
                    row.instructionCover = Number((row.counters[j].covered * 100 / (row.counters[j].covered + row.counters[j].missed)).toFixed(2));
                    row.instructionCoverInfo = row.counters[j].covered + ' of ' + (row.counters[j].covered + row.counters[j].missed);
                }
            } else if (row.counters[j].type === 'BRANCH') {
                if ((row.counters[j].covered + row.counters[j].missed) < 1) {
                    row.branchCover = 0;
                    row.branchCoverInfo = '0 of 0';
                } else {
                    row.branchCover = Number((row.counters[j].covered * 100 / (row.counters[j].covered + row.counters[j].missed)).toFixed(2));
                    row.branchCoverInfo = row.counters[j].covered + ' of ' + (row.counters[j].covered + row.counters[j].missed);
                }
            } else if (row.counters[j].type === 'COMPLEXITY') {
                if ((row.counters[j].covered + row.counters[j].missed) < 1) {
                    row.cxtyCover = 0;
                    row.cxtyCoverInfo = '0 of 0';
                } else {
                    row.cxtyCover = Number((row.counters[j].covered * 100 / (row.counters[j].covered + row.counters[j].missed)).toFixed(2));
                    row.cxtyCoverInfo = row.counters[j].covered + ' of ' + (row.counters[j].covered + row.counters[j].missed);
                }
            } else if (row.counters[j].type === 'METHOD') {
                if ((row.counters[j].covered + row.counters[j].missed) < 1) {
                    row.methodCover = 0;
                    row.methodCoverInfo = '0 of 0';
                } else {
                    row.methodCover = Number((row.counters[j].covered * 100 / (row.counters[j].covered + row.counters[j].missed)).toFixed(2));
                    row.methodCoverInfo = row.counters[j].covered + ' of ' + (row.counters[j].covered + row.counters[j].missed);
                }
            } else if (row.counters[j].type === 'CLASS') {
                if ((row.counters[j].covered + row.counters[j].missed) < 1) {
                    row.classCover = 0;
                    row.classCoverInfo = '0 of 0';
                } else {
                    row.classCover = Number((row.counters[j].covered * 100 / (row.counters[j].covered + row.counters[j].missed)).toFixed(2));
                    row.classCoverInfo = row.counters[j].covered + ' of ' + (row.counters[j].covered + row.counters[j].missed);
                }
            } else if (row.counters[j].type === 'LINE') {
                if ((row.counters[j].covered + row.counters[j].missed) < 1) {
                    row.lineCover = 0;
                    row.lineCoverInfo = '0 of 0';
                } else {
                    row.lineCover = Number((row.counters[j].covered * 100 / (row.counters[j].covered + row.counters[j].missed)).toFixed(2));
                    row.lineCoverInfo = row.counters[j].covered + ' of ' + (row.counters[j].covered + row.counters[j].missed);
                }
            }
        }
    }

    function openPackage2(id: number, record: DataType) {
        axios.post(ApiUrlConfig.OPEN_PACKAGE_COVER_INFO_URL, {id: id, packageName: record.name}).then(resp => {
            if (resp.status !== 200) {
                message.error('加载包覆盖率信息失败！');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    for (let i = 0; i < ret.data.length; i++) {
                        const row = ret.data[i];
                        row.name = row.sourceFileName;
                        handleCounters(row);
                    }
                    const row1: DataType = {
                        cxtyCover: 0, cxtyCoverInfo: "", key: RandomUtils.getKey(), name: '总计', type: 'total', counters: record.counters,
                        instructionCover: 0, instructionCoverInfo: '', branchCover: 0, branchCoverInfo: '', methodCoverInfo: '',
                        methodCover: 0, lineCoverInfo: '', lineCover: 0, classCover: 0, classCoverInfo: ''};
                    handleCounters(row1);
                    ret.data.push(row1);
                    setRows(ret.data);
                }
            }
        }).catch(resp => {
            message.error(resp.message);
        }).finally();
    }

    function openPackage(record: DataType) {
        setBreadcrumbItems([breadcrumbItems[0], {
            title: <a href="javascript:;" className={'el_package'} onClick={() => openPackage(record)}><span style={{marginLeft: 15}}>{record.name}</span></a>,
        },]);
        openPackage2(id, record);
        setHiddenClassCover(false);
    }

    function openClass(record: DataType) {
        setBreadcrumbItems([breadcrumbItems[0], breadcrumbItems[1], {
            title: <span className={'el_class'}>{record.name}</span>,
        },]);
        openClass2(record);
        setHiddenClassCover(true);
    }

    function openClass2(record: DataType) {
        const dataTypes = record.methodCoverageResults||[];
        for (let i = 0; i < dataTypes.length; i++) {
            const desc = dataTypes[i].desc;
            if(desc) {
                dataTypes[i].name += parseJavaMethodDescriptor(desc.substring(desc.indexOf('('), desc.lastIndexOf(')')+1));
            }
            handleCounters(dataTypes[i]);
        }
        const row1: DataType = {
            cxtyCover: 0, cxtyCoverInfo: "", key: RandomUtils.getKey(), name: '总计', type: 'total', counters: record.counters,
            instructionCover: 0, instructionCoverInfo: '', branchCover: 0, branchCoverInfo: '', methodCoverInfo: '',
            methodCover: 0, lineCoverInfo: '', lineCover: 0, classCover: 0, classCoverInfo: ''};
        handleCounters(row1);
        dataTypes.push(row1);
        setRows(dataTypes);
    }

    function openSourceCode() {
        axios.post(ApiUrlConfig.OPEN_CLASS_COVER_INFO_URL, {id: id, packageName: packageName, sourceFileName: className}).then(resp => {
            if (resp.status !== 200) {
                message.error('加载失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {

                }
            }
        }).catch(resp => {
            message.error(resp.message);
        }).finally();
    }

    function parseJavaMethodDescriptor(descriptor) {
        // 去掉括号
        const paramsPart = descriptor.slice(1, descriptor.indexOf(')'));

        // 定义类型映射
        const typeMap = {
            'I': 'int',
            'J': 'long',
            'S': 'short',
            'B': 'byte',
            'C': 'char',
            'F': 'float',
            'D': 'double',
            'Z': 'boolean',
            'V': 'void',
        };

        const result :string[] = [];
        let i = 0;

        while (i < paramsPart.length) {
            if (paramsPart[i] === 'L') {
                // 处理对象类型（以 'L' 开头，以 ';' 结尾）
                const endIndex = paramsPart.indexOf(';', i);
                const className = paramsPart.slice(i + 1, endIndex).replace(/\//g, '.');
                result.push(className);
                i = endIndex + 1;
            } else if (paramsPart[i] === '[') {
                // 处理数组类型
                let arrayDepth = 1;
                while (paramsPart[i + arrayDepth] === '[') {
                    arrayDepth++;
                }
                const baseType = paramsPart[i + arrayDepth];
                if (baseType === 'L') {
                    const endIndex = paramsPart.indexOf(';', i + arrayDepth);
                    const className = paramsPart.slice(i + arrayDepth + 1, endIndex).replace(/\//g, '.');
                    result.push(`${'[]'.repeat(arrayDepth)}${className}`);
                    i = endIndex + 1;
                } else {
                    result.push(`${'[]'.repeat(arrayDepth)}${typeMap[baseType]}`);
                    i += arrayDepth + 1;
                }
            } else {
                // 处理基本类型
                result.push(typeMap[paramsPart[i]]);
                i++;
            }
        }
        for (let j = 0; j < result.length; j++) {
            if(result[j].startsWith('java.lang.')) {
                result[j] = result[j].substring(10);
            }
        }

        return `(${result.join(', ')})`;
    }

    const columns: TableColumnsType<DataType> = [
        {
            title: 'Element',
            dataIndex: 'name',
            width: 300,
            sorter: (a, b) => {
                if(a.name === '总计') {
                    return -1;
                } else {
                    return a.name.localeCompare(b.name);
                }},
            render: (text, record) => <span><a className={record.type === 'package' ? 'el_package' : record.type === 'class' ? 'el_class' : record.type === 'method' ? 'el_method': ''} onClick={() => {open(record);}}>{text}</a></span>
        },
        {
            title: '指令覆盖',
            dataIndex: 'instructionCover',
            width: 170,
            sorter: (a, b) => {
                if(a.name === '总计') {
                    return -1;
                } else {
                    return a.instructionCover - b.instructionCover;
                }
            },
            render: (text, record) => <span className={'span-cover-info-td'}><Progress style={{width: 150, flexGrow: 0.7}} size="small" strokeColor={record.instructionCover < 80 ? 'red' : 'green'} percent={record.instructionCover} /><span className={'span-cover-info'}>{record.instructionCoverInfo}</span></span>
        },
        {
            title: '分支覆盖',
            width: 170,
            dataIndex: 'branchCover',
            sorter: (a, b) => {
                if(a.name === '总计') {
                    return -1;
                } else {
                    return a.branchCover - b.branchCover;
                }
            },
            render: (text, record) => <span className={'span-cover-info-td'}><Progress style={{width: 150, flexGrow: 0.7}} size="small" strokeColor={record.branchCover < 80 ? 'red' : 'green'} percent={record.branchCover} /><span className={'span-cover-info'}>{record.branchCoverInfo}</span></span>
        },
        {
            title: '复杂度覆盖',
            width: 170,
            dataIndex: 'cxtyCover',
            sorter: (a, b) => {
                if(a.name === '总计') {
                    return -1;
                } else {
                    return a.cxtyCover - b.cxtyCover;
                }
            },
            render: (text, record) => <span className={'span-cover-info-td'}><Progress style={{width: 150, flexGrow: 0.7}} size="small" strokeColor={record.cxtyCover < 80 ? 'red' : 'green'} percent={record.cxtyCover} /><span className={'span-cover-info'}>{record.cxtyCoverInfo}</span></span>
        },
        {
            title: '类覆盖',
            width: 170,
            hidden: hiddenClassCover,
            dataIndex: 'classCover',
            sorter: (a, b) => {
                if(a.name === '总计') {
                    return -1;
                } else {
                    return a.classCover - b.classCover;
                }
            },
            render: (text, record) => <span className={'span-cover-info-td'}><Progress style={{width: 150, flexGrow: 0.7}} size="small" strokeColor={record.classCover < 80 ? 'red' : 'green'} percent={record.classCover} /><span className={'span-cover-info'}>{record.classCoverInfo}</span></span>
        },
        {
            title: '方法覆盖',
            width: 170,
            dataIndex: 'methodCover',
            sorter: (a, b) => {
                if(a.name === '总计') {
                    return -1;
                } else {
                    return a.methodCover - b.methodCover;
                }
            },
            render: (text, record) => <span className={'span-cover-info-td'}><Progress style={{width: 150, flexGrow: 0.7}} size="small" strokeColor={record.methodCover < 80 ? 'red' : 'green'} percent={record.methodCover} /><span className={'span-cover-info'}>{record.methodCoverInfo}</span></span>
        },
        {
            title: '行覆盖',
            width: 170,
            dataIndex: 'lineCover',
            sorter: (a, b) => {
                if(a.name === '总计') {
                    return -1;
                } else {
                    return a.lineCover - b.lineCover;
                }
            },
            render: (text, record) => {
                const span = <span className={'span-cover-info-td'}><Progress style={{width: 150, flexGrow: 0.7}}
                                                                                size="small"
                                                                                strokeColor={record.lineCover < 80 ? 'red' : 'green'}
                                                                                percent={record.lineCover}/><span
                    className={'span-cover-info'}>{record.lineCoverInfo}</span></span>;
                return span;
            }
        },
    ];

    const items: DescriptionsProps['items'] = [
        {
            key: '1',
            label: '指令覆盖',
            children: (<div style={{width: 200, display: 'flex', flexDirection: 'column'}}><Progress size="small" percent={instructionCover} strokeColor={instructionCover < 80 ? 'red' : 'green'} /><span style={{color: '#1890ff'}}>{instructionCoverInfo}</span></div>),
        },
        {
            key: '2',
            label: '分支覆盖',
            children: (<div style={{width: 200, display: 'flex', flexDirection: 'column'}}><Progress size="small" percent={branchCover} strokeColor={branchCover < 80 ? 'red' : 'green'} /><span style={{color: '#1890ff'}}>{branchCoverInfo}</span></div>),
        },
        {
            key: '3',
            label: '复杂度覆盖',
            children: (<div style={{width: 200, display: 'flex', flexDirection: 'column'}}><Progress size="small" percent={cxtyCover} strokeColor={cxtyCover < 80 ? 'red' : 'green'} /><span style={{color: '#1890ff'}}>{cxtyCoverInfo}</span></div>),
        },
        {
            key: '4',
            label: '类覆盖',
            children: (<div style={{width: 200, display: 'flex', flexDirection: 'column'}}><Progress size="small" percent={classCover} strokeColor={classCover < 80 ? 'red' : 'green'} /><span style={{color: '#1890ff'}}>{classCoverInfo}</span></div>),
        },
        {
            key: '5',
            label: '方法覆盖',
            children: (<div style={{width: 200, display: 'flex', flexDirection: 'column'}}><Progress size="small" percent={methodCover} strokeColor={methodCover < 80 ? 'red' : 'green'} /><span style={{color: '#1890ff'}}>{methodCoverInfo}</span></div>),
        },
        {
            key: '6',
            label: '行覆盖',
            children: (<div style={{width: 200, display: 'flex', flexDirection: 'column'}}><Progress size="small" percent={lineCover} strokeColor={lineCover < 80 ? 'red' : 'green'} /><span style={{color: '#1890ff'}}>{lineCoverInfo}</span></div>),
        },
        {
            key: '7',
            label: '创建信息',
            children: <span>{createInfo}</span>,
        },
    ];

    return (<div style={{padding: '10px'}}>
        <Breadcrumb
            style={{paddingBottom: 15}}
            items={breadcrumbItems}
        />
        <Descriptions title={'覆盖率总计'} items={items} />
        <div style={{paddingTop: '10px'}}>
            <Table<DataType>
                scroll={{x: 1350}}
                columns={columns}
                dataSource={rows}
                pagination={false}
                onRow={(record) => {
                    if(record.type === 'total') {
                        return {
                            style: {background: 'aliceblue'}
                        }
                    }
                    else {
                        return {};
                    }
                }}
                size="small" />
        </div>
    </div>)
}
export {CoverageInfo}