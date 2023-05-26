package com.tm.worker.core.node.function.generate;

import cn.binarywang.tools.generator.ChineseAddressGenerator;
import com.tm.worker.core.node.function.FunctionNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetChineseAddressNode extends FunctionNode {
    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行平台api：获取中国地址");
        String address = ChineseAddressGenerator.getInstance().generate();
        log.info("生成中国地址的结果是：{}", address);
        addResultInfo("中国地址: ").addResultInfoLine(address);
        putResultVariable(arg100, address);
    }
}
