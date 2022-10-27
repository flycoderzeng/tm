package com.tm.worker.core.protocol.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.*;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;


@Slf4j
public class HttpClientBuilderProxy {

    @Value("${spring.credentials-path}")
    private String credentialsPath;

    HttpClientBuilder httpClientBuilder;
    public HttpClientBuilderProxy() {
        this.httpClientBuilder = HttpClients.custom();
    }

    public static HttpClientBuilderProxy create() {
        return new HttpClientBuilderProxy();
    }

    public final HttpClientBuilderProxy setCookies(List<BasicClientCookie> cookies) {
        BasicCookieStore cookieStore = CookieStoreManage.createCookieStore(cookies);
        if(!cookieStore.getCookies().isEmpty()) {
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }
        return this;
    }

    public final HttpClientBuilderProxy ignoreCredentials() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslcontext = SSLContexts.custom()
                // 忽略掉对服务器端证书的校验
                .loadTrustMaterial(new TrustStrategy() {
                    @Override
                    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        return true;
                    }
                }).build();
        if(sslcontext != null) {
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext,
                    new String[] { "TLSv1", "TLSv1.1", "TLSv1.2"}, null,
                    new DefaultHostnameVerifier());
            httpClientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);
        }
        return this;
    }

    public final HttpClientBuilderProxy withCredentials(String keyStoreRelativePath, String keyPassword,
                                                        String trustStoreRelativePath, String trustPassword) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException {
        String format = "jks";
        if(keyStoreRelativePath.toLowerCase().endsWith("pfx")) {
            format = "pkcs12";
        }
        SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
        KeyStore keyStore = KeyStore.getInstance(format);
        try (FileInputStream inStream
                     = new FileInputStream(new File(credentialsPath + File.separator + keyStoreRelativePath))) {
            keyStore.load(inStream, keyPassword.toCharArray());
        }
        sslContextBuilder.loadKeyMaterial(keyStore, keyPassword.toCharArray());
        if(trustStoreRelativePath.toLowerCase().endsWith("jks")) {
            format = "jks";
        }
        KeyStore trustStore = KeyStore.getInstance(format);
        try (FileInputStream inStream
                     = new FileInputStream(new File(credentialsPath  + File.separator + trustStoreRelativePath))) {
            trustStore.load(inStream, trustPassword.toCharArray());
        }
        sslContextBuilder.loadTrustMaterial(trustStore, new TrustAllStrategy());

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build(),
                new String[] { "TLSv1", "TLSv1.1", "TLSv1.2"}, null,
                new DefaultHostnameVerifier());
        httpClientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);

        return this;
    }

    public CloseableHttpClient build() {
        return httpClientBuilder.build();
    }
}
