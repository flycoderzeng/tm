import {StepNode} from "./StepNode";
import {AutoCaseVariable} from "./AutoCaseVariable";

export interface EditorIState<T> {
    stepNode: StepNode;
    define: T;
    refreshTree: any;
    userDefinedVariables?: AutoCaseVariable[];
    groupVariables?: string|null;
    onChangeGroupVariables?: any;
    onChange?: any;
    key?: string;
    groupManageEditorWidth?: number;
    treeData?: StepNode[];
}
