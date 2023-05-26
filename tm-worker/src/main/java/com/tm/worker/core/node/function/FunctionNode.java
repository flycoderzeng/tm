package com.tm.worker.core.node.function;

import com.tm.common.entities.common.BaseNameValue;
import com.tm.worker.core.node.StepNodeBase;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.ExpressionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class FunctionNode extends StepNodeBase {
    protected Integer platformApiId;
    protected List<BaseNameValue> parametricList;
    protected Map<String, String> parametersMap = new HashMap<>();

    public static final String arg100 = "result";
    public static final String arg101 = "result_1";
    public static final String arg102 = "result_2";

    @Override
    public void run() throws Exception {
        super.run();
        parameterListToMap();
        setResultInfoBuilder(new StringBuilder());
    }

    protected void parameterListToMap() {
        if(parametricList != null && !parametricList.isEmpty()) {
            for (BaseNameValue baseNameValue : parametricList) {
                parametersMap.put(baseNameValue.getName(), baseNameValue.getValue());
            }
        }
    }

    protected String getContent(String arg, AutoTestVariables caseVariables) {
        String content = "";
        if (parametersMap.containsKey(arg)) {
            content = parametersMap.get(arg);
            content = ExpressionUtils.replaceExpression(content, caseVariables.getVariables());
        }
        return content;
    }

    protected String getResultVariable(String parameterName) {
        if(!parametersMap.containsKey(parameterName)) {
            log.info("没有配置结果变量: {}", parameterName);
            return null;
        }
        String result = parametersMap.get(parameterName);
        if(StringUtils.isBlank(result) || StringUtils.isBlank(result.trim())) {
            log.info("结果变量是空: {}", parameterName);
            return null;
        }
        String resultVariable = ExpressionUtils.extractVariable(result);
        return resultVariable;
    }

    protected void putResultVariable(String argName, String value) {
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        String resultVariable = getResultVariable(argName);
        if (StringUtils.isNoneBlank(resultVariable) && StringUtils.isNoneBlank(resultVariable.trim())) {
            addResultInfo(argName).addResultInfo(": ").addResultInfo("值: ").addResultInfo(value)
                    .addResultInfo(" 保存到变量: ").addResultInfoLine(resultVariable);
            log.info("将结果保存到变量：{}", resultVariable);
            caseVariables.put(resultVariable, value);
        }
    }

    protected String getArgStringValue(String argName) {
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        String value = null;
        if (parametersMap.containsKey(argName)) {
            value = parametersMap.get(argName);
            value = ExpressionUtils.replaceExpression(value, caseVariables.getVariables());
        }
        return value;
    }

    protected String getArgStringValue(String argName, String defaultValue) {
        final String value = getArgStringValue(argName);
        if(StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }
}
