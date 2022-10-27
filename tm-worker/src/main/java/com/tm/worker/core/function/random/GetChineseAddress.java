package com.tm.worker.core.function.random;

import cn.binarywang.tools.generator.ChineseAddressGenerator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

public class GetChineseAddress extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env) {
        String address = ChineseAddressGenerator.getInstance()
                .generate();
        return new AviatorString(address);
    }
    @Override
    public String getName() {
        return "__getChineseAddress";
    }
}
