package com.tm.worker.core.protocol.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
public class HttpEmitter {

    public static final RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(60000).setConnectionRequestTimeout(120000).setSocketTimeout(120000).build();
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_XML = "application/xml";
    public static final String URL_IS_BLANK = "请求的地址为空!";

    HttpEmitter() {}

    public static void packingHttpProxyResponse(HttpProxyResponse proxyResponse,CloseableHttpResponse response) throws IOException {
        String resp = EntityUtils.toString(response.getEntity());

        proxyResponse.setStatusCode(response.getStatusLine().getStatusCode());
        proxyResponse.setReasonPhrase(response.getStatusLine().getReasonPhrase());
        proxyResponse.setResponseAllHeaders(response.getAllHeaders());
        proxyResponse.setResponseBodyContent(resp);
    }

    public static HttpProxyResponse post(String url,
                                         Object data,
                                         List<BasicNameValuePair> params,
                                         List<BasicNameValuePair> headers,
                                         List<BasicClientCookie> cookies) throws BlankUrlException, IOException, URISyntaxException {

        if(StringUtils.isBlank(url)) {
            throw new BlankUrlException(URL_IS_BLANK);
        }
        log.info(url);
        HttpProxyResponse proxyResponse = new HttpProxyResponse();
        proxyResponse.setUrl(url);
        String requestBodyContent = data == null ? "" : data.toString();
        proxyResponse.setRequestBodyContent(requestBodyContent);

        try (CloseableHttpClient httpClient = HttpClientFactory.createHttpClient(cookies)) {
            CloseableHttpResponse response;
            HttpPost httpPost;
            if (params != null) {
                URIBuilder uriBuilder = createURIBuilder(url, params);
                if (uriBuilder != null) {
                    httpPost = new HttpPost(uriBuilder.build());
                } else {
                    httpPost = new HttpPost(url);
                }
            } else {
                httpPost = new HttpPost(url);
            }
            httpPost.setConfig(requestConfig);
            StringEntity entity = new StringEntity(requestBodyContent);
            httpPost.setEntity(entity);

            if (headers != null && !headers.isEmpty()) {
                headers.forEach(row -> httpPost.setHeader(row.getName(), row.getValue()));
            }

            response = httpClient.execute(httpPost);

            log.info("状态码: {}\t\treason phrase: {}", response.getStatusLine().getStatusCode(),
                    response.getStatusLine().getReasonPhrase());

            proxyResponse.setRequestAllHeaders(httpPost.getAllHeaders());
            packingHttpProxyResponse(proxyResponse, response);
        }

        return proxyResponse;
    }

    public static HttpProxyResponse post(String url, Object data,
                                         List<BasicNameValuePair> params,
                                         List<BasicNameValuePair> headers) throws BlankUrlException, IOException, URISyntaxException {
        return post(url, data, params, headers, null);
    }

