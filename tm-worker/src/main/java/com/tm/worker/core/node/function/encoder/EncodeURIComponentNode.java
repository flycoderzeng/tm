package com.tm.worker.core.node.function.encoder;

import com.tm.worker.core.node.function.FunctionNode;
import com.tm.worker.utils.FunctionUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


@Slf4j
public class EncodeURIComponentNode extends FunctionNode {
    private static final String ARG_1 = "srcString";

    @SneakyThrows
    @Override
    public void run() {
        super.run();
        log.info("执行平台api：encodeURIComponent 编码");
        String srcString = getArgStringValue(ARG_1);
        if(StringUtils.isBlank(srcString)) {
            addResultInfoLine("原字符串是空");
            return ;
        }
        addResultInfo("需要编码的字符串: ").addResultInfoLine(srcString);
        String encodedStr = FunctionUtils.encodeURIComponent(srcString);
        log.info("编码的结果是：{}", encodedStr);
        addResultInfo("result: ").addResultInfoLine(encodedStr);
        putResultVariable(arg100, encodedStr);
    }
}
