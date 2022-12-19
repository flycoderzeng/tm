package com.tm.worker.core.node.function.extractor;

import com.jayway.jsonpath.JsonPath;
import com.tm.worker.core.exception.TMException;
import com.tm.worker.core.node.function.FunctionNode;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JsonMultiExtractorNode extends FunctionNode {
    private static final String ARG_0 = "content";
    private static final String ARG_1 = "path_1";
    private static final String ARG_2 = "path_2";
    private static final String ARG_3 = "path_3";
    private static final String ARG_4 = "path_4";
    private static final String ARG_5 = "path_5";
    private static final String ARG_6 = "path_6";
    private static final String ARG_7 = "path_7";
    private static final String ARG_8 = "path_8";
    private static final String ARG_9 = "path_9";
    private static final String ARG_10 = "path_10";

    private static final String OUT_1 = "result_1";
    private static final String OUT_2 = "result_2";
    private static final String OUT_3 = "result_3";
    private static final String OUT_4 = "result_4";
    private static final String OUT_5 = "result_5";
    private static final String OUT_6 = "result_6";
    private static final String OUT_7 = "result_7";
    private static final String OUT_8 = "result_8";
    private static final String OUT_9 = "result_9";
    private static final String OUT_10 = "result_10";

    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行 json报文提取器");
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();

        List<String> argList = new ArrayList<>();
        argList.add(ARG_1);
        argList.add(ARG_2);
        argList.add(ARG_3);
        argList.add(ARG_4);
        argList.add(ARG_5);
        argList.add(ARG_6);
        argList.add(ARG_7);
        argList.add(ARG_8);
        argList.add(ARG_9);
        argList.add(ARG_10);


        List<String> outList = new ArrayList<>();
        outList.add(OUT_1);
        outList.add(OUT_2);
        outList.add(OUT_3);
        outList.add(OUT_4);
        outList.add(OUT_5);
        outList.add(OUT_6);
        outList.add(OUT_7);
        outList.add(OUT_8);
        outList.add(OUT_9);
        outList.add(OUT_10);

        String content = getContent(ARG_0, caseVariables);
        if(StringUtils.isBlank(content)) {
            log.info("json提取，取内置变量__response");
            content = caseVariables.getVariableString(AutoTestVariables.BUILTIN_VARIABLE_NAME_RESPONSE);
        }
        if(StringUtils.isBlank(content)) {
            log.info("json提取，content为空");
            throw new TMException("参数[content]内容为空");
        }
        // json提取
        for (int i = 0; i < argList.size(); i++) {
            String path = getContent(argList.get(i), caseVariables);
            if(StringUtils.isNotBlank(path)) {
                addResultInfo("json路径: ").addResultInfoLine(path);
                Object result = JsonPath.read(content, path);
                addResultInfo("json路径提取结果: ").addResultInfoLine(result);
                putResultVariable(outList.get(i), result.toString());
            }
        }
    }
}
