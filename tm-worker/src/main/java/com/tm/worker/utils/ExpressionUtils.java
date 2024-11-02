package com.tm.worker.utils;

import com.googlecode.aviator.AviatorEvaluator;
import com.tm.common.base.model.DataNode;
import com.tm.common.base.model.GlobalVariable;
import com.tm.common.entities.common.enumerate.DataTypeEnum;
import com.tm.worker.core.exception.TMException;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 引用 "用例变量"
 * ${v_variableName}
 * 引用 "全局变量"
 * #{v_variableName}
 * 引用 "正则表达式"  根据正则表达式 生成字符串替换
 * @/[0-9]{1,3}/
 */
public final class ExpressionUtils {
    public static final String __PLATFORM_PRIVATE_NULL = "__NULL__";

    public static final Pattern TEMPLATE_CASE_VARIABLE_PATTERN = Pattern.compile("\\$\\{(.*?)\\}");
    public static final Pattern EXISTS_FUNCTION_PATTERN = Pattern.compile(".*\\$\\{(.*?)\\}.*");
    public static final Pattern FUNCTION_CALL_PATTERN = Pattern.compile("\\$\\{__(.*?)\\}");
    public static final Pattern TEMPLATE_GLOBAL_VARIABLE_PATTERN = Pattern.compile("#\\{(.*?)\\}");
    public static final Pattern TEMPLATE_REGEX_PATTERN = Pattern.compile("@/(.*?)/");


    private ExpressionUtils() {}

    public static String replaceExpression(String expression, Map envMap) {
        return replaceGlobalVariable(replaceRegex(replaceCaseVariable(expression, envMap)));
    }

    public static String replaceGlobalVariable(String src) {
        if(StringUtils.isBlank(src)) {
            return src;
        }
        AutoTestContext context = AutoTestContextService.getContext();
        Matcher m = TEMPLATE_GLOBAL_VARIABLE_PATTERN.matcher(src);
        List<String> listExpr = new ArrayList<>();
        while (m.find()) {
            listExpr.add(m.group(1));
        }
        for (String globalKeyName : listExpr) {
            GlobalVariable globalVariable = getGlobalVariable(context, globalKeyName);
            src = src.replace("#{" + globalKeyName + "}", globalVariable.getValue());
        }
        return src;
    }

    public static GlobalVariable getGlobalVariable(AutoTestContext context, String globalKeyName) {
        List<DataNode> dataNodes = context.getTaskService().selectByDataTypeIdAndName(DataTypeEnum.GLOBAL_VARIABLE.value(), globalKeyName);
        if(dataNodes == null || dataNodes.isEmpty()) {
            throw new TMException("没有找到全局变量: " + globalKeyName);
        }
        if(dataNodes.size() > 1) {
            throw new TMException("找到多个全局变量: " + globalKeyName);
        }
        DataNode dataNode = dataNodes.get(0);
        GlobalVariable globalVariable = context.getTaskService().selectGlobalVariableByPrimaryId(dataNode.getId());
        if(globalVariable == null) {
            throw new TMException("没有找到全局变量: " + dataNode.getId());
        }
        return globalVariable;
    }

    public static String replaceRegex(String src) {
        if(StringUtils.isBlank(src)) {
            return src;
        }
        Matcher m = TEMPLATE_REGEX_PATTERN.matcher(src);
        List<String> listExpr = new ArrayList<>();
        while (m.find()) {
            listExpr.add(m.group(1));
        }
        for (String expr : listExpr) {
            src = src.replace("@/" + expr + "/", CmdUtils.randomStringByRegex(expr));
        }
        return src;
    }

    public static String replaceCaseVariable(String src, Map envMap) {
        if(StringUtils.isBlank(src)) {
            return src;
        }
        Matcher m = TEMPLATE_CASE_VARIABLE_PATTERN.matcher(src);
        List<String> listExpr = new ArrayList<>();
        while (m.find()) {
            listExpr.add(m.group(1));
        }
        for (String expr : listExpr) {
            if(StringUtils.isBlank(expr)) {
                continue;
            }
            Object object = AviatorEvaluator.execute(expr, envMap);
            String result = "";
            if(object instanceof String) {
                result = (String)object;
            }else{
                result = object + "";
            }
            result += "";
            if(StringUtils.equals("v_columnFormat", expr) && StringUtils.isBlank(result)) {
                src = src.replace("\"${" + expr + "}\"", "null");
            } else if(StringUtils.equals(result, "null")) {
                src = src.replace("\"${" + expr + "}\"", result);
                src = src.replace("${" + expr + "}", result);
            } else {
                src = src.replace("${" + expr + "}", result);
            }
        }

        if(StringUtils.equals(__PLATFORM_PRIVATE_NULL, src)) {
            return null;
        }

        return src;
    }

    public static String executeExpression(String expression, Map envMap) {
        if(StringUtils.isBlank(expression)) {
            return expression;
        }
        return AviatorEvaluator.execute(expression, envMap).toString();
    }

    public static String extractVariable(String expression) {
        if(StringUtils.isBlank(expression)) {
            return null;
        }
        Matcher m = TEMPLATE_CASE_VARIABLE_PATTERN.matcher(expression);
        if(m.find()) {
            return m.group(1);
        }
        return expression;
    }
}
