package com.tm.worker.core.protocol.jdbc;

import com.tm.common.base.model.DbConfig;
import com.tm.common.base.model.PlanRunningConfigSnapshot;
import com.tm.common.entities.common.KeyValueRow;
import com.tm.worker.core.exception.TMException;
import com.tm.worker.core.node.StepNodeBase;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.ExpressionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Data
public class JDBCRequest extends StepNodeBase {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private String dbName;
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

    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行jdbc request步骤，{}", getName());
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        if(StringUtils.isBlank(dbName)) {
            throw new TMException("数据库名不能为空");
        }
        PlanRunningConfigSnapshot runningConfigSnapshot = context.getPlanTask().getRunningConfigSnapshot();
        if(runningConfigSnapshot.getEnvId() == null) {
            throw new TMException("运行环境不能为空");
        }

        dbName = ExpressionUtils.replaceExpression(dbName, caseVariables.getVariables());
        if(StringUtils.isBlank(dbName)) {
            throw new TMException("数据库名不能为空");
        }

        DbConfig dbConfig = context.getTaskService().findDbConfig(runningConfigSnapshot.getEnvId(), dbName);
        if(dbConfig == null) {
            throw new TMException(String.format("不存在 数据库名: %s，环境: %s(id: %s) 的配置",
                    dbName, runningConfigSnapshot.getEnvName(), runningConfigSnapshot.getEnvId()));
        }

        if(StringUtils.isBlank(content)) {
            throw new TMException("sql语句不能为空");
        }
        content = ExpressionUtils.replaceExpression(content, caseVariables.getVariables());
        if(StringUtils.isBlank(content)) {
            throw new TMException("sql语句不能为空");
        }
        content = content.trim();
        addResultInfo("数据库名：").addResultInfoLine(dbName);
        addResultInfo("sql语句：").addResultInfoLine(content);

        if(content.toLowerCase().startsWith("select")) {
            List<Map<String, String>> list = execSelect(dbConfig, content);
            addResultInfo("sql执行结果：").addResultInfoLine(list);
        }
    }

    private List<Map<String, String>> execSelect(DbConfig dbConfig, String sql) {
        AutoTestContext context = AutoTestContextService.getContext();

        if(sql.endsWith(";")) {
            sql = sql.substring(0, sql.length()-1);
        }

        int ind = sql.toLowerCase().lastIndexOf("limit");
        if(ind < 0) {
            sql += " limit 1";
        }

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
                context.getTaskService().closeJDBCConnection(dbConfig, conn);
            }
        }
        return list;
    }
}
