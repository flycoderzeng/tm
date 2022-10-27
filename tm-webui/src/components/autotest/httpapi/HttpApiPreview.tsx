import React, {useState} from "react";
import { Row, Col } from 'antd';

import {HttpApiModel} from "../../../entities/HttpApiModel";
import moment from "moment";
import {CommonTableShow} from "../../common/CommonTableShow";

interface IState {
    appApi: HttpApiModel,
}


const HttpApiPreview: React.FC<IState> = (props) => {
    const {appApi} = props;
    const addTime = appApi.addTime && moment(new Date(appApi.addTime)).format('YYYY-MM-DD HH:mm:ss');
    const lastModifyTime = appApi.lastModifyTime && moment(new Date(appApi.lastModifyTime)).format('YYYY-MM-DD HH:mm:ss');
    const headerColumns = [
        {
            title: '参数名称',
            dataIndex: 'name',
            key: 'key',
            render: text => <span>{text}</span>,
        },
        {
            title: '参数值',
            dataIndex: 'value',
        },
        {
            title: '示例',
            dataIndex: 'example',
        },
        {
            title: '备注',
            dataIndex: 'remark',
        },
    ];
    const paramsColumns = [
        {
            title: '参数名称',
            dataIndex: 'name',
            key: 'key',
            render: text => <span>{text}</span>,
        },
        {
            title: '是否必须',
            dataIndex: 'required',
            render: text => <span>{text === '1' ? '是' : '否'}</span>,
        },
        {
            title: '示例',
            dataIndex: 'example',
        },
        {
            title: '备注',
            dataIndex: 'remark',
        },
    ];

    const formColumns = [
        {
            title: '参数名称',
            dataIndex: 'name',
            key: 'key',
            render: text => <span>{text}</span>,
        },
        {
            title: '参数类型',
            dataIndex: 'type',
            render: text => <span>{text === 'text' ? '文本' : '文件'}</span>,
        },
        {
            title: '是否必须',
            dataIndex: 'required',
            render: text => <span>{text === '1' ? '是' : '否'}</span>,
        },
        {
            title: '示例',
            dataIndex: 'example',
        },
        {
            title: '备注',
            dataIndex: 'remark',
        },
    ];

    const jsonColumns = [
        {
            title: '参数名称',
            dataIndex: 'name',
            key: 'key',
            render: text => <span>{text}</span>,
        },
        {
            title: '类型',
            render: (text, record) => <span>{record.define.type}</span>,
        },
        {
            title: '是否必须',
            dataIndex: 'required',
            render: text => <span>{text === '1' ? '是' : '否'}</span>,
        },
        {
            title: '备注',
            dataIndex: 'remark',
            render: text => <span>{text}</span>,
        },
        {
            title: '其他信息',
            render: (text, record) => <span></span>,
        },
    ];

    function renderBody() {
        if(appApi.reqBodyType === '1') {
            return <CommonTableShow columns={formColumns} data={appApi.reqBodyForm} size={'small'}></CommonTableShow>
        }else if(appApi.reqBodyType === '2') {
            return <CommonTableShow columns={paramsColumns} data={appApi.reqBodyKv} size={'small'}></CommonTableShow>
        }else if(appApi.reqBodyType === '3') {
            return <pre>{appApi.reqBodyMessage}</pre>
        }
    }

    return (<div>
        <h3 className="api-basic-info-title">基本信息</h3>
        <div style={{background: '#f7f7f7', padding: '16px'}}>
            <Row className="basic-info-row">
                <Col span={4}><span className="ctrl-span">名称:</span></Col>
                <Col span={20}>{appApi.name}</Col>
            </Row>
            <Row className="basic-info-row">
                <Col span={4}><span className="ctrl-span">描述:</span></Col>
                <Col span={20}>{appApi.description}</Col>
            </Row>
            <Row className="basic-info-row">
                <Col span={4}><span className="ctrl-span">创建人:</span></Col>
                <Col span={8}>{appApi.addUser}</Col>
                <Col span={4}><span className="ctrl-span">创建时间:</span></Col>
                <Col span={8}>{addTime}</Col>
            </Row>
            <Row className="basic-info-row">
                <Col span={4}><span className="ctrl-span">修改人:</span></Col>
                <Col span={8}>{appApi.lastModifyUser}</Col>
                <Col span={4}><span className="ctrl-span">修改时间:</span></Col>
                <Col span={8}>{lastModifyTime}</Col>
            </Row>
            <Row className="basic-info-row">
                <Col span={4}><span className="ctrl-span">接口路径:</span></Col>
                <Col span={20}>{appApi.url}</Col>
            </Row>
        </div>

        <h3 className="api-basic-info-title">备注</h3>
        <div style={{background: '#f7f7f7', marginTop: '10px', padding: '16px'}}>
            <div dangerouslySetInnerHTML={{__html: appApi.remark}}></div>
        </div>

        <h3 className="api-basic-info-title">请求参数</h3>
        <div style={{background: '#f7f7f7', marginTop: '10px', padding: '16px'}}>
            <div style={{marginBottom: '30px'}}>
                <h3>Headers: </h3>
                <CommonTableShow columns={headerColumns} data={appApi.reqHeaders} size={'small'}></CommonTableShow>
            </div>
            <div style={{marginBottom: '30px'}}>
                <h3>Params: </h3>
                <CommonTableShow columns={paramsColumns} data={appApi.reqParams} size={'small'}></CommonTableShow>
            </div>
            <div>
                <h3>Body: </h3>
                {renderBody()}
            </div>
        </div>

        <h3 className="api-basic-info-title">返回数据</h3>
        <div style={{background: '#f7f7f7', marginTop: '10px', padding: '16px'}}>
            <div>
                <pre>{appApi.reqBodyMessage}</pre>
            </div>
        </div>
    </div>)
}

export {HttpApiPreview}
