package com.tm.worker.core.function.random;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.tm.worker.core.exception.TMException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class GetRandomInt extends AbstractFunction {

    protected Integer getCount(Map<String, Object> env, AviatorObject arg1) {
        if(arg1 instanceof AviatorString) {
            String count = FunctionUtils.getStringValue(arg1, env);
            if(!StringUtils.isNumeric(count)) {
                throw new TMException("[" + arg1 + "]参数值类型错误，必须是数字。当前的值是：" + count);
            }
            return Integer.valueOf(count);
        }
        return FunctionUtils.getNumberValue(arg1, env).intValue();
    }

    protected String getResult(Map<String, Object> env, AviatorObject arg1) {
        String result = RandomStringUtils.randomNumeric(getCount(env, arg1));
        return result;
    }

    protected String getResult1(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        Integer count = getCount(env, arg1);
        String prefix = FunctionUtils.getStringValue(arg2, env);
        String result = RandomStringUtils.randomNumeric(count);
        if(StringUtils.isNotBlank(prefix)) {
            result = prefix + result;
        }
        return result;
    }

    protected String getResult2(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        String result = getResult1(env, arg1, arg2);
        String suffix = FunctionUtils.getStringValue(arg3, env);
        if(StringUtils.isNotBlank(suffix)) {
            result = result + suffix;
        }
        return result;
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        String result = getResult(env, arg1);
        return new AviatorString(result);
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        String result = getResult1(env, arg1, arg2);
        return new AviatorString(result);
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        String result = getResult2(env, arg1, arg2, arg3);
        return new AviatorString(result);
    }

    /**
     * 返回方法名
     */
    @Override
    public String getName() {
        return "__getRandomInt";
    }
}
