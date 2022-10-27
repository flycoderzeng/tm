package com.tm.worker.core.node.function.encoder;

import com.tm.worker.core.node.function.FunctionNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;


@Slf4j
public class EncodeURIComponentNode extends FunctionNode {
    private static final String ARG_1 = "srcString";

    @SneakyThrows
    @Override
    public void run() {
        //知识点：
        //1）encodeuricomponent和urlencoder.encode的区别在于前者对下面五个符号不编码
        //! %21
        //' %27
        //( %28
        //) %29
        //~ %7e
        //
        //2）decodeuricomponent和urldecoder.decode暂时还看不出有何区别
        super.run();
        log.info("执行平台api：encodeURIComponent 编码");
        String srcString = getArgStringValue(ARG_1);
        if(StringUtils.isBlank(srcString)) {
            addResultInfoLine("原字符串是空");
            return ;
        }
        addResultInfo("需要编码的字符串: ").addResultInfoLine(srcString);
        String encodedStr = URLEncoder.encode(srcString, "UTF-8");
        if(StringUtils.isNoneBlank(encodedStr)) {
            encodedStr = encodedStr.replace("%21", "!").replace("%27", "'")
                    .replace("%28", "(").replace("%29", ")")
                    .replace("~", "%7e");
        }
        log.info("编码的结果是：{}", encodedStr);
        addResultInfo("result: ").addResultInfoLine(encodedStr);
        putResultVariable(arg100, encodedStr);
    }
}
