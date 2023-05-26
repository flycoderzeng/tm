package com.tm.worker.core.function.random;

import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.tm.common.utils.DateUtils;

import java.util.Map;

public class GetRandomInt_1 extends GetRandomInt {

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        String ymd = DateUtils.parseTimestampToFormatDate(System.currentTimeMillis(), DateUtils.DATE_PATTERN_YMD);
        String result = ymd + getResult(env, arg1);
        return new AviatorString(result);
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        String ymd = DateUtils.parseTimestampToFormatDate(System.currentTimeMillis(), DateUtils.DATE_PATTERN_YMD);
        String result = ymd + getResult1(env, arg1, arg2);
        return new AviatorString(result);
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        String ymd = DateUtils.parseTimestampToFormatDate(System.currentTimeMillis(), DateUtils.DATE_PATTERN_YMD);
        String result = ymd + getResult2(env, arg1, arg2, arg3);
        return new AviatorString(result);
    }

    /**
     * 返回方法名
     */
    @Override
    public String getName() {
        return "__getRandomInt_1";
    }
}
