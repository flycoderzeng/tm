package com.tm.worker.core.node.function.secure;

import cn.hutool.crypto.SecureUtil;
import com.tm.worker.core.node.function.FunctionNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


@Slf4j
public class Md5Node extends FunctionNode {
    private static final String ARG_1 = "srcString";

    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行平台api：获取字符串md5值");
        String srcString = getArgStringValue(ARG_1);
        addResultInfo("需要获取md5值的字符串: ").addResultInfoLine(srcString);
        if(StringUtils.isBlank(srcString)) {
            addResultInfoLine("原字符串是空");
            return ;
        }
        String md5 = SecureUtil.md5(srcString);
        log.info("md5的结果是：{}", md5);
        addResultInfo("result: ").addResultInfoLine(md5);
        putResultVariable(arg100, md5);
    }
}
