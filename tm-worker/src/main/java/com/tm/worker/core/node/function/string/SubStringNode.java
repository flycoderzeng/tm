package com.tm.worker.core.node.function.string;

import cn.hutool.core.util.StrUtil;
import com.tm.worker.core.node.function.FunctionNode;
import com.tm.worker.utils.FunctionUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


@Slf4j
public class SubStringNode extends FunctionNode {
    private static final String ARG_1 = "srcString";
    private static final String ARG_2 = "fromIndexInclude";
    private static final String ARG_3 = "toIndexExclude";
    private static final String ARG_4 = "fromStringExclude";
    private static final String ARG_5 = "toStringExclude";
    private static final String ARG_6 = "length";

    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行平台api：截取字符串");
        String srcString = getArgStringValue(ARG_1);
        if(StringUtils.isBlank(srcString)) {
            addResultInfoLine("原字符串是空");
            return ;
        }

        final String fromIndexIncludeStr = getArgStringValue(ARG_2);
        Integer fromIndexInclude = FunctionUtils.getIntegerFromString(fromIndexIncludeStr, ARG_2, 0);

        final String toIndexExcludeStr = getArgStringValue(ARG_3);
        Integer toIndexExclude = FunctionUtils.getIntegerFromString(toIndexExcludeStr, ARG_3, null);

        final String fromStringExclude = getArgStringValue(ARG_4, null);
        final String toStringExclude = getArgStringValue(ARG_5, null);

        final String lengthStr = getArgStringValue(ARG_6);
        final Integer length = FunctionUtils.getIntegerFromString(lengthStr, ARG_6, null);

        if(toIndexExclude == null && length == null) {
            toIndexExclude = srcString.length();
        }

        if(StringUtils.isNoneEmpty(fromStringExclude)) {
            final int index = srcString.indexOf(fromStringExclude);
            if(index < 0) {
                fromIndexInclude = 0;
            }else{
                fromIndexInclude = index + fromStringExclude.length();
            }
        }
        if(StringUtils.isNoneEmpty(toStringExclude)) {
            final int index = srcString.indexOf(toStringExclude);
            if(index < 0) {
                toIndexExclude = srcString.length();
            }else{
                toIndexExclude = index;
            }
        }
        if(toIndexExclude == null && length != null) {
            toIndexExclude = fromIndexInclude + length;
        }

        final String result = StrUtil.sub(srcString, fromIndexInclude, toIndexExclude);
        log.info("截取的结果是：{}", result);
        addResultInfo("result: ").addResultInfoLine(result);
        putResultVariable(arg100, result);
    }
}
