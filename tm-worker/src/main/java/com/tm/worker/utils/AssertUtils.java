package com.tm.worker.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tm.common.entities.common.enumerate.RelationOperatorEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class AssertUtils {
    public static final Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(new TypeToken<Map<String, Object>>() {
            }.getType(),
            new DataTypeAdapter()).create();

    private AssertUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String objectToString(Object object) {
        if(object == null) {
            return null;
        }
        if(object instanceof String) {
            return object.toString();
        }
        return gson.toJson(object);
    }

    public static boolean compare(Object leftOperand, RelationOperatorEnum relationOperatorEnum, String rightOperand) {
        String leftOperandString = objectToString(leftOperand);
        switch (relationOperatorEnum) {
            case EQUAL:
                if(leftOperand == null) {
                    return false;
                }
                return StringUtils.equals(leftOperandString, rightOperand);
            case NOT_EQUAL:
                if(leftOperand == null) {
                    return false;
                }
                return !StringUtils.equals(leftOperandString, rightOperand);
            case LESS_THAN:
                if(NumberUtil.isNumber(leftOperandString) && NumberUtil.isNumber(rightOperand)) {
                    Double left = Convert.toDouble(leftOperandString);
                    Double right = Convert.toDouble(rightOperand);
                    return left < right;
                }
                return false;
            case LESS_EQUAL:
                if(NumberUtil.isNumber(leftOperandString) && NumberUtil.isNumber(rightOperand)) {
                    Double left = Convert.toDouble(leftOperandString);
                    Double right = Convert.toDouble(rightOperand);
                    return left <= right;
                }
                return false;
            case GREATER_THAN:
                if(NumberUtil.isNumber(leftOperandString) && NumberUtil.isNumber(rightOperand)) {
                    Double left = Convert.toDouble(leftOperandString);
                    Double right = Convert.toDouble(rightOperand);
                    return left > right;
                }
                return false;
            case GREATER_EQUAL:
                if(NumberUtil.isNumber(leftOperandString) && NumberUtil.isNumber(rightOperand)) {
                    Double left = Convert.toDouble(leftOperandString);
                    Double right = Convert.toDouble(rightOperand);
                    return left >= right;
                }
                return false;
            case INCLUDE:
                if(leftOperandString == null) {
                    return false;
                }
                return leftOperandString.contains(rightOperand);
            case NOT_INCLUDE:
                if(leftOperandString == null) {
                    return false;
                }
                return !leftOperandString.contains(rightOperand);
            case START_WITH:
                if(leftOperandString == null) {
                    return false;
                }
                return leftOperandString.startsWith(rightOperand);
            case END_WITH:
                if(leftOperandString == null) {
                    return false;
                }
                return leftOperandString.endsWith(rightOperand);
            case IS_NULL:
                return leftOperand == null;
            case IS_NOT_NULL:
                return leftOperand != null;
            case IS_EMPTY:
                return StringUtils.isEmpty(leftOperandString);
            case IS_NOT_EMPTY:
                return StringUtils.isNoneEmpty(leftOperandString);
            case REGEX_PATTERN:
                if(leftOperand == null) {
                    return false;
                }
                return ReUtil.isMatch(rightOperand, leftOperandString);
            case PATH_NOT_EXISTS:
                if(leftOperand == null) {
                    return false;
                }
                return leftOperand instanceof com.jayway.jsonpath.PathNotFoundException;
            case IS_BLANK:
                if(leftOperand == null) {
                    return true;
                }
                return StringUtils.isBlank(leftOperandString);
            case IS_NOT_BLANK:
                if(leftOperand == null) {
                    return false;
                }
                return StringUtils.isNoneBlank(leftOperandString);
            case IS_NUMBER:
                if(leftOperand == null) {
                    return false;
                }
                return StrUtil.isNumeric(leftOperandString);
            default:
                break;
        }
        return false;
    }
}
