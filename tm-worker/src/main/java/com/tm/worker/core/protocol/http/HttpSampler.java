package com.tm.worker.core.protocol.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.jayway.jsonpath.JsonPath;
import com.tm.common.entities.autotest.enumerate.BodyTypeNum;
import com.tm.common.entities.autotest.enumerate.ExtractorTypeEnum;
import com.tm.common.entities.autotest.enumerate.RawTypeNum;
import com.tm.common.entities.common.KeyValueRow;
import com.tm.common.entities.common.enumerate.RelationOperatorEnum;
import com.tm.worker.core.cookie.AutoTestCookie;
import com.tm.worker.core.exception.CommonValueBlankException;
import com.tm.worker.core.exception.InvalidXMLStringException;
import com.tm.worker.core.exception.SendHttpException;
import com.tm.worker.core.node.StepNodeBase;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.AssertUtils;
import com.tm.worker.utils.ExpressionUtils;
import com.tm.worker.utils.FunctionUtils;
import com.tm.worker.utils.XMLUtils;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.File;
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
        if(!headerMap.isEmpty()) {
            addResultInfoLine("Request Headers: ");
            headerMap.forEach((k, v) -> addResultInfo("    ").addResultInfo(k).addResultInfo(": ").addResultInfoLine(v));
        }
        addResultInfo("Cookie: ").addResultInfoLine(cookies.toString());
        addResultInfoLine("请求报文: ").addResultInfoLine(content);
        if(response == null) {
            throw new SendHttpException("发送http请求异常");
        }
        log.info(response.toString());
        addResultInfoLine(response.toString());

        checkError(caseVariables, response);
        extractResponse(caseVariables, response);
    }

    private void extractResponse(AutoTestVariables caseVariables, HttpResponse httpResponse) {
        if(responseExtractorList == null || responseExtractorList.isEmpty()) {
            return ;
        }
        final String body = httpResponse.body();
    }

    private void checkError(AutoTestVariables caseVariables, HttpResponse httpResponse) {
        addResultInfoLine("断言检查结果");
        if(checkErrorList == null || checkErrorList.isEmpty()) {
            addResultInfoLine("没有配置断言");
            return ;
        }
        final String body = httpResponse.body();

        for (KeyValueRow keyValueRow : checkErrorList) {
            if(StringUtils.equals(keyValueRow.getExtractorType(), ExtractorTypeEnum.RESPONSE_BODY.val())) {
                checkResponseBody(caseVariables, body, keyValueRow);
            }
        }
    }

    private void checkResponseBody(AutoTestVariables caseVariables, String body, KeyValueRow keyValueRow) {
        Document document;
        final String name = ExpressionUtils.replaceExpression(keyValueRow.getName(), caseVariables.getVariables());
        final String value = ExpressionUtils.replaceExpression(keyValueRow.getValue(), caseVariables.getVariables());
        if(name.startsWith("$.")) {
            final Object read = JsonPath.read(body, name);
            final RelationOperatorEnum relationOperator = keyValueRow.getRelationOperator();
            addResultInfo(name).addResultInfo(" ").addResultInfo(relationOperator.desc()).addResultInfo(" ").addResultInfo(value);
            if(AssertUtils.compare(read.toString(), relationOperator, value)) {
                addResultInfo("[成功]");
            }else{
                addResultInfo("[失败]");
            }
        }else if(name.startsWith("/")) {
            document = XMLUtils.parseXmlString(body);
            if(document == null) {
                throw new InvalidXMLStringException("http的响应不是xml字符串");
            }
        }
    }

    private HttpResponse executeHttp(AutoTestVariables caseVariables, String actualUrl, Map<String, String> headerMap, List<HttpCookie> cookies) throws UnsupportedEncodingException {
        HttpResponse body = null;
        addResultInfo("请求方法: ").addResultInfoLine(requestType);
        if(HttpMethod.GET.name().equals(requestType)) {
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
            body = HttpRequest.post(actualUrl).headerMap(headerMap, true)
                    .cookie(cookies).timeout(60000).body(content).execute();
        }else if(HttpMethod.POST.name().equals(requestType) && StringUtils.equals(bodyType, BodyTypeNum.FORM_DATA.value())) {
            Map<String, Object> formMap = getFormMap(caseVariables);
            HttpRequest.post(actualUrl).headerMap(headerMap, true).cookie(cookies).form(formMap).timeout(60000).execute();
        }

        return body;
    }

    private Map<String, Object> getFormMap(AutoTestVariables caseVariables) {
        Map<String, Object> formMap = new HashMap<>();
        if(formData != null && !formData.isEmpty()) {
            for (KeyValueRow formDatum : formData) {
                final String name = ExpressionUtils.replaceExpression(formDatum.getName(), caseVariables.getVariables());
                final String value = ExpressionUtils.replaceExpression(formDatum.getValue(), caseVariables.getVariables());
                if(StringUtils.equals("text", formDatum.getType())) {
                    formMap.put(name, value);
                } else if (StringUtils.equals("file", formDatum.getType())) {
                    formMap.put(name, new File(value));
                }
            }
        }
        return formMap;
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
