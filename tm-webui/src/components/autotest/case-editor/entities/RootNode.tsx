import {BaseNode} from "./BaseNode";
import {AutoCaseVariable} from "./AutoCaseVariable";
import {KeyValueRow} from "./KeyValueRow";

export interface RootNode extends BaseNode {
    userDefinedVariables: AutoCaseVariable[];
    cookies: KeyValueRow[];
}
