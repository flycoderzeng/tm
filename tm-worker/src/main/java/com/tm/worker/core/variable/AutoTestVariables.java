package com.tm.worker.core.variable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoTestVariables {
    public static final String BUILTIN_VARIABLE_NAME_REQUEST = "__request";
    public static final String BUILTIN_VARIABLE_NAME_RESPONSE = "__response";
    public static final String BUILTIN_VARIABLE_NAME_CASE_ID = "__case_id";
    public static final String BUILTIN_VARIABLE_NAME_CASE_NAME = "__case_name";
    public static final String BUILTIN_VARIABLE_NAME_PLAN_ID = "__plan_id";
    public static final String BUILTIN_VARIABLE_NAME_PLAN_NAME = "__plan_name";
    public static final String BUILTIN_VARIABLE_NAME_ENV_ID = "__env_id";
    public static final String BUILTIN_VARIABLE_NAME_ENV_NAME = "__env_name";
    public static final String BUILTIN_VARIABLE_NAME_WORKER_IP = "__worker_ip";
    public static final String BUILTIN_VARIABLE_NAME_PLAN_RESULT_ID = "__plan_result_id";



    private final Map<String, Object> variables = new HashMap();

    public AutoTestVariables() {
        initBuiltinVariables();
    }

    public void updateAutoCaseVariables(List<AutoCaseVariable> autoCaseVariables) {
        updateVariables(autoCaseVariables);
    }

    public AutoTestVariables(Map<String, String> variables) {
        for (Map.Entry<String, String> stringObjectEntry : variables.entrySet()) {
            put(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
    }

    private void updateVariables(List<AutoCaseVariable> autoCaseVariables) {
        if(autoCaseVariables == null) {
            return;
        }
        for (int i = 0; i < autoCaseVariables.size(); i++) {
            put(autoCaseVariables.get(i).getName(), autoCaseVariables.get(i).getValue());
        }
    }

    public String getVariableString(String key) {
        return get(key);
    }

    private String get(String key) {
        Object o = this.variables.get(key);
        if (o instanceof String) {
            return (String)o;
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

    public void replace(AutoTestVariables newVariables) {
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            if(newVariables.getVariables().containsKey(entry.getKey())) {
                this.put(entry.getKey(), newVariables.get(entry.getKey()));
            }
        }
    }

    private void initBuiltinVariables() {
        put(BUILTIN_VARIABLE_NAME_REQUEST, "");
        put(BUILTIN_VARIABLE_NAME_RESPONSE, "");
        put(BUILTIN_VARIABLE_NAME_CASE_ID, null);
        put(BUILTIN_VARIABLE_NAME_CASE_NAME, null);
        put(BUILTIN_VARIABLE_NAME_PLAN_ID, null);
        put(BUILTIN_VARIABLE_NAME_PLAN_NAME, "");
        put(BUILTIN_VARIABLE_NAME_ENV_ID, null);
        put(BUILTIN_VARIABLE_NAME_ENV_NAME, "");
        put(BUILTIN_VARIABLE_NAME_WORKER_IP, "");
        put(BUILTIN_VARIABLE_NAME_PLAN_RESULT_ID, null);
    }
}
