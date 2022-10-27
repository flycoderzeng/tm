import {EnumRowDefine} from "./EnumRowDefine";

export interface BaseDefine {
    type: string;
    name: string;
    title: string;
    remark: string;
    defaultValue: string;
    required: boolean;
    enum: EnumRowDefine[];
}
