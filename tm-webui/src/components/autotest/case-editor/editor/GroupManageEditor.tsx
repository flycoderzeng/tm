import React, {useState, useRef, useEffect} from "react";
import {AutoCaseVariable} from "../entities/AutoCaseVariable";
import {Button, Switch, Table, Input, Modal, Select} from "antd";


interface IState {
    groupVariables: string | null | undefined;
    userDefinedVariables: AutoCaseVariable[];
    onChangeGroupVariables: any;
}


const GroupManageEditor: React.FC<IState> = (props) => {
    const [groupVariables, setGroupVariables] = useState(props.groupVariables);
    const [visibleRemoveGroupModal, setVisibleRemoveGroupModal] = useState(false);
    const [removedGroups, setRemovedGroups] = useState<string[]>([]);
    const [userDefinedVariables, setUserDefinedVariables] = useState(props.userDefinedVariables);
    const {onChangeGroupVariables} = props;
    let headers: any[]|undefined;
    let rows: any[];
    let options: any[]|undefined;

    if(groupVariables !== props.groupVariables) {
        setGroupVariables(props.groupVariables);
    }
    if (JSON.stringify(userDefinedVariables) !== JSON.stringify(props.userDefinedVariables)) {
        setUserDefinedVariables(props.userDefinedVariables);
    }

    function onAddGroup() {
        let groups: any[] = [];
        if(groupVariables) {
            groups = JSON.parse(groupVariables);
        }
        const row: any = {"__runFlag": "1", "__groupDescription": ""};
        groups.push(row);
        let s = JSON.stringify(groups);
        setGroupVariables(s);
        onChangeGroupVariables(s);
    }

    function onRemoveGroup() {
        setRemovedGroups([]);
        setVisibleRemoveGroupModal(true);
    }

    function onChange() {
        if(!headers || headers?.length < 2) {
            setGroupVariables(null);
            onChangeGroupVariables(null);
        }else{
            let groups: any[] = [];
            for (let i = 1; i < headers.length; i++) {
                const dataIndex = headers[i]['dataIndex'];
                const row: any = {};
                for (let j = 0; j < rows.length; j++) {
                    row[rows[j]['__variableName']] = rows[j][dataIndex];
                }
                groups.push(row);
            }
            let s = JSON.stringify(groups);
            setGroupVariables(s);
            onChangeGroupVariables(s);
        }
    }

    function renderHeaders() {
        let headers: any[] = [
            {
                "title": "????????????",
                "width": 120,
                dataIndex: "__variableName",
                key: "__variableName",
                render: (text: string) => {
                    if(text === '__runFlag') {
                        return '????????????';
                    }else if(text === '__groupDescription') {
                        return '????????????';
                    }
                    return text;
                },
            },
        ];
        let groups: any[] = [];
        if(groupVariables) {
            groups = JSON.parse(groupVariables);
        }
        if(groups) {
            headers = headers.concat(groups.map((value, index) => {
                let name = '??????_';
                const ind = index + 1;
                name += ind;
                const dataIndex = "__group_" + ind;
                return {
                    "title": name,
                    "width": 120,
                    dataIndex: dataIndex,
                    key: dataIndex,
                    render: (text: string, record: any) => {
                        if(record['__variableName'] === '__runFlag') {
                            return <Switch size="small" checkedChildren="???" unCheckedChildren="???" defaultChecked={record[dataIndex] === '1'} onChange={(checked: boolean) => {
                                if(checked) {
                                    record[dataIndex] = '1';
                                }else{
                                    record[dataIndex] = '0';
                                }
                                onChange();
                            }}/>
                        }
                        if(record['__variableName'] !== '__runFlag') {
                            return <Input size="small" value={record[dataIndex]} onChange={e => {
                                record[dataIndex] = e.target.value;
                                onChange();
                            }}/>
                        }
                    }
                };
            }));
        }

        return headers;
    }

    headers = renderHeaders();

    function renderData() {
        let rows: any[] = [{"__variableName": "__runFlag", key: '-2'}, {"__variableName": "__groupDescription", key: '-1'}];
        let groups: any[] = [];
        if(groupVariables) {
            groups = JSON.parse(groupVariables);
        }

        rows = rows.concat(userDefinedVariables.map((v, ind) => {
            return {"__variableName": v.name, key: ind.toString()};
        }));
        for (let i = 0; i < groups.length; i++) {
            for (let j = 0; j < rows.length; j++) {
                const dataIndex = "__group_" + (i + 1);
                rows[j][dataIndex] = groups[i][rows[j]["__variableName"]] || '';
            }
        }

        return rows;
    }

    rows = renderData();

    const maxWidth = window.innerWidth - 600;
    const maxHeight = window.outerHeight - 250;

    function renderOptions() {
        if(!headers) {
            return [];
        }
        const values: any[] = [];
        for (let i = 1; i < headers.length; i++) {
            values.push({label: '??????_' + i, value: '??????_' + i});
        }
        return values;
    }

    options = renderOptions();

    function handleChangeRemoveGroups(values: string[]) {
        setRemovedGroups(values);
    }

    return (
        <div>
            <div className="variable-toolbar">
                <Button size="small" type="primary" onClick={onAddGroup}>????????????</Button>
                <Button size="small" type="primary" onClick={onRemoveGroup}>????????????</Button>
            </div>
            <div style={{marginTop: '5px', maxWidth: maxWidth}}>
                <Table columns={headers} scroll={{ x: maxWidth, y: maxHeight }} dataSource={rows} size="small" pagination={false} bordered={true}/>
            </div>
            <Modal
                title="????????????"
                open={visibleRemoveGroupModal}
                onOk={() => {
                    if (!window.confirm('??????????????????') || !headers || headers.length < 2) {
                        return;
                    }
                    for (let i = 0; i < removedGroups.length; i++) {
                        let ind = -1;
                        for (let j = 0; j < headers.length; j++) {
                            if(headers[j]['title'] === removedGroups[i]) {
                                ind = j;
                                break;
                            }
                        }
                        if(ind !== -1) {
                            headers?.splice(ind, 1);
                        }
                    }
                    onChange();
                    setVisibleRemoveGroupModal(false);
                }}
                onCancel={() => {
                    setVisibleRemoveGroupModal(false);
                }}
                width={1000}
            >
                <Select
                    mode="multiple"
                    size={'small'}
                    allowClear
                    style={{ width: '100%' }}
                    placeholder="???????????????????????????"
                    defaultValue={[]}
                    value={removedGroups}
                    onChange={handleChangeRemoveGroups}
                    options={options}
                />
            </Modal>
        </div>
    )
}

export {GroupManageEditor};
