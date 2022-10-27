import React from "react";
import {DataNodeModel} from "./DataNodeModel";

export interface AntDataNode {
    title: React.ReactNode;
    key: string;
    isLeaf?: boolean;
    children?: AntDataNode[];
    dataNode?: DataNodeModel;
    parentNode?: any;
}
