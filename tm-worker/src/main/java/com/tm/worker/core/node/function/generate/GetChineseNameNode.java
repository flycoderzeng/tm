package com.tm.worker.core.node.function.generate;

import cn.binarywang.tools.generator.ChineseNameGenerator;
import com.tm.worker.core.node.function.FunctionNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetChineseNameNode extends FunctionNode {
    @Override
    public void run() {
        super.run();
        log.info("执行平台api：获取中国姓名");
        String generatedName = ChineseNameGenerator.getInstance().generate();
        log.info("生成姓名的结果是：{}", generatedName);
        addResultInfo("中国姓名: ").addResultInfoLine(generatedName);
        putResultVariable(arg100, generatedName);
    }
}
