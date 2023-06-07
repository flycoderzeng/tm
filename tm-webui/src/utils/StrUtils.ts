export class StrUtils {
    public static getCamelCase(str: string): string {
        if(!str) return str;
        return str.replace(/([a-z])/g, function(all, i) {
            return i.toUpperCase();
        });
    }

    public static getGetParams(json: any): string {
        let params = '';
        Object.keys(json).map(k => {
            params += encodeURIComponent(k) + '=' + encodeURIComponent(json[k]) + '&';
            return null;
        });
        return params.substring(0, params.length-1);
    }
}
