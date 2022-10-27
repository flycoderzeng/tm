package com.tm.worker.utils;

import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

public class AviatorUtils {
    private AviatorUtils() {
        throw new IllegalStateException("Utility class");
    }
    public static String getArgStringValue(Map<String, Object> env, AviatorObject arg1) {
        String value = null;
        if(arg1 instanceof AviatorString) {
            value = FunctionUtils.getStringValue(arg1, env);
        }else {
            Object javaObject = FunctionUtils.getJavaObject(arg1, env);
            if (javaObject != null) {
                value = javaObject.toString();
            }
        }
        return value;
    }

    public static String getArgStringValue(Map<String, Object> env, AviatorObject arg1, String defaultValue) {
        String argStringValue = getArgStringValue(env, arg1);
        return argStringValue == null ? defaultValue : argStringValue;
    }
}
