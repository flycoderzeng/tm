import {BaseNode} from "./BaseNode";
import {KeyValueRow} from "./KeyValueRow";

export interface JDBCRequestNode extends BaseNode {
    dbName: string;
    // sql语句
    content: string;
    // 总行数输出到变量
    countVariableName?: string;
    // 结果集输出到变量
    resultSetVariableName?: string;
    retryTimes: number;
    // 检查断言
    checkErrorList: KeyValueRow[];
    // 查询列结果输出到变量
    responseExtractorList: KeyValueRow[];
    // 自增主键值输出到变量
    autoIncrementPrimaryKeyVariableName?: string;
}
