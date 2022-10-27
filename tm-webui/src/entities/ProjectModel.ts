export interface ProjectModel {
    id : number;
    name: string;
    description: string;
    addTime ?: string;
    addUser ?: string;
    lastModifyTime ?: string;
    lastModifyUser ?: string;
    status ?: number;
}
