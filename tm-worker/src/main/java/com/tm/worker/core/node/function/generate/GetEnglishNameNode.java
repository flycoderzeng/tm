package com.tm.worker.core.node.function.generate;

import cn.binarywang.tools.generator.EnglishNameGenerator;
import com.tm.worker.core.node.function.FunctionNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetEnglishNameNode extends FunctionNode {
    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行平台api：获取英文姓名");
        String generatedName = EnglishNameGenerator.getInstance().generate();
        log.info("生成英文姓名的结果是：{}", generatedName);
        addResultInfo("英文姓名: ").addResultInfoLine(generatedName);
        putResultVariable(arg100, generatedName);
    }
}
