package com.tm.worker.core.protocol.http;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.List;

public class HttpClientFactory {

    public static CloseableHttpClient createHttpClient() {
        return new HttpClientBuilderProxy().build();
    }

    public static CloseableHttpClient createHttpClient(List<BasicClientCookie> cookies) {
        return new HttpClientBuilderProxy().setCookies(cookies).build();
    }
}
