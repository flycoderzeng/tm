package com.tm.worker.core.protocol.jdbc;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tm.common.base.model.DbConfig;
import com.tm.common.base.model.PlanRunningConfigSnapshot;
import com.tm.common.entities.common.KeyValueRow;
import com.tm.common.entities.common.enumerate.DbTypeEnum;
import com.tm.common.entities.common.enumerate.RelationOperatorEnum;
import com.tm.worker.core.exception.TMException;
import com.tm.worker.core.node.StepNodeBase;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.AssertUtils;
import com.tm.worker.utils.DataTypeAdapter;
import com.tm.worker.utils.ExpressionUtils;
import com.tm.worker.utils.JDBCUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
public class JDBCRequest extends StepNodeBase {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().registerTypeAdapter(new TypeToken<Map<String, Object>>() {
            }.getType(),
            new DataTypeAdapter()).create();

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final int MAX_ROWS = 100;

    private String dbName;
    private Integer dcnId;
    // 总行数输出到变量
    private String countVariableName;
    // 结果集输出到变量
    private String resultSetVariableName;
    private List<KeyValueRow> checkErrorList;
    private List<KeyValueRow> responseExtractorList;
    private String content;
    // 出现超时、断言失败等情况时的重试次数，最大100次
    private Integer retryTimes = 0;
    // 重试间隔时间（秒）
    private Integer retryIntervalSeconds = 10;
    // 自增主键保存至变量
    private String autoIncrementPrimaryKeyVariableName;

    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行jdbc request步骤，{}", getName());
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        if(StringUtils.isBlank(dbName)) {
            throw new TMException("数据库名称不能为空");
        }

        PlanRunningConfigSnapshot runningConfigSnapshot = context.getPlanTask().getRunningConfigSnapshot();
        if(runningConfigSnapshot.getEnvId() == null) {
            throw new TMException("运行环境不能为空");
        }

        String actualDbName = ExpressionUtils.replaceExpression(dbName, caseVariables.getVariables());
        if(StringUtils.isBlank(actualDbName)) {
            throw new TMException("数据库名不能为空");
        }

        DbConfig dbConfig = context.getTaskService().findDbConfig(runningConfigSnapshot.getEnvId(), dcnId, actualDbName);
        if(dbConfig == null) {
            throw new TMException(String.format("不存在 环境: %s(id: %s)，DCN id: %s， 数据库名: %s 的配置",
                    runningConfigSnapshot.getEnvName(), runningConfigSnapshot.getEnvId(), dcnId, actualDbName));
        }

        if(StringUtils.isBlank(content)) {
            throw new TMException("sql语句不能为空");
        }
        String actualContent = ExpressionUtils.replaceExpression(content, caseVariables.getVariables());
        if(StringUtils.isBlank(actualContent)) {
            throw new TMException("sql语句不能为空");
        }
        actualContent = actualContent.trim();
        addResultInfo("数据库名：").addResultInfoLine(actualDbName);
        addResultInfo("数据库ip：").addResultInfoLine(dbConfig.getIp());
        addResultInfo("数据库port：").addResultInfoLine(dbConfig.getPort());
        addResultInfo("sql语句：").addResultInfoLine(actualContent);

