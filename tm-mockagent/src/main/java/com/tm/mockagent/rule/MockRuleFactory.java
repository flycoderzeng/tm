package com.tm.mockagent.rule;

import com.tm.mockagent.entities.enumerate.HttpMethod;
import com.tm.mockagent.entities.model.HttpMockRule;
import com.tm.mockagent.entities.msg.PushMockRuleMsg;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MockRuleFactory {
    private static final Logger logger = Logger.getLogger(MockRuleFactory.class);

    public static final String CUSTOMIZE_MOCK_URI_PREFIX = "/__customize_mock";

    private final Map<String, HttpMockRule> httpMockRuleMap = new ConcurrentHashMap<>();

    public void putHttpRule(HttpMockRule rule) {
        final String key = getHttpRuleKey(rule);
        httpMockRuleMap.put(key, rule);
    }

    public URI getMockTargetUrl(URI sourceUrl, String method) {
        final String mockTargetUrl = getMockTargetUrl(sourceUrl.toString(), method);
        if (mockTargetUrl.equals(sourceUrl.toString())) {
            return sourceUrl;
        }
        try {
            return new URI(mockTargetUrl);
        } catch (URISyntaxException e) {
            logger.error("build new URI error, ", e);
            return sourceUrl;
        }
    }

    public String getMockTargetUrl(String sourceUrl, String method) {
        logger.info("判断 " + method + " " + sourceUrl + " 是否有配置mock规则");
        final HttpMethod httpMethod = HttpMethod.get(method);
        if (httpMethod == null) {
            return sourceUrl;
        }
        StringBuilder keyBuilder = new StringBuilder();
        int httpProtocolType = 1;
        try {
            final URI uri = new URI(sourceUrl);
            if (!uri.getScheme().equals("http")) {
                httpProtocolType = 2;
            }
            keyBuilder.append(httpProtocolType).append("_")
                    .append(uri.getHost()).append("_");
            if (uri.getPort() == -1) {
                if (httpProtocolType == 1) {
                    keyBuilder.append("80");
                } else {
                    keyBuilder.append("443");
                }
            } else {
                keyBuilder.append(uri.getPort());
            }
            keyBuilder.append("_").append(httpMethod.val()).append("_").append(uri.getPath());
            logger.info("http mock rule key: " + keyBuilder);
            final HttpMockRule httpMockRule = getHttpMockRule(keyBuilder.toString());
            if (httpMockRule != null) {
                logger.info("配置了mock规则");
                StringBuilder sb = new StringBuilder();
                sb.append(uri.getScheme()).append("://").append(httpMockRule.getMockTargetIp());
                if (httpMockRule.getMockSourcePort() != null) {
                    sb.append(":").append(httpMockRule.getMockTargetPort());
                }
                sb.append(CUSTOMIZE_MOCK_URI_PREFIX).append(uri.getPath());
                if (StringUtils.isNotBlank(uri.getQuery())) {
                    sb.append("?").append(uri.getQuery());
                    sb.append("&__MOCK_RULE_ID=").append(httpMockRule.getId());
                } else {
                    sb.append("?__MOCK_RULE_ID=").append(httpMockRule.getId());
                }
                logger.info("mock后的地址是：" + sb);
                return sb.toString();
            }
        } catch (URISyntaxException e) {
            logger.error("build new URI error, ", e);
            return sourceUrl;
        }

        return sourceUrl;
    }

    public HttpMockRule getHttpMockRule(String key) {
        final HttpMockRule httpMockRule = httpMockRuleMap.get(key);
        if (httpMockRule != null && httpMockRule.getEnabled() == 0) {
            return httpMockRule;
        }
        return null;
    }

    private String getHttpRuleKey(HttpMockRule rule) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(rule.getHttpProtocolType()).append("_").append(rule.getMockSourceIp()).append("_");
        if ((rule.getMockSourcePort() == null || rule.getMockSourcePort() < 0)) {
            if (rule.getHttpProtocolType() == 1) {
                stringBuilder.append("80");
            } else {
                stringBuilder.append("443");
            }
        } else {
            stringBuilder.append(rule.getMockSourcePort());
        }
        stringBuilder.append("_").append(rule.getMethod()).append("_").append(rule.getUri());
        return stringBuilder.toString();
    }

    public void clear() {
        httpMockRuleMap.clear();
    }

    public Integer getHttpRulesSize() {
        return httpMockRuleMap.size();
    }

    public void putRule(PushMockRuleMsg<?> mockRuleMsg) {
        if (mockRuleMsg == null) {
            return;
        }
        if (1 == mockRuleMsg.getMockRuleType()) {
            HttpMockRule httpMockRule = (HttpMockRule) mockRuleMsg.getMockRule();
            putHttpRule(httpMockRule);
        }
    }
}
