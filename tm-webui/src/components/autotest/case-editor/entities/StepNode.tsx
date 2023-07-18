export interface StepNode {
    type: string;
    disabled: boolean;
    level: number;
    title: string;
    key: string;
    id: string;
    isLeaf: boolean;
    children: StepNode[];
    define: any;
    seq: number;
}
