import {BaseNode} from "./BaseNode";

export interface ScriptActionNode extends BaseNode {
    content: string;
    scriptResultVariableName: string;
}