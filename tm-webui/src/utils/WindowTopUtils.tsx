import {AntDataNode} from "../entities/AntDataNode";

export class WindowTopUtils {

    public static object_setExpandedKeys: string = 'setExpandedKeys';
    public static object_setSelectedKeys: string = 'setSelectedKeys';
    public static object_expandedKeys: string = 'expandedKeys';

    public static object_activeTabJson: string = 'activeTabJson';

    public static object_leftTree: string = 'leftTree';

    public static object_currDataNode: string = 'object_currDataNode';

    public static getWindowTopObject(objectName: string):any {
        // @ts-ignore
        return window.top[objectName];
    }

    public static setWindowTopObject(objectName: string, objectValue: any): void {
        // @ts-ignore
        window.top[objectName] = objectValue;
    }

    public static expandLeftTree(dataNode: any, level: number): void {
        if(level >= 11) return;
        let keys: string[] = ['1-1'];
        for(let i = 2;i < 11; i++) {
            if(dataNode['parent' + i] > 0) {
                keys.push(dataNode['parent' + i] + '-' + dataNode.dataTypeId);
            }
        }
        let expandedKeys: string[] = WindowTopUtils.getWindowTopObject(WindowTopUtils.object_expandedKeys);
        if(!expandedKeys) {
            expandedKeys = []
        }

        let treeData: AntDataNode[] = WindowTopUtils.getWindowTopObject(WindowTopUtils.object_leftTree);
        if(treeData === null || treeData === undefined) {
            treeData = [];
        }
        const stack: AntDataNode[] = [treeData[0]];
        while (true) {
            const node: AntDataNode|undefined = stack.shift();
            for (let i = 0; i < keys.length; i++) {
                if(node?.key.startsWith(keys[i]+"-") && expandedKeys.indexOf(node?.key) === -1) {
                    expandedKeys.push(node?.key);
                }
                if(node && node.children) {
                    for (let i = 0; i < node.children.length; i++) {
                        stack.push(node.children[i]);
                    }
                }
            }

            if(!node) {
                break;
            }
        }
        if(expandedKeys.indexOf('1-1') === -1) {
            expandedKeys.push('1-1');
        }

        if(WindowTopUtils.getWindowTopObject(WindowTopUtils.object_setExpandedKeys) && expandedKeys.length > 0) {
            const windowTopObject = WindowTopUtils.getWindowTopObject(WindowTopUtils.object_setExpandedKeys) as any;
            windowTopObject([...expandedKeys]);
        }

        setTimeout(() => {
            WindowTopUtils.expandLeftTree(dataNode, level + 1)
        }, 500);
    }
}