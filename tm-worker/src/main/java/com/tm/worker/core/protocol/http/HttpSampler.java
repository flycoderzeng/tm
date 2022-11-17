package com.tm.worker.core.protocol.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.tm.common.entities.autotest.enumerate.BodyTypeNum;
import com.tm.common.entities.autotest.enumerate.RawTypeNum;
import com.tm.common.entities.common.KeyValueRow;
import com.tm.worker.core.cookie.AutoTestCookie;
import com.tm.worker.core.exception.CommonValueBlankException;
import com.tm.worker.core.exception.SendHttpException;
import com.tm.worker.core.node.StepNodeBase;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.ExpressionUtils;
import com.tm.worker.utils.FunctionUtils;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class HttpSampler extends StepNodeBase {
    // GET POST etc.
    private String requestType;
    private String url;
    private List<KeyValueRow> params;
    private List<KeyValueRow> headers;
    // none form-data x-www-urlencoded raw
    private String bodyType;
    // text json xml
    private String rawType;
    private List<KeyValueRow> formData;
    private List<KeyValueRow> formUrlencoded;
    private List<KeyValueRow> checkErrorList;
    private List<KeyValueRow> responseExtractorList;
    private String content;

    @SneakyThrows
    @Override
    public void run() {
        super.run();
        log.info("执行http步骤，{}", getName());

        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();

        String actualUrl = getActualUrl(caseVariables);

        final Map<String, String> headerMap = getHeaderMap(caseVariables);

        final AutoTestCookie autoTestCookie = context.getAutoTestCookie();
        final List<HttpCookie> cookies = autoTestCookie.getCookies(actualUrl);

        HttpResponse response = executeHttp(caseVariables, actualUrl, headerMap, cookies);
        if(response == null) {
            throw new SendHttpException("发送http请求异常");
        }
        final String body = response.body();
        log.info(body);
        addResultInfo("响应报文: ").addResultInfoLine(body);
        addResultInfo("响应码: ").addResultInfoLine(response.getStatus());

        checkError(caseVariables, response);
        extractResponse(caseVariables, response);
    }

    private void extractResponse(AutoTestVariables caseVariables, HttpResponse body) {

    }

    private void checkError(AutoTestVariables caseVariables, HttpResponse body) {

    }

    private HttpResponse executeHttp(AutoTestVariables caseVariables, String actualUrl, Map<String, String> headerMap, List<HttpCookie> cookies) throws UnsupportedEncodingException {
        HttpResponse body = null;
        if(HttpMethod.GET.name().equals(requestType)) {
            addResultInfo("请求方法: ").addResultInfoLine(requestType);
            body = HttpRequest.get(actualUrl).headerMap(headerMap, true)
                    .cookie(cookies).timeout(60000).execute();
        }

        if(HttpMethod.POST.name().equals(requestType) && (StringUtils.equals(bodyType, BodyTypeNum.RAW.value())
                || StringUtils.equals(bodyType, BodyTypeNum.X_WWW_FORM_URLENCODED.value()))) {
            updateContentType(headerMap);
            if(StringUtils.equals(bodyType, BodyTypeNum.RAW.value()) && StringUtils.isNoneBlank(content)) {
                content = ExpressionUtils.replaceExpression(content, caseVariables.getVariables());
            }else if(StringUtils.equals(bodyType, BodyTypeNum.X_WWW_FORM_URLENCODED.value())) {
                content = getParamStr(caseVariables, formUrlencoded);
            }

            addResultInfo("请求报文: ").addResultInfoLine(content);
            body = HttpRequest.post(actualUrl).headerMap(headerMap, true)
                    .cookie(cookies).timeout(60000).body(content).execute();
        }

        return body;
    }

    private void updateContentType(Map<String, String> headerMap) {
        if(StringUtils.equals(rawType, RawTypeNum.JSON.value())) {
            headerMap.putIfAbsent(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }else if(StringUtils.equals(rawType, RawTypeNum.TEXT.value())) {
            headerMap.putIfAbsent(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
        }else if(StringUtils.equals(rawType, RawTypeNum.XML.value())) {
            headerMap.putIfAbsent(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
        }else if(StringUtils.equals(bodyType, BodyTypeNum.X_WWW_FORM_URLENCODED.value())) {
            headerMap.putIfAbsent(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        }
    }

    private String getActualUrl(AutoTestVariables caseVariables) throws UnsupportedEncodingException {
        if(StringUtils.isBlank(url)) {
            throw new CommonValueBlankException("请求地址不能为空");
        }
        addResultInfo("请求地址: ").addResultInfoLine(url);
        String actualUrl = ExpressionUtils.replaceExpression(url, caseVariables.getVariables());
        if(StringUtils.isBlank(actualUrl)) {
            throw new CommonValueBlankException("实际的请求地址不能为空");
        }

        // 拼装params
        String paramStr = getParamStr(caseVariables, params);
        if(actualUrl.indexOf('?') > -1) {
            actualUrl += "&" + paramStr;
        }else{
            actualUrl += "?" + paramStr;
        }
        log.info(actualUrl);
        addResultInfo("实际的请求地址: ").addResultInfoLine(actualUrl);

        return actualUrl;
    }

    private Map<String, String> getHeaderMap(AutoTestVariables caseVariables) {
        Map<String, String> headerMap = new HashMap<>();
        if(headers != null && !headers.isEmpty()) {
            for (KeyValueRow header : headers) {
                String name = header.getName();
                name = ExpressionUtils.replaceExpression(name, caseVariables.getVariables());
                String value = header.getValue();
                value = ExpressionUtils.replaceExpression(value, caseVariables.getVariables());
                headerMap.put(name, value);
            }
        }
        return headerMap;
    }

    private String getParamStr(AutoTestVariables caseVariables, List<KeyValueRow> keyValueRows) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder("");
        if (keyValueRows != null) {
            for (KeyValueRow param : keyValueRows) {
                String paramName = param.getName();
                if(StringUtils.isBlank(paramName)) {
                    throw new CommonValueBlankException("参数名称不能为空");
                }
                paramName = ExpressionUtils.replaceExpression(paramName, caseVariables.getVariables());
                if(StringUtils.isBlank(paramName)) {
                    throw new CommonValueBlankException("参数名称不能为空");
                }
                builder.append(FunctionUtils.encodeURIComponent(paramName)).append("&");
                String value = param.getValue();
                value = ExpressionUtils.replaceExpression(value, caseVariables.getVariables());
                builder.append(FunctionUtils.encodeURIComponent(value));
            }
        }

        return builder.toString();
    }
}