    public static HttpProxyResponse postJson(String url, Object data) throws BlankUrlException, IOException, URISyntaxException {
        List<BasicNameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair(CONTENT_TYPE, APPLICATION_JSON));
        return post(url, data, null, headers);
    }

    public static HttpProxyResponse postXml(String url, Object data) throws BlankUrlException, IOException, URISyntaxException {
        List<BasicNameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair(CONTENT_TYPE, APPLICATION_XML));
        return post(url, data, null, headers);
    }

    public static boolean findContentTypeHeader(List<BasicNameValuePair> headers) {
        boolean found = false;
        for (BasicNameValuePair header : headers) {
            if(header.getName().equalsIgnoreCase(CONTENT_TYPE)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public static HttpProxyResponse postJson(String url, Object data,
                                             List<BasicNameValuePair> headers) throws BlankUrlException, IOException, URISyntaxException {
        boolean found = findContentTypeHeader(headers);
        if(!found) {
            headers.add(new BasicNameValuePair(CONTENT_TYPE, APPLICATION_JSON));
        }
        return post(url, data, null, headers);
    }

    public static HttpProxyResponse postJson(String url, Object data,
                                             List<BasicNameValuePair> headers,
                                             List<BasicClientCookie> cookies) throws BlankUrlException, IOException, URISyntaxException {
        boolean found = findContentTypeHeader(headers);
        if(!found) {
            headers.add(new BasicNameValuePair(CONTENT_TYPE, APPLICATION_JSON));
        }
        return post(url, data, null, headers, cookies);
    }

    public static HttpProxyResponse postXml(String url, Object data,
                                            List<BasicNameValuePair> headers,
                                            List<BasicClientCookie> cookies) throws BlankUrlException, IOException, URISyntaxException {
        boolean found = findContentTypeHeader(headers);
        if(!found) {
            headers.add(new BasicNameValuePair(CONTENT_TYPE, APPLICATION_XML));
        }
        return post(url, data, null, headers, cookies);
    }

    public static HttpProxyResponse postXml(String url, Object data,
                                            List<BasicNameValuePair> headers) throws BlankUrlException, IOException, URISyntaxException {
        boolean found = findContentTypeHeader(headers);
        if(!found) {
            headers.add(new BasicNameValuePair(CONTENT_TYPE, APPLICATION_XML));
        }
        return post(url, data, null, headers);
    }

    public static String transferMapToFormUrlencodedString(Map<Object, Object> map) {
        URIBuilder uriBuilder = new URIBuilder();
        map.forEach((key, value) -> uriBuilder.setParameter(key.toString(), value.toString()));
        try {
            String result = uriBuilder.build().toString();
            if(result.startsWith("?")) {
                return result.substring(1);
            }
            return result;
        } catch (URISyntaxException e) {
            log.error("uri builder error, ", e);
            return "";
        }
    }

    public static HttpProxyResponse postFormUrlencoded(String url, Object data,
                                                       List<BasicNameValuePair> headers,
                                                       List<BasicClientCookie> cookies) throws BlankUrlException, IOException, URISyntaxException {
        boolean found = findContentTypeHeader(headers);
        if(!found) {
            headers.add(new BasicNameValuePair(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED));
        }
        if(data instanceof Map) {
            Map<Object, Object> map = (Map)data;
            data = transferMapToFormUrlencodedString(map);
        }
        return post(url, data, null, headers, cookies);
    }

    public static HttpProxyResponse postFormUrlencoded(String url,
                                                       Object data,
                                                       List<BasicNameValuePair> headers) throws BlankUrlException, IOException, URISyntaxException {
        boolean found = findContentTypeHeader(headers);
        if(!found) {
            headers.add(new BasicNameValuePair(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED));
        }
        return postFormUrlencoded(url, data, headers, null);
    }

    public static HttpProxyResponse postFormUrlencoded(String url, Object data) throws BlankUrlException, IOException, URISyntaxException {
        List<BasicNameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED));
        return postFormUrlencoded(url, data, headers, null);
    }

    public static URIBuilder createURIBuilder(String url, List<BasicNameValuePair> params) throws URISyntaxException {
        if(params == null || params.isEmpty()) {
            return null;
        }
        URIBuilder uriBuilder = new URIBuilder(url);
        params.forEach(param -> uriBuilder.setParameter(param.getName(), param.getValue()));
        return uriBuilder;
    }

    public static HttpProxyResponse get(String url, List<BasicNameValuePair> params,
                                        List<BasicNameValuePair> headers,
                                        List<BasicClientCookie> cookies) throws BlankUrlException, IOException, URISyntaxException {

        if(StringUtils.isBlank(url)) {
            throw new BlankUrlException(URL_IS_BLANK);
        }
        HttpProxyResponse proxyResponse = new HttpProxyResponse();
        proxyResponse.setUrl(url);
        log.info(url);

        try (CloseableHttpClient httpClient = HttpClientFactory.createHttpClient(cookies)) {
            CloseableHttpResponse response;
            HttpGet httpGet;
            URIBuilder uriBuilder = createURIBuilder(url, params);
            if (uriBuilder == null) {
                httpGet = new HttpGet(url);
            } else {
                httpGet = new HttpGet(uriBuilder.build());
            }
            httpGet.setConfig(requestConfig);
            proxyResponse.setRequestBodyContent(httpGet.getURI().getQuery());

            if (headers != null && !headers.isEmpty()) {
                headers.forEach(row -> httpGet.setHeader(row.getName(), row.getValue()));
            }

            response = httpClient.execute(httpGet);
            log.info("状态码: {}\t\t{}", response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
            proxyResponse.setRequestAllHeaders(httpGet.getAllHeaders());
            packingHttpProxyResponse(proxyResponse, response);

        }

        return proxyResponse;
    }

    public static HttpProxyResponse get(String url, List<BasicNameValuePair> params, List<BasicNameValuePair> headers) throws BlankUrlException, IOException, URISyntaxException {
        return get(url, params, headers, null);
    }

    public static HttpProxyResponse get(String url, Map<String, String> params, List<BasicNameValuePair> headers) throws BlankUrlException, IOException, URISyntaxException {
        List<BasicNameValuePair> list = new ArrayList<>();
        if(params != null) {
            params.forEach((key, value) -> list.add(new BasicNameValuePair(key, value)));
        }

        return get(url, list, headers, null);
    }

    public static HttpProxyResponse get(String url, Map<String, String> params) throws BlankUrlException, IOException, URISyntaxException {
        return get(url, params, null);
    }

    public static HttpProxyResponse get(String url) throws BlankUrlException, IOException, URISyntaxException {
        return get(url, null);
    }

    public static HttpProxyResponse postMultiPart(String url,
                                                  List<FormMultipartEntity> params,
                                                  List<BasicNameValuePair> headers,
                                                  List<BasicClientCookie> cookies) throws BlankUrlException, IOException {
        if(StringUtils.isBlank(url)) {
            throw new BlankUrlException(URL_IS_BLANK);
        }
        log.info(url);
        HttpProxyResponse proxyResponse = new HttpProxyResponse();
        proxyResponse.setUrl(url);

        try (CloseableHttpClient httpClient = HttpClientFactory.createHttpClient(cookies)) {
            CloseableHttpResponse response;
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            StringBuilder requestContent = new StringBuilder();
            params.forEach(param -> {
                requestContent.append(param.toString()).append("\n");
                if (param.getType().equalsIgnoreCase("text")) {
                    builder.addTextBody(param.getName(), param.getValue());
                } else if (param.getType().equalsIgnoreCase("file")) {
                    builder.addBinaryBody(param.getName(), new File(param.getValue()),
                            ContentType.APPLICATION_OCTET_STREAM, param.getValue());
                }
            });
            log.info("request params: {}", requestContent);
            HttpEntity entity = builder.build();

            httpPost.setEntity(entity);

            if (headers != null && !headers.isEmpty()) {
                headers.forEach(row -> httpPost.setHeader(row.getName(), row.getValue()));
            }

            response = httpClient.execute(httpPost);
            log.info("状态码: {}\t\t{}", response.getStatusLine().getStatusCode(),
                    response.getStatusLine().getReasonPhrase());

            proxyResponse.setRequestAllHeaders(httpPost.getAllHeaders());
            packingHttpProxyResponse(proxyResponse, response);

        }

        return proxyResponse;
    }

    public static HttpProxyResponse postMultiPart(String url, List<FormMultipartEntity> params, List<BasicNameValuePair> headers) throws BlankUrlException, IOException {
        return postMultiPart(url, params, headers, null);
    }

    public static HttpProxyResponse postMultiPart(String url, List<FormMultipartEntity> params) throws BlankUrlException, IOException {
        return postMultiPart(url, params, null, null);
    }

    public static HttpProxyResponse postTextPlain(String url, Object data) throws BlankUrlException, IOException, URISyntaxException {
        List<BasicNameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair(CONTENT_TYPE, "text/plain"));
        return post(url, data, null, headers);
    }
}
