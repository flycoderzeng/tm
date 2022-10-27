package com.tm.worker.core.function.date;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.tm.worker.utils.AviatorUtils;

import java.util.Map;

public class GetTimestamp  extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env) {
        return new AviatorString(System.currentTimeMillis() + "");
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        String type = AviatorUtils.getArgStringValue(env, arg1, "0");
        String resultDateString = System.currentTimeMillis() + "";
        if(!"0".equals(type)) {
            resultDateString = resultDateString.substring(0, 10);
        }
        return new AviatorString(resultDateString);
    }

    /**
     * 返回方法名
     */
    @Override
    public String getName() {
        return "__getTimestamp";
    }
}
