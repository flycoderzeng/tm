package com.tm.worker.core.node.function.decoder;

import com.tm.worker.core.node.function.FunctionNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


@Slf4j
public class Base64DecodeNode extends FunctionNode {
    private static final String ARG_1 = "encodedString";

    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行平台api：base64 解码");
        String encodedString = getArgStringValue(ARG_1);
        addResultInfo("需要解码的字符串: ").addResultInfoLine(encodedString);
        if(StringUtils.isBlank(encodedString)) {
            addResultInfoLine("原字符串是空");
            return ;
        }
        byte[] decodeBytes = java.util.Base64.getDecoder().decode(encodedString);
        final String result = new String(decodeBytes);
        log.info("解码的结果是：{}", result);
        addResultInfo("result: ").addResultInfoLine(result);
        putResultVariable(arg100, result);
    }
}
