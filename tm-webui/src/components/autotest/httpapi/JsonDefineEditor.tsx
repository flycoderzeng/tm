import React, {useState} from "react";

import {Input, Checkbox, Select, Tooltip, Menu, Dropdown, Modal, Button} from 'antd';
import { CaretRightOutlined,CaretDownOutlined,PlusOutlined,CloseOutlined,SettingOutlined,EditOutlined } from '@ant-design/icons';
import {RandomUtils} from "../../../utils/RandomUtils";
const Option = Select.Option;
const { TextArea } = Input;
interface IState {
    defineRows: any[],
    id: number,
}
const suffix = (
    <EditOutlined
        className="input-with-editor-icon"
    />
);
const findNode = (data, key, parent: boolean) => {
    for (let i = 0; i < data.length; i++) {
        if (data[i].key === key && !parent) {
            return data[i];
        }
        if(parent) {
            for (let j = 0; j < data[i].children.length; j++) {
                if(data[i].children[j].key === key) {
                    return data[i];
                }
            }
        }
        if (data[i].children && data[i].children.length > 0) {
            return findNode(data[i].children, key, parent);
        }
    }
};

const JsonDefineEditor: React.FC<IState> = (props) => {
    const [defineRows, setDefineRows] = useState(props.defineRows);
    const [id, setId] = useState(props.id);
    const [isRemarkModalVisible, setIsRemarkModalVisible] = useState(false);
    const [isDetailModalVisible, setIsDetailModalVisible] = useState(false);
    const [remark, setRemark] = useState('');
    const [currRow, setCurrRow] = useState({description: ''});
    if(id !== props.id) {
        setId(props.id);
    }
    if(JSON.stringify(defineRows) !== JSON.stringify(props.defineRows)) {
        setDefineRows(props.defineRows);
    }

    function expandDefine(row: any) {
        if(row.expanded) {
            row.expanded = false;
            row.displayChild = 'none';
        }else{
            row.expanded = true;
            row.displayChild = 'block';
        }

        setDefineRows([...defineRows]);
    }

    function renderLeftIcon(row: any) {
        if(!(row.define.type === 'object')) {
            return <div></div>
        }
        if(row.expanded) {
            return <CaretDownOutlined className="expand-icon" onClick={() => {expandDefine(row)}}/>
        }else{
            return <CaretRightOutlined className="expand-icon" onClick={() => {expandDefine(row)}}/>
        }
    }

    function onCloseClick(row: any) {
        const data = [...defineRows];
        let ind = -1;
        const parent = findNode(data, row.key, true);

        for (let i = 0; i < parent.children.length; i++) {
            if(parent.children[i].key === row.key) {
                ind = i;
                break;
            }
        }
        if(ind > -1) {
            parent.children.splice(ind, 1);
        }
        setDefineRows(data);
    }

    function onSetDetailClick(row: any) {
        setIsDetailModalVisible(true);
    }

    function renderSettingIcon(row: any) {
        return (<span onClick={() => {onSetDetailClick(row);}} className="define-row-icon-span icon-setting"><SettingOutlined /></span>)
    }

    function renderCloseIcon(row: any) {
        if(row.level === 0) {
            return (<span></span>)
        }
        if(row.name === 'Items' && row.disabled) {
            return (<span></span>)
        }
        return (
            <span onClick={() => {onCloseClick(row);}} className="define-row-icon-span icon-close"><CloseOutlined /></span>
        )
    }

    function renderPlusIcon(row: any) {
        const menu = (
            <Menu>
                <Menu.Item key="0" className="ant-tree-title" onClick={() => {
                    onClickPlus(row, true);
                }}>
                    添加兄弟节点
                </Menu.Item>
                <Menu.Item key="1" className="ant-tree-title" onClick={() => {
                    onClickPlus(row, false);
                }}>
                    添加子节点
                </Menu.Item>
            </Menu>
        );

        if(row.name === 'Items' && row.disabled && row.define.type !== 'object') {
            return (<span></span>)
        }

        if(row.level === 0 && row.define.type !== 'object') {
            return (<span></span>)
        }

        let title = "添加兄弟节点";
        let brother = true;
        if(row.level === 0 && row.define.type === 'object') {
            title = "添加子节点";
            brother = false;
        }else if(row.name === 'Items' && row.disabled && row.define.type === 'object') {
            title = "添加子节点";
            brother = false;
        }else if(row.define.type === 'object') {
            title = "添加兄弟/子节点";
        }
        if(title === "添加兄弟/子节点") {
            return (
                <Tooltip title={title}>
                    <Dropdown overlay={menu}>
                        <span className="define-row-icon-span icon-plus"><PlusOutlined /></span>
                    </Dropdown>
                </Tooltip>
            )
        }
        return (
            <Tooltip title={title}>
                <span className="define-row-icon-span icon-plus" onClick={() => {
                    onClickPlus(row, brother);
                }}><PlusOutlined /></span>
            </Tooltip>
        )
    }

    function onClickPlus(row: any, brother: boolean) {
        const data = [...defineRows];
        const node = {name: '', paddingLeft: '0px', expanded: true, title: '',
            description: '',
            disabled: false, level: (row.level+1), key: RandomUtils.getKey(),
            displayChild: 'block',
            children: [], define: {type: 'string'},
            required: false};
        const currRow = findNode(data, row.key, false);
        if(brother) {
            node.level = currRow.level;
            // 在row的位置后插入
            let ind = -1;
            const parent = findNode(data, row.key, true);

            for (let i = 0; i < parent.children.length; i++) {
                if(parent.children[i].key === row.key) {
                    ind = i;
                    break;
                }
            }
            if(ind > -1 && row.define.type !== 'array') {
                parent.children.splice(ind+1, 0, node);
            }else if(ind > -1 && row.define.type === 'array') {
                parent.children.splice(ind, 0, node);
            }
        }else{
            currRow.children.push(node);
        }
        node.paddingLeft = node.level * 10 + 'px';
        setDefineRows(data);
    }

    function onChangeName(value: any, row: any) {
        row.name = value;
        setDefineRows([...defineRows]);
    }

    function onChangeRequired(checked: boolean, row: any) {
        row.required = checked;
        setDefineRows([...defineRows]);
    }

    function onChangeType(value: string, row: any) {
        row.define.type = value;
        row.children = [];
        if(value === 'array') {
            const childRow = {name: 'Items', paddingLeft: '0px', expanded: true, key: RandomUtils.getKey(),
                disabled: true, level: (row.level+1),
                displayChild: 'block',
                children: [], define: {type: 'string'},
                required: false};
            childRow.paddingLeft = childRow.level * 10 + 'px';
            row.children.push(childRow);
        }
        setDefineRows([...defineRows]);
    }

    function onChangeTitle(value: string, row: any) {
        row.title = value;
        setDefineRows([...defineRows]);
    }

    function onChangeDescription(value: string, row: any) {
        row.description = value;
        setDefineRows([...defineRows]);
    }

    function showRemarkSettingModal(row: any) {
        setRemark(row.description);
        setCurrRow(row);
        setIsRemarkModalVisible(true);
    }

    function handleOkSetRemark() {
        currRow.description = remark;
        setIsRemarkModalVisible(false);
        setDefineRows([...defineRows]);
    }

    function handleCancelSetRemark () {
        setIsRemarkModalVisible(false);
    }

    function handleOkSetDetail() {

    }

    function handleCancelSetDetail() {
        setIsDetailModalVisible(false);
    }

    function onChangeRemark(e: any) {
        setRemark(e.target.value);
    }

    function generate(row: any) {
        if(!row) {
            return (<div></div>)
        }
        let list;
        if(row.children && row.children.length > 0) {
            list = row.children.map(child => {
                return generate(child);
            });
        }
        return (<div key={row.key}>
            <div className="define-row">
                <div className="define-item-col-8">
                    <div className="flex-row" style={{paddingLeft: row.paddingLeft}}>
                    <span className="define-row-item define-row-icon-span icon-expand">
                        {
                            renderLeftIcon(row)
                        }
                    </span>
                        <Input className="define-row-item" disabled={row.disabled} value={row.name} onChange={(value) => {onChangeName(value.target.value, row);}}/>
                        <Checkbox className="define-row-item" onChange={(e) => {onChangeRequired(e.target.checked, row);}} disabled={row.disabled} defaultChecked={row.required}></Checkbox>
                    </div>
                </div>
                <div className="define-row-item parameter-type-min-width">
                    <Select onChange={(value) => {
                        onChangeType(value, row);
                    }} defaultValue={row.define.type} className="select-max-width">
                        <Option value="string">string</Option>
                        <Option value="number">number</Option>
                        <Option value="array">array</Option>
                        <Option value="object">object</Option>
                        <Option value="boolean">boolean</Option>
                        <Option value="integer">integer</Option>
                    </Select>
                </div>
                <div className="define-row-item define-item-col-3">
                    <Input onChange={(e) => {onChangeTitle(e.target.value, row);}} value={row.title} placeholder="中文标题" />
                </div>
                <div className="define-row-item define-item-col-3">
                    <Input.Group compact style={{display: 'flex'}}>
                        <Input onChange={(e) => {onChangeDescription(e.target.value, row);}} value={row.description} placeholder="备注" />
                        <Button style={{width: '45px'}} type="default" icon={suffix} onClick={() => {showRemarkSettingModal(row);}} />
                    </Input.Group>
                </div>
                <div className="define-row-item define-row-tool">
                    {renderSettingIcon(row)}
                    {renderCloseIcon(row)}
                    {renderPlusIcon(row)}
                </div>
            </div>
            <div style={{display: row.displayChild}}>
                {list}
            </div>
        </div>
        )
    }
    return (
        <div>
            <div>
                <Button type="primary" size="small">导入Json</Button>
            </div>
            {generate(defineRows[0])}
            <Modal title="备注" open={isRemarkModalVisible} onOk={handleOkSetRemark} onCancel={handleCancelSetRemark}>
                <TextArea rows={8} value={remark} onChange={onChangeRemark}/>
            </Modal>
            <Modal title="高级设置" open={isDetailModalVisible} onOk={handleOkSetDetail} onCancel={handleCancelSetDetail}>
                <TextArea rows={8} value={remark} onChange={onChangeRemark}/>
            </Modal>
        </div>
    )
}

export {JsonDefineEditor}
