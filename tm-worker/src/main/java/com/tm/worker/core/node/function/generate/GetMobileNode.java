package com.tm.worker.core.node.function.generate;

import cn.binarywang.tools.generator.ChineseMobileNumberGenerator;
import com.tm.worker.core.node.function.FunctionNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetMobileNode extends FunctionNode {
    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行平台api：获取手机号");
        String mobile = ChineseMobileNumberGenerator.getInstance()
                .generate();
        log.info("生成手机号的结果是：{}", mobile);
        addResultInfo("mobile: ").addResultInfoLine(mobile);
        putResultVariable(arg100, mobile);
    }
}
