package com.tm.worker.core.variable;

import cn.hutool.core.util.ReUtil;
import com.tm.common.base.model.DbConfig;
import com.tm.common.base.model.PlanRunningConfigSnapshot;
import com.tm.common.base.model.RunEnv;
import com.tm.worker.core.protocol.jdbc.JDBCRequest;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.utils.ExpressionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoTestVariables {
    public static final String BUILTIN_VARIABLE_NAME_REQUEST = "__request";
    public static final String BUILTIN_VARIABLE_NAME_RESPONSE = "__response";

    public static final String BUILTIN_VARIABLE_NAME_RESPONSE_STATUS = "__response_status";
    public static final String BUILTIN_VARIABLE_NAME_CASE_ID = "__case_id";
    public static final String BUILTIN_VARIABLE_NAME_CASE_NAME = "__case_name";
    public static final String BUILTIN_VARIABLE_NAME_PLAN_ID = "__plan_id";
    public static final String BUILTIN_VARIABLE_NAME_PLAN_NAME = "__plan_name";
    public static final String BUILTIN_VARIABLE_NAME_ENV_ID = "__env_id";
    public static final String BUILTIN_VARIABLE_NAME_ENV_NAME = "__env_name";
    public static final String BUILTIN_VARIABLE_NAME_WORKER_IP = "__worker_ip";
    public static final String BUILTIN_VARIABLE_NAME_PLAN_RESULT_ID = "__plan_result_id";
    public static final String BUILTIN_VARIABLE_NAME_GROUP_NO = "__group_no";
    public static final String BUILTIN_VARIABLE_NAME_GROUP_NAME = "__group_name";


    private final Map<String, Object> variables = new HashMap();
    private final Map<String, AutoCaseVariable> caseVariableMap = new HashMap();
    private AutoTestVariables planVariables;

    public AutoTestVariables() {
        initBuiltinVariables();
    }

    public void updateAutoCaseVariables(List<AutoCaseVariable> autoCaseVariables, AutoTestVariables autoTestVariables) {
        this.planVariables = autoTestVariables;
        updateVariables(autoCaseVariables, autoTestVariables);
    }

    public AutoTestVariables(Map<String, String> testVariables) {
        variables.putAll(testVariables);
    }

    private void updateVariables(List<AutoCaseVariable> autoCaseVariables, AutoTestVariables autoTestVariables) {
        if(autoCaseVariables == null) {
            return;
        }
        for (AutoCaseVariable autoCaseVariable : autoCaseVariables) {
            caseVariableMap.put(autoCaseVariable.getName(), autoCaseVariable);
            if (StringUtils.isNotBlank(autoCaseVariable.getPlanVariableName()) && autoTestVariables != null
                    && autoTestVariables.exists(autoCaseVariable.getPlanVariableName())) {
                String value = autoTestVariables.get(autoCaseVariable.getPlanVariableName());
                if (value != null && value.toLowerCase().startsWith("${sql:select") && value.endsWith("}")) {
                    value = getSqlValue(value);
                }
                put(autoCaseVariable.getName(), value);
            } else {
                put(autoCaseVariable.getName(), autoCaseVariable.getValue());
            }
        }
    }

    private String getSqlValue(String value) {
        String sql = value.substring(6, value.length() - 1);
        AutoTestContext context = AutoTestContextService.getContext();
        final PlanRunningConfigSnapshot runningConfigSnapshot = context.getPlanTask().getRunningConfigSnapshot();
        final RunEnv runEnv = runningConfigSnapshot.getRunEnv();
        DbConfig dbConfig = new DbConfig(runEnv.getDbUsername(), runEnv.getDbPassword(),
                runEnv.getDbIp(), runEnv.getDbPort(), runEnv.getDbType());
        final String from = ReUtil.getGroup1(".+[ \t\r\n]+(.+\\..+)[ \r\n\t]*", sql);
        if(StringUtils.isNoneBlank(from)) {
            dbConfig.setDbName(from.split("\\.")[0]);
        }
        dbConfig.setEnvId(runningConfigSnapshot.getEnvId());
        dbConfig.setSchemaName(runEnv.getDbSchemaName());
        List<Map<String, String>> list = JDBCRequest.execSelect(dbConfig, sql, context);
        if(!list.isEmpty()) {
            final Collection<String> values = list.get(0).values();
            if(!values.isEmpty()) {
                value = values.stream().findFirst().get();
            }
        }
        return value;
    }

    public void replace(AutoTestVariables newVariables) {
        if(newVariables != null) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                String variableName = entry.getKey();
                AutoCaseVariable autoCaseVariable = caseVariableMap.get(variableName);
                // 如果计划变量不为空, 且用例变量配置了计划变量, 则 不用组合变量设置的值 做替换
                if(!(planVariables != null && autoCaseVariable != null && StringUtils.isNotBlank(autoCaseVariable.getPlanVariableName()))
                        && newVariables.getVariables().containsKey(variableName)) {
                    String value = newVariables.get(variableName);
                    if(ReUtil.isMatch(ExpressionUtils.FUNCTION_CALL_PATTERN, value)) {
                        value = ExpressionUtils.replaceExpression(value, variables);
                    }
                    this.put(variableName, value);
                }
            }
        }
    }

    private boolean exists(String planVariableName) {
        return variables.containsKey(planVariableName);
    }

    public String getVariableString(String key) {
        return get(key);
    }

    private String get(String key) {
        Object o = this.variables.get(key);
        if (o instanceof String string) {
            if(key.equals("v_columnName")) {
                return string.toUpperCase();
            }
            return string;
        } else {
            return o != null ? o.toString() : null;
        }
    }

    public void put(String key, String value) {
        this.variables.put(key, value);
    }

    public void putObject(String key, Object value) {
        variables.put(key, value);
    }

    public Object getObject(String key) {
        Object o = this.variables.get(key);
        if (o instanceof String string) {
            if(StringUtils.equals(string, ExpressionUtils.__PLATFORM_PRIVATE_NULL)) {
                return null;
            }else if(ReUtil.isMatch(ExpressionUtils.EXISTS_FUNCTION_PATTERN, string)) {
                return ExpressionUtils.replaceExpression((String) o, variables);
            }
            return string;
        } else {
            return o != null ? o.toString() : null;
        }
    }

    public Map<String, Object> getVariables() {
        Map<String, Object> newVariables = new HashMap();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String key = entry.getKey();
            newVariables.put(key, this.getObject(key));
        }
        return newVariables;
    }

    private void initBuiltinVariables() {
        put(BUILTIN_VARIABLE_NAME_REQUEST, "");
        put(BUILTIN_VARIABLE_NAME_RESPONSE, "");
        put(BUILTIN_VARIABLE_NAME_RESPONSE_STATUS, "");
        put(BUILTIN_VARIABLE_NAME_CASE_ID, null);
        put(BUILTIN_VARIABLE_NAME_CASE_NAME, null);
        put(BUILTIN_VARIABLE_NAME_PLAN_ID, null);
        put(BUILTIN_VARIABLE_NAME_PLAN_NAME, "");
        put(BUILTIN_VARIABLE_NAME_ENV_ID, null);
        put(BUILTIN_VARIABLE_NAME_ENV_NAME, "");
        put(BUILTIN_VARIABLE_NAME_WORKER_IP, "");
        put(BUILTIN_VARIABLE_NAME_PLAN_RESULT_ID, null);
        putObject(BUILTIN_VARIABLE_NAME_GROUP_NO, 0);
        putObject(BUILTIN_VARIABLE_NAME_GROUP_NAME, "");
    }

    public void execBuiltinFunction() {
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String variableName = entry.getKey();
            String value = getVariableString(variableName);
            if(ReUtil.isMatch(ExpressionUtils.FUNCTION_CALL_PATTERN, value)) {
                value = ExpressionUtils.replaceExpression(value, variables);
                put(variableName, value);
            }
        }
    }
}
