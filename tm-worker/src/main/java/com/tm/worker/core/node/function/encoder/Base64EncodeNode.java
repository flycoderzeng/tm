package com.tm.worker.core.node.function.encoder;

import com.tm.worker.core.node.function.FunctionNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


@Slf4j
public class Base64EncodeNode extends FunctionNode {
    private static final String ARG_1 = "srcString";

    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行平台api：base64 编码");
        String srcString = getArgStringValue(ARG_1);
        addResultInfo("需要编码的字符串: ").addResultInfoLine(srcString);
        if(StringUtils.isBlank(srcString)) {
            addResultInfoLine("原字符串是空");
            return ;
        }
        String encodedStr = java.util.Base64.getEncoder().encodeToString(srcString.getBytes());
        log.info("编码的结果是：{}", encodedStr);
        addResultInfo("result: ").addResultInfoLine(encodedStr);
        putResultVariable(arg100, encodedStr);
    }
}
