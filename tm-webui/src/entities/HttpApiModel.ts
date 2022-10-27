export interface HttpApiModel {
    id: number;
    reqDefineRows: any[],
    reqBodyType: string;
    reqBodyForm: any[],
    reqBodyKv: any[],
    reqParams: any[],
    reqHeaders: any[],
    resBodyType: string,
    resBody?: string,
    resDefineRows: any[],
    remark: string,
    reqBodyOther?: string,
    name: string,
    description: string,
    reqBodyMessage: string,
    resBodyMessage: string,
    url: string;
    method: string;
    addTime ?: string;
    addUser ?: string;
    lastModifyTime ?: string;
    lastModifyUser ?: string;
    status ?: number;
}
