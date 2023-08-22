import {BaseNode} from "./BaseNode";

export interface ScriptActionNode extends BaseNode {
    content: string;
    interpreterPath: string;
    scriptResultVariableName: string;
    scriptId: string|number|null|undefined;
}
