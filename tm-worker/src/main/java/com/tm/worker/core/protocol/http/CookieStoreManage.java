package com.tm.worker.core.protocol.http;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.List;


public class CookieStoreManage {

    public static BasicCookieStore createCookieStore(List<BasicClientCookie> cookies) {
        BasicCookieStore cookieStore = new BasicCookieStore();
        if(cookies != null && !cookies.isEmpty()) {
            cookies.forEach(cookie -> {
                cookieStore.addCookie(cookie);
            });
        }
        return cookieStore;
    }
}
