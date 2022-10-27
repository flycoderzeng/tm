package com.tm.worker.core.function.random;

import cn.binarywang.tools.generator.EmailAddressGenerator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

public class GetEmail extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env) {
        String generatedEmail = EmailAddressGenerator.getInstance().generate();
        return new AviatorString(generatedEmail);
    }
    @Override
    public String getName() {
        return "__getEmail";
    }
}
