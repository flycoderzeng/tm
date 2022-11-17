import {BaseNode} from "./BaseNode";
import {KeyValueRow} from "./KeyValueRow";


export interface HttpNode extends BaseNode {
    requestType: string;
    url: string;
    params: KeyValueRow[];
    headers: KeyValueRow[];
    checkErrorList: KeyValueRow[];
    responseExtractorList: KeyValueRow[];
    // none form-data x-www-form-urlencoded raw
    bodyType: string;
    // text json xml
    rawType: string;
    formData: KeyValueRow[];
    formUrlencoded: KeyValueRow[];
    content: string;
}
