import {BaseDefine} from "./BaseDefine";

export interface StringDefine extends BaseDefine {
    minLength: number;
    maxLength: number;
    regularExpression: string;
    prefix: string;
    suffix: string;
    format: string;
}
