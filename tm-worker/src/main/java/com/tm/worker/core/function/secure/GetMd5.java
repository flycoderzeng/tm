package com.tm.worker.core.function.secure;

import cn.hutool.crypto.SecureUtil;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.tm.worker.utils.AviatorUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class GetMd5 extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env) {
        return new AviatorString(System.currentTimeMillis() + "");
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        String srcString = AviatorUtils.getArgStringValue(env, arg1);
        String result = "";
        if(StringUtils.isNoneBlank(srcString)) {
            result = SecureUtil.md5(srcString);
        }
        return new AviatorString(result);
    }

    /**
     * 返回方法名
     */
    @Override
    public String getName() {
        return "__md5";
    }
}
