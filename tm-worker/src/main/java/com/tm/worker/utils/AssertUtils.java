package com.tm.worker.utils;

import com.tm.common.entities.common.enumerate.RelationOperatorEnum;
import org.apache.commons.lang3.StringUtils;

public class AssertUtils {
    private AssertUtils() {
        throw new IllegalStateException("Utility class");
    }
    public static boolean compare(Object leftOperand, RelationOperatorEnum relationOperatorEnum, String rightOperand) {
        switch (relationOperatorEnum) {
            case EQUAL:
                return StringUtils.equals(leftOperand.toString(), rightOperand);
            case NOT_EQUAL:
                return !StringUtils.equals(leftOperand.toString(), rightOperand);
            case LESS_THAN:
                return true;
            case LESS_EQUAL:
                return true;
            case MORE_THAN:
                return true;
            case MORE_EQUAL:
                return true;
            case INCLUDE:
                return true;
            case NOT_INCLUDE:
                return true;
            case START_WITH:
                return true;
            case END_WITH:
                return true;
            case IS_NULL:
                return true;
            case IS_NOT_NULL:
                return true;
            case IS_EMPTY:
                return true;
            case IS_NOT_EMPTY:
                return true;
            case REGEX_PATTERN:
                return true;

            default:
                break;
        }
        return false;
    }
}
