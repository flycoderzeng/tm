package com.tm.worker.core.variable;

import cn.hutool.core.util.ReUtil;
import com.tm.worker.utils.ExpressionUtils;
import org.apache.commons.lang3.StringUtils;

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

    public void updateAutoCaseVariables(List<AutoCaseVariable> autoCaseVariables, AutoTestVariables planVariables) {
        this.planVariables = planVariables;
        updateVariables(autoCaseVariables, planVariables);
    }

    public AutoTestVariables(Map<String, String> variables) {
        for (Map.Entry<String, String> stringObjectEntry : variables.entrySet()) {
            put(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
    }

    private void updateVariables(List<AutoCaseVariable> autoCaseVariables, AutoTestVariables planVariables) {
        if(autoCaseVariables == null) {
            return;
        }
        for (int i = 0; i < autoCaseVariables.size(); i++) {
            AutoCaseVariable autoCaseVariable = autoCaseVariables.get(i);
            caseVariableMap.put(autoCaseVariable.getName(), autoCaseVariable);
            if(StringUtils.isNotBlank(autoCaseVariable.getPlanVariableName()) && planVariables != null
                    && planVariables.exists(autoCaseVariable.getPlanVariableName())) {
                put(autoCaseVariable.getName(), planVariables.get(autoCaseVariable.getPlanVariableName()));
            } else {
                put(autoCaseVariable.getName(), autoCaseVariable.getValue());
            }
        }
    }

    public void replace(AutoTestVariables newVariables) {
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String variableName = entry.getKey();
            AutoCaseVariable autoCaseVariable = caseVariableMap.get(variableName);
            // 如果计划变量不为空, 且用例变量配置了计划变量, 则 不用组合变量设置的值 做替换
            if(!(planVariables != null && autoCaseVariable != null && StringUtils.isNotBlank(autoCaseVariable.getPlanVariableName()))
                    && newVariables.getVariables().containsKey(variableName)) {
                this.put(variableName, newVariables.get(variableName));
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
            if(StringUtils.equals(string, ExpressionUtils.__PLATFORM_PRIVATE_NULL)) {
                return null;
            }else if(ReUtil.isMatch(ExpressionUtils.pattern, string)) {
                return ExpressionUtils.replaceExpression((String) o, variables);
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
        return variables.get(key);
    }

    public Map<String, Object> getVariables() {
        return variables;
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
}
