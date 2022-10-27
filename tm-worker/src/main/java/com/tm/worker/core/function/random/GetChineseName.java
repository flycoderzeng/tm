package com.tm.worker.core.function.random;

import cn.binarywang.tools.generator.ChineseNameGenerator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

public class GetChineseName extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env) {
        String name = ChineseNameGenerator.getInstance()
                .generate();
        return new AviatorString(name);
    }
    @Override
    public String getName() {
        return "__getChineseName";
    }
}
