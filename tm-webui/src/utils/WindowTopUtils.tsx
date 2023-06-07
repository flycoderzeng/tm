export class WindowTopUtils {

    public static object_setExpandedKeys: string = 'setExpandedKeys';
    public static object_setSelectedKeys: string = 'setSelectedKeys';
    public static object_expandedKeys: string = 'expandedKeys';

    public static object_activeTabJson: string = 'activeTabJson';

    public static getWindowTopObject(objectName: string):any {
        // @ts-ignore
        return window.top[objectName];
    }

    public static setWindowTopObject(objectName: string, objectValue: any): void {
        // @ts-ignore
        window.top[objectName] = objectValue;
    }

    public static expandLeftTree(dataNode: any): void {
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
        for (let i = 0; i < expandedKeys.length; i++) {
            if(keys.indexOf(expandedKeys[i]) === -1) {
                keys.push(expandedKeys[i]);
            }
        }
        if(WindowTopUtils.getWindowTopObject(WindowTopUtils.object_setExpandedKeys) && keys.length > 0) {
            const windowTopObject = WindowTopUtils.getWindowTopObject(WindowTopUtils.object_setExpandedKeys) as any;
            windowTopObject([...keys]);
        }

        if(WindowTopUtils.getWindowTopObject(WindowTopUtils.object_setSelectedKeys) && keys.length > 0) {
            const windowTopObject = WindowTopUtils.getWindowTopObject(WindowTopUtils.object_setSelectedKeys) as any;
            windowTopObject([dataNode.id + '-' + dataNode.dataTypeId]);
        }
    }
}