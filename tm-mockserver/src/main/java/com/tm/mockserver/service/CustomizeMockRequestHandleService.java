package com.tm.mockserver.service;

import com.tm.common.base.mapper.HttpMockRuleMapper;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Getter
@Slf4j
@Service
public class CustomizeMockRequestHandleService {

    private final HttpMockRuleMapper httpMockRuleMapper;

    public static final String MOCK_RULE_ID = "__MOCK_RULE_ID";

    @Inject
    public CustomizeMockRequestHandleService(HttpMockRuleMapper httpMockRuleMapper) {
        this.httpMockRuleMapper = httpMockRuleMapper;
    }

    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestURI = request.getRequestURI();
        String mockRuleIdStr = request.getParameter(MOCK_RULE_ID);

        log.info("requestURI: {}", requestURI);
        log.info("mockRuleIdStr: {}", mockRuleIdStr);

        if(StringUtils.isBlank(mockRuleIdStr)) {
            writeResponse(request, response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "mock规则不存在，mock规则id：" + mockRuleIdStr + "!");
            return;
        }


        // ?a=b&a=c 那么a的参数值就是["b", "c"]
        Map<String, String[]> parameterMap = request.getParameterMap();

        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = response.getWriter();
        printWriter.write("这是来自mock的响应，mock规则id：" + mockRuleIdStr + "!");
        printWriter.flush();
        printWriter.close();
    }

    public void writeResponse(HttpServletRequest request,
                              HttpServletResponse response,
                              int responseCode,
                              String responseBody) throws IOException {
        String contentType = request.getHeader("Content-Type");
        response.setContentType(contentType);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(responseCode);
        PrintWriter printWriter = response.getWriter();
        printWriter.write(responseBody);
        printWriter.flush();
        printWriter.close();
    }

}
