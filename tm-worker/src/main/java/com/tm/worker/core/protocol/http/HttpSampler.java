package com.tm.worker.core.protocol.http;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.jayway.jsonpath.JsonPath;
import com.tm.common.base.model.ApiIpPortConfig;
import com.tm.common.entities.autotest.enumerate.BodyTypeNum;
import com.tm.common.entities.autotest.enumerate.ExtractorTypeEnum;
import com.tm.common.entities.autotest.enumerate.RawTypeNum;
import com.tm.common.entities.common.KeyValueRow;
import com.tm.common.entities.common.enumerate.RelationOperatorEnum;
import com.tm.worker.core.cookie.AutoTestCookie;
import com.tm.worker.core.exception.TMException;
import com.tm.worker.core.node.StepNodeBase;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.AssertUtils;
import com.tm.worker.utils.ExpressionUtils;
import com.tm.worker.utils.FunctionUtils;
import com.tm.worker.utils.XMLUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.dom4j.Document;
import org.dom4j.Node;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.util.ArrayList;
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

    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行http步骤，{}", getName());

        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();

        String actualUrl = getActualUrl(caseVariables);

        final Map<String, String> headerMap = getHeaderMap(caseVariables);

        final AutoTestCookie autoTestCookie = context.getAutoTestCookie();
        final List<HttpCookie> cookies = autoTestCookie.getCookies(actualUrl);

        addResultInfo("Cookie: ").addResultInfoLine(cookies.toString());

        HttpResponse response = executeHttp(caseVariables, actualUrl, headerMap, cookies);
        if(!headerMap.isEmpty()) {
            addResultInfoLine("Request Headers: ");
            headerMap.forEach((k, v) -> addResultInfo("    ").addResultInfo(k).addResultInfo(": ").addResultInfoLine(v));
        }
        if(response == null) {
            throw new TMException("发送http请求异常");
        }
        log.info(response.toString());
        caseVariables.putObject(AutoTestVariables.BUILTIN_VARIABLE_NAME_RESPONSE_STATUS, response.getStatus());
        caseVariables.put(AutoTestVariables.BUILTIN_VARIABLE_NAME_RESPONSE, response.body());

        addResultInfoLine(response.toString());

        checkError(caseVariables, response);
        extractResponse(caseVariables, response);
    }

    private void extractResponse(AutoTestVariables caseVariables, HttpResponse httpResponse) {
        addResultInfoLine("响应提取结果:");
        if(responseExtractorList == null || responseExtractorList.isEmpty()) {
            addResultInfoLine("没有配置响应提取");
            return ;
        }
        for (KeyValueRow keyValueRow : responseExtractorList) {
            final String name = ExpressionUtils.replaceExpression(keyValueRow.getName(), caseVariables.getVariables());
            Object leftOperand = extractLeftOperand(httpResponse, keyValueRow, name);
            String resultVariable = ExpressionUtils.extractVariable(keyValueRow.getValue());
            if(StringUtils.isNoneBlank(resultVariable)) {
                addResultInfo("将").addResultInfo(name).addResultInfo("结果存入变量[").addResultInfo(resultVariable).addResultInfoLine("]");
                caseVariables.putObject(resultVariable, leftOperand);
            }
        }
    }

    private void checkError(AutoTestVariables caseVariables, HttpResponse httpResponse) {
        addResultInfoLine("断言检查结果:");
        if(checkErrorList == null || checkErrorList.isEmpty()) {
            addResultInfoLine("没有配置断言");
            return ;
        }
        boolean checkResult = true;
        for (KeyValueRow keyValueRow : checkErrorList) {
            checkResult = checkResult && checkResponseBody(caseVariables, httpResponse, keyValueRow);
        }
        if(!checkResult) {
            throw new TMException("响应断言失败");
        }
    }

    private Object extractLeftOperand(HttpResponse httpResponse, KeyValueRow keyValueRow, String name) {
        Document document;
        final String body = httpResponse.body();
        Object leftOperand = null;
        if(StringUtils.equals(keyValueRow.getExtractorType(), ExtractorTypeEnum.RESPONSE_BODY.val())) {
            if(name.startsWith("$.")) {
                try {
                    leftOperand = JsonPath.read(body, name);
                } catch (Exception e) {
                    leftOperand = e;
                }
            }else if(name.startsWith("/")) {
                document = XMLUtils.parseXmlString(body);
                if(document == null) {
                    throw new TMException("http的响应不是xml字符串");
                }
                List<Node> nodes = XMLUtils.selectNodeList(document, name);
                if(nodes != null && !nodes.isEmpty()) {
                    leftOperand = nodes.get(0).getStringValue();
                }
            }else{
                leftOperand = body;
            }
        }else if(StringUtils.equals(keyValueRow.getExtractorType(), ExtractorTypeEnum.RESPONSE_HEADER.val())) {
            leftOperand = httpResponse.header(name);
        }else if(StringUtils.equals(keyValueRow.getExtractorType(), ExtractorTypeEnum.COOKIE.val())) {
            leftOperand = httpResponse.getCookie(name).getValue();
        }

        return leftOperand;
    }

    private boolean checkResponseBody(AutoTestVariables caseVariables, HttpResponse httpResponse, KeyValueRow keyValueRow) {
        final String name = ExpressionUtils.replaceExpression(keyValueRow.getName(), caseVariables.getVariables());
        Object leftOperand = extractLeftOperand(httpResponse, keyValueRow, name);
        final String value = ExpressionUtils.replaceExpression(keyValueRow.getValue(), caseVariables.getVariables());
        final RelationOperatorEnum relationOperator = RelationOperatorEnum.get(keyValueRow.getRelationOperator());
        addResultInfo(name).addResultInfo("[").addResultInfo(leftOperand.toString()).addResultInfo("] ")
                .addResultInfo(relationOperator.desc()).addResultInfo(" ").addResultInfo(value);
        if(AssertUtils.compare(leftOperand, relationOperator, value)) {
            addResultInfoLine("[成功]");
            return true;
        }else{
            addResultInfoLine("[失败]");
            return false;
        }
    }

    private HttpResponse executeHttp(AutoTestVariables caseVariables, String actualUrl, Map<String, String> headerMap, List<HttpCookie> cookies) throws UnsupportedEncodingException {
        HttpResponse httpResponse = null;
        addResultInfo("请求方法: ").addResultInfoLine(requestType);
        if(HttpMethod.GET.name().equals(requestType)) {
            httpResponse = HttpRequest.get(actualUrl).headerMap(headerMap, true)
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
            caseVariables.put(AutoTestVariables.BUILTIN_VARIABLE_NAME_REQUEST, content);

            addResultInfoLine("请求报文: ").addResultInfoLine(content);

            httpResponse = HttpRequest.post(actualUrl).headerMap(headerMap, true)
                    .cookie(cookies).timeout(60000).body(content).execute();
        }else if(HttpMethod.POST.name().equals(requestType) && StringUtils.equals(bodyType, BodyTypeNum.FORM_DATA.value())) {
            Map<String, Object> formMap = getFormMap(caseVariables);
            httpResponse = HttpRequest.post(actualUrl).headerMap(headerMap, true).cookie(cookies).form(formMap).timeout(60000).execute();
        }

        return httpResponse;
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
        AutoTestContext context = AutoTestContextService.getContext();
        if(StringUtils.isBlank(url)) {
            throw new TMException("请求地址不能为空");
        }
        log.info(url);

        String path = "";

        addResultInfo("请求地址: ").addResultInfoLine(url);

        List<ApiIpPortConfig> apiIpPortConfigs = new ArrayList<>();


        String actualUrl = "";

        String pathParams = ReUtil.get("/\\$\\{(.*?)\\}", url, 0);
        if(StringUtils.isNoneBlank(pathParams)) {
            path = ReUtil.replaceAll(url, ".*://[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{1,4}", "");
            log.info(path);
            int index = path.indexOf("?");
            if(index > -1) {
                path = path.substring(0, index);
                log.info(path);
            }
            actualUrl = ExpressionUtils.replaceExpression(url, caseVariables.getVariables());
        }else {
            actualUrl = ExpressionUtils.replaceExpression(url, caseVariables.getVariables());
            path = URLUtil.getPath(actualUrl);
            log.info(path);
        }
        apiIpPortConfigs = context.getTaskService().selectByUrlAndEnvId(path, context.getPlanTask().getRunningConfigSnapshot().getEnvId());
        if(apiIpPortConfigs != null && apiIpPortConfigs.size() > 1) {
            throw new TMException("接口: " + path + ", 环境: "
                    + context.getPlanTask().getRunningConfigSnapshot().getEnvName() + ", 存在多条配置");
        }


        if(StringUtils.isBlank(actualUrl)) {
            throw new TMException("实际的请求地址不能为空");
        }

        // 拼装params
        String paramStr = getParamStr(caseVariables, params);
        if(actualUrl.indexOf('?') > -1 && StringUtils.isNoneBlank(paramStr)) {
            actualUrl += "&" + paramStr;
        } else if(StringUtils.isNoneBlank(paramStr)) {
            actualUrl += "?" + paramStr;
        }

        if(apiIpPortConfigs != null && !apiIpPortConfigs.isEmpty()) {
            ApiIpPortConfig apiIpPortConfig = apiIpPortConfigs.get(0);
            log.info("将接口路径中的ip和端口替换为环境配置的ip和端口");
            actualUrl = ReUtil.replaceAll(actualUrl, "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{1,4}",
                    apiIpPortConfig.getIp() + ":" + apiIpPortConfig.getPort());
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
            for (int i = 0; i < keyValueRows.size(); i++) {
                KeyValueRow param = keyValueRows.get(i);
                String paramName = param.getName();
                if(StringUtils.isBlank(paramName)) {
                    throw new TMException("参数名称不能为空");
                }
                paramName = ExpressionUtils.replaceExpression(paramName, caseVariables.getVariables());
                if(StringUtils.isBlank(paramName)) {
                    throw new TMException("参数名称不能为空");
                }
                builder.append(FunctionUtils.encodeURIComponent(paramName)).append("=");
                String value = param.getValue();
                value = ExpressionUtils.replaceExpression(value, caseVariables.getVariables());
                builder.append(FunctionUtils.encodeURIComponent(value));
                if(i < keyValueRows.size() - 1) {
                    builder.append("&");
                }
            }
        }

        return builder.toString();
    }
}
