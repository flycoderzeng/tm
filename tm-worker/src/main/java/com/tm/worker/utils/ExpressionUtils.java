package com.tm.worker.utils;

import com.googlecode.aviator.AviatorEvaluator;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ExpressionUtils {
    private ExpressionUtils() {}
    public static final Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
    public static String replaceExpression(String expression, Map envMap) {
        if(StringUtils.isBlank(expression)) {
            return expression;
        }
        Matcher m = pattern.matcher(expression);
        List<String> listExpr = new ArrayList<>();
        while (m.find()) {
            listExpr.add(m.group(1));
        }
        for (String expr : listExpr) {
            if(StringUtils.isBlank(expr)) {
                continue;
            }
            String result = AviatorEvaluator.execute(expr, envMap).toString();
            expression = expression.replace("${" + expr + "}", result);
        }

        return expression;
    }

    public static String executeExpression(String expression, Map envMap) {
        if(StringUtils.isBlank(expression)) {
            return expression;
        }
        String result = AviatorEvaluator.execute(expression, envMap).toString();
        return result;
    }

    public static String extractVariable(String expression) {
        if(StringUtils.isBlank(expression)) {
            return null;
        }
        Matcher m = pattern.matcher(expression);
        if(m.find()) {
            return m.group(1);
        }
        return expression;
    }
}
