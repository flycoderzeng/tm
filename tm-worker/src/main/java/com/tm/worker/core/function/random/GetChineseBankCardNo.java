package com.tm.worker.core.function.random;

import cn.binarywang.tools.generator.bank.BankCardNumberGenerator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

public class GetChineseBankCardNo extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env) {
        String result = BankCardNumberGenerator.getInstance()
                .generate();
        return new AviatorString(result);
    }
    @Override
    public String getName() {
        return "__getChineseBankCardNo";
    }
}
