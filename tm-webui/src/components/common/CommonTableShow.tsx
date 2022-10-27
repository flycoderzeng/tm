import React from "react";
import {Table} from "antd";

interface IState {
    columns: any[],
    data: any[],
    size?: any;
    expandDefine?: any;
}

const CommonTableShow: React.FC<IState> = (props) => {
    const { columns } = props;
    const { data } = props;
    let { size } = props;
    if(!size) {
        size = 'small';
    }
    let { expandDefine } = props;
    if(!expandDefine) {
        expandDefine = undefined;
    }

    return (<Table expandable={expandDefine} columns={columns} dataSource={data} size={size} pagination={false} bordered={true}/>)
}

export {CommonTableShow}
