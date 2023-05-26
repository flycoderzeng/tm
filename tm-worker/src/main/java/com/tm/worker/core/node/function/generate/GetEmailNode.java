package com.tm.worker.core.node.function.generate;

import cn.binarywang.tools.generator.EmailAddressGenerator;
import com.tm.worker.core.node.function.FunctionNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetEmailNode extends FunctionNode {
    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行平台api：获取email");
        String generatedEmail = EmailAddressGenerator.getInstance().generate();
        log.info("生成email的结果是：{}", generatedEmail);
        addResultInfo("email: ").addResultInfoLine(generatedEmail);
        putResultVariable(arg100, generatedEmail);
    }
}
