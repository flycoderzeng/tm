package com.tm.worker.core.function.random;

import cn.binarywang.tools.generator.ChineseMobileNumberGenerator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

public class GetMobile extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env) {
        String mobile = ChineseMobileNumberGenerator.getInstance()
                .generate();
        return new AviatorString(mobile);
    }
    @Override
    public String getName() {
        return "__getMobile";
    }
}
