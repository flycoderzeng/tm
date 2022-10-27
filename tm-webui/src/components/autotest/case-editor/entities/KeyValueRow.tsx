import {BaseNameValue} from "../../../common/entities/BaseNameValue";

export interface KeyValueRow extends BaseNameValue {
    type?: string;
    extractorType?: string;
    description: string;
    domain?: string;
    path?: string;
    relationOperator?: string;
    assertLevel?: string;
    rowNumber?: number;
}
