package com.tm.worker.utils;

import com.tm.worker.core.exception.ParameterValueTypeErrorException;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

public final class FunctionUtils {
    private FunctionUtils() {}
    public static Integer getIntegerFromString(String intStr, String argName, Integer defaultValue) {
        Integer result = defaultValue;
        if(StringUtils.isBlank(intStr)) {
            return result;
        }
        if(!intStr.startsWith("-") && !StringUtils.isNumeric(intStr)) {
            throw new ParameterValueTypeErrorException("[" + argName + "]参数值类型错误，必须是数字。");
        }
        if(intStr.startsWith("-") && !StringUtils.isNumeric(intStr.substring(1))) {
            throw new ParameterValueTypeErrorException("[" + argName + "]参数值类型错误，必须是数字。");
        }
        result = Integer.valueOf(intStr);
        return result;
    }

    public static Integer randomUnitEvenNumber() {
        while (true) {
            int i = RandomUtils.nextInt(1, 10);
            if (i % 2 <= 0) {
                return i;
            }
        }
    }

    public static Integer randomUnitOddNumber() {
        while (true) {
            int i = RandomUtils.nextInt(1, 10);
            if (i % 2 > 0) {
                return i;
            }
        }
    }
}
