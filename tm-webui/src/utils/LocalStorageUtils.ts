export class LocalStorageUtils {
    public static COPY_STEP_NODE = '__COPY_STEP_NODE';
    public static _LOGIN_USERNAME = '_LOGIN_USERNAME';
    public static __COPY_VARIABLES = '__COPY_VARIABLES';

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

    public static getItem(key: string): string {
        return localStorage.getItem(key) || '';
    }

    public static getLoginUsername(): string|null {
        return localStorage.getItem(LocalStorageUtils._LOGIN_USERNAME);
    }

    public static copyVariables(variables: string) {
        localStorage.setItem(LocalStorageUtils.__COPY_VARIABLES, variables);
    }
}
