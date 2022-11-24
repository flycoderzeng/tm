package com.tm.worker.utils;

import com.tm.worker.core.exception.TMException;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public final class FunctionUtils {
    private FunctionUtils() {}
    public static Integer getIntegerFromString(String intStr, String argName, Integer defaultValue) {
        Integer result = defaultValue;
        if(StringUtils.isBlank(intStr)) {
            return result;
        }
        if(!intStr.startsWith("-") && !StringUtils.isNumeric(intStr)) {
            throw new TMException("[" + argName + "]参数值类型错误，必须是数字。");
        }
        if(intStr.startsWith("-") && !StringUtils.isNumeric(intStr.substring(1))) {
            throw new TMException("[" + argName + "]参数值类型错误，必须是数字。");
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

    //知识点：
    //1）encodeuricomponent和urlencoder.encode的区别在于前者对下面五个符号不编码
    //! %21
    //' %27
    //( %28
    //) %29
    //~ %7e
    //
    //2）decodeuricomponent和urldecoder.decode暂时还看不出有何区别
    public static String encodeURIComponent(@NotNull String src) throws UnsupportedEncodingException {
        String encodedStr = URLEncoder.encode(src, "UTF-8");
        if(StringUtils.isNoneBlank(encodedStr)) {
            encodedStr = encodedStr.replace("%21", "!").replace("%27", "'")
                    .replace("%28", "(").replace("%29", ")")
                    .replace("~", "%7e");
        }
        return encodedStr;
    }
}
