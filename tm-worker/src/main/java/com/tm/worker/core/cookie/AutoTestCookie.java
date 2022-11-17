package com.tm.worker.core.cookie;

import com.tm.common.entities.common.KeyValueRow;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.ExpressionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Data
@Slf4j
public class AutoTestCookie {
    private List<KeyValueRow> cookies;

    public AutoTestCookie(List<KeyValueRow> cookies) {
        this.cookies = cookies;
    }

    public List<HttpCookie> getCookies(String url) throws URISyntaxException {
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();

        if(cookies == null || cookies.isEmpty()) {
            return Collections.emptyList();
        }

        List<HttpCookie> httpCookies = new ArrayList<>();
        URI uri = new URI(url);
        final String host = uri.getHost();

        String path = uri.getPath();
        if(StringUtils.isBlank(path)) {
            path = "/";
        }
        for (KeyValueRow cookie : cookies) {
            String domain = cookie.getDomain();
            domain = ExpressionUtils.replaceExpression(domain, caseVariables.getVariables());
            String rowPath = cookie.getPath();
            rowPath = ExpressionUtils.replaceExpression(rowPath, caseVariables.getVariables());
            if(StringUtils.isBlank(path)) {
                rowPath = "/";
            }

            if(StringUtils.equals(domain, host) && StringUtils.equals(rowPath, path)) {
                final String name = ExpressionUtils.replaceExpression(cookie.getName(), caseVariables.getVariables());
                final String value = ExpressionUtils.replaceExpression(cookie.getValue(), caseVariables.getVariables());
                httpCookies.add(new HttpCookie(name,value));
            }
        }

        return httpCookies;
    }
}
