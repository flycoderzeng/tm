export class LocalStorageUtils {
    public static COPY_STEP_NODE = '__COPY_STEP_NODE';

    public static saveFilteredValue(className: string, filteredValue: string[]): void {
        localStorage.setItem(className + '_filteredValue', JSON.stringify(filteredValue));
    }

    public static getFilteredValue(className: string|undefined|null): string[] {
        if(!className) return [];
        const item = localStorage.getItem(className + '_filteredValue');
        if(item) {
            try {
                const parse = JSON.parse(item);
                return parse || [];
            } catch (e) {
                return []
            }
        }
        return [];
    }
}
