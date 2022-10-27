package com.tm.worker.core.protocol.http;

import com.tm.common.entities.common.KeyValueRow;
import com.tm.worker.core.exception.CommonValueBlankException;
import com.tm.worker.core.node.StepNodeBase;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.ExpressionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

import java.util.List;

@Slf4j
@Data
public class HttpSampler extends StepNodeBase {
    // GET POST etc.
    private String requestType;
    private String url;
    private List<KeyValueRow> params;
    private List<KeyValueRow> headers;
    private String bodyType;
    private String rawType;
    private List<KeyValueRow> formData;
    private List<KeyValueRow> formUrlencoded;
    private List<KeyValueRow> checkErrorList;
    private List<KeyValueRow> responseExtractorList;
    private String content;

    @Override
    public void run() {
        super.run();
        log.info("执行http步骤，{}", getName());
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        if(StringUtils.isBlank(url)) {
            throw new CommonValueBlankException("url不能为空");
        }
        addResultInfo("url: ").addResultInfoLine(url);
        String actualUrl = ExpressionUtils.replaceExpression(url, caseVariables.getVariables());
        if(StringUtils.isBlank(url)) {
            throw new CommonValueBlankException("url不能为空");
        }
        addResultInfo("实际的url: ").addResultInfoLine(actualUrl);

        // 拼装params
        if (params != null) {

        }

        if(HttpMethod.POST.name().equals(requestType)) {

        }
    }
}