        if(DbTypeEnum.MYSQL.value() == dbConfig.getType() ||
                DbTypeEnum.POSTGRESQL.value() == dbConfig.getType() ||
                DbTypeEnum.DM.value() == dbConfig.getType()) {
            if (actualContent.toLowerCase().startsWith("select")) {
                List<Map<String, String>> list = execMySQLSelect(dbConfig, actualContent);
                String resultValue = gson.toJson(list);
                addResultInfo("sql结果: ").addResultInfoLine(resultValue);
                checkError(list);
                extractResult(list);
            } else if (actualContent.toLowerCase().startsWith("update")) {
                execUpdate(dbConfig, actualContent);
            } else if (actualContent.toLowerCase().startsWith("delete")) {
                execDelete(dbConfig, actualContent);
            } else if (actualContent.toLowerCase().startsWith("insert")) {
                execInsert(dbConfig, actualContent);
            }
        }
    }

    private void extractResult(List<Map<String, String>> list) {
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        saveAffectedRowsValueToVariable(list == null ? 0 : list.size(), caseVariables);
        if(list == null || list.isEmpty()) {
            addResultInfoLine("sql结果是空");
            return ;
        }
        resultSetVariableName = ExpressionUtils.extractVariable(resultSetVariableName);
        if(StringUtils.isNoneBlank(resultSetVariableName)) {
            String resultValue = gson.toJson(list);
            caseVariables.put(resultSetVariableName, resultValue);
        }

        if (responseExtractorList == null || responseExtractorList.isEmpty()) {
            addResultInfoLine("没有配置响应提取");
            return ;
        }
        for (KeyValueRow keyValueRow : responseExtractorList) {
            extractValueToVariable(list, caseVariables, keyValueRow);
        }
    }

    private void extractValueToVariable(List<Map<String, String>> list, AutoTestVariables caseVariables, KeyValueRow keyValueRow) {
        String name = keyValueRow.getName();
        name = ExpressionUtils.replaceExpression(name, caseVariables.getVariables());
        if (StringUtils.isBlank(name)) {
            return;
        }
        String value = keyValueRow.getValue();
        value = ExpressionUtils.extractVariable(value);
        if (StringUtils.isBlank(value)) {
            return;
        }
        if(!StringUtils.isBlank(keyValueRow.getRowNumber()) && !StrUtil.isNumeric(keyValueRow.getRowNumber())) {
            joinFields(list, keyValueRow.getRowNumber(), name, value, caseVariables);
        }else{
            Integer rowNumber = getRowNumber(keyValueRow);
            if(rowNumber >= list.size()) {
                return;
            }
            Map<String, String> map = list.get(rowNumber);
            if(map.containsKey(name.toUpperCase())) {
                name = name.toUpperCase();
            }
            addResultInfo("将列: ").addResultInfo(name).addResultInfo(" 的值[").addResultInfo(map.get(name)).addResultInfo("]保存到变量: ").addResultInfoLine(value);
            caseVariables.putObject(value, map.get(name));
        }
    }

    private void joinFields(List<Map<String, String>> list, String splitChar, String fieldName, String outputVariableName, AutoTestVariables caseVariables) {
        if(StringUtils.isBlank(splitChar) || StrUtil.isNumeric(splitChar)) {
            return;
        }
        List<String> arr = new ArrayList<>();
        for (Map<String, String> map : list) {
            if(map.containsKey(fieldName.toUpperCase())) {
                fieldName = fieldName.toUpperCase();
            }
            arr.add(map.get(fieldName));
        }
        String joinResult = StrUtil.join(splitChar, arr);
        log.info(joinResult);
        addResultInfo("将列: ").addResultInfo(fieldName).addResultInfo(" 的join值保存到变量: ").addResultInfoLine(outputVariableName);
        caseVariables.putObject(outputVariableName, joinResult);
    }

    private void saveAffectedRowsValueToVariable(int affectedRowsCount, AutoTestVariables caseVariables) {
        countVariableName = ExpressionUtils.extractVariable(countVariableName);
        if(StringUtils.isNoneBlank(countVariableName)) {
            addResultInfo("保存行数到变量: ").addResultInfoLine(countVariableName);
            caseVariables.put(countVariableName, affectedRowsCount + "");
        }
    }

    private void checkError(List<Map<String, String>> list) {
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        if(checkErrorList == null || checkErrorList.isEmpty()) {
            addResultInfoLine("没有配置断言");
            return;
        }
        if(list == null || list.isEmpty()) {
            throw new TMException("没有sql结果,断言失败");
        }
        boolean checkResult = true;
        String info = null;
        for (KeyValueRow keyValueRow : checkErrorList) {
            Integer rowNumber = getRowNumber(keyValueRow);
            if(rowNumber >= list.size()) {
                break;
            }
            Map<String, String> map = list.get(rowNumber);
            info = checkResponse(caseVariables, map, keyValueRow);
            if(info != null) {
                checkResult = false;
                break;
            }
        }
        if(!checkResult) {
            throw new TMException(info);
        }
    }

    private Integer getRowNumber(KeyValueRow keyValueRow) {
        Integer rowNumber = null;
        String rowNumberStr = keyValueRow.getRowNumber();
        if(StrUtil.isNumeric(rowNumberStr)) {
            rowNumber = Integer.parseInt(rowNumberStr);
        }

        if (rowNumber == null || rowNumber < 0) {
            rowNumber = 0;
        }

        if (rowNumber > 0) {
            rowNumber--;
        }

        return rowNumber;
    }

    private String checkResponse(AutoTestVariables caseVariables, Map<String, String> map, KeyValueRow keyValueRow) {
        String name = ExpressionUtils.replaceExpression(keyValueRow.getName(), caseVariables.getVariables());
        if (StringUtils.isBlank(name)) {
            return null;
        }
        if(map.containsKey(name.toUpperCase())) {
            name = name.toUpperCase();
        }
        Object leftOperand = map.get(name);
        final String value = ExpressionUtils.replaceExpression(keyValueRow.getValue(), caseVariables.getVariables());
        final RelationOperatorEnum relationOperator = RelationOperatorEnum.get(keyValueRow.getRelationOperator());
        String info = name + "[" + leftOperand + "] " + relationOperator.desc() + " " + value;
        addResultInfo(info);
        if(AssertUtils.compare(leftOperand, relationOperator, value)) {
            addResultInfoLine(" [成功]");
            return null;
        }else{
            addResultInfoLine(" [失败]");
            return info + " [失败]";
        }
    }

    private List<Map<String, String>> execMySQLSelect(DbConfig dbConfig, String sql) {
        AutoTestContext context = AutoTestContextService.getContext();

        if(sql.endsWith(";")) {
            sql = sql.substring(0, sql.length()-1);
        }

        List<Map<String, String>> list = execSelect(dbConfig, sql, context);

        return list;
    }

    private void execInsert(DbConfig dbConfig, String content) throws Exception {
        AutoTestContext context = AutoTestContextService.getContext();
        Connection conn = context.getTaskService().getJDBCConnection(dbConfig);
        try {
            if (conn == null) {
                throw new TMException("获取数据库连接失败");
            }
            int i = JDBCUtils.doExecuteUpdate(conn, content);

            AutoTestVariables caseVariables = context.getCaseVariables();
            saveAffectedRowsValueToVariable(i, caseVariables);
        } finally {
            if(conn != null) {
                log.info("归还数据库连接, {}", dbConfig.getDataSourceKey());
                context.getTaskService().closeJDBCConnection(dbConfig, conn);
            }
        }
    }

    private void execDelete(DbConfig dbConfig, String content) {

    }

    private void execUpdate(DbConfig dbConfig, String content) throws Exception {
        AutoTestContext context = AutoTestContextService.getContext();
        Connection conn = context.getTaskService().getJDBCConnection(dbConfig);
        try {
            if (content.indexOf("where") < 0 && content.indexOf("WHERE") < 0) {
                throw new TMException("SQL语句中必须有where条件");
            }
            if (conn == null) {
                throw new TMException("获取数据库连接失败");
            }
            int i = JDBCUtils.doExecuteUpdate(conn, content);

            AutoTestVariables caseVariables = context.getCaseVariables();
            saveAffectedRowsValueToVariable(i, caseVariables);
        } finally {
            if(conn != null) {
                log.info("归还数据库连接, {}", dbConfig.getDataSourceKey());
                context.getTaskService().closeJDBCConnection(dbConfig, conn);
            }
        }
    }

    private List<Map<String, String>> execSelect(DbConfig dbConfig, String sql, AutoTestContext context) {
        Connection conn = context.getTaskService().getJDBCConnection(dbConfig);
        if(conn == null) {
            throw new TMException("获取数据库连接失败");
        }
        Statement stmt = null;
        List<Map<String, String>> list = new ArrayList<>();
        ResultSet rs = null;
        try{
            // 执行查询
            stmt = conn.createStatement();
            stmt.setMaxRows(MAX_ROWS);
            rs = stmt.executeQuery(sql);
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            // 展开结果集数据库
            while(rs.next()) {
                Map<String, String> map = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String name = resultSetMetaData.getColumnName(i);
                    String value = rs.getString(i);
                    map.put(name, value);
                }
                list.add(map);
            }
        }catch(SQLException e){
            log.error("exec sql error, ", e);
            throw new TMException("执行select发生错误: " + e.getMessage(), e);
        }finally{
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    log.error("close rs error, ", e);
                }
            }

            try{
                if(stmt != null) stmt.close();
            }catch(SQLException e){
                log.error("close statement error, ", e);
            }

            if(conn != null) {
                log.info("归还数据库连接, {}", dbConfig.getDataSourceKey());
                context.getTaskService().closeJDBCConnection(dbConfig, conn);
            }
        }
        return list;
    }
}
