package com.tm.worker.core.protocol.http;


import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;

@Data
public class HttpProxyResponse {
    private String url;
    private Integer statusCode;
    private String responseBodyContent;
    private String reasonPhrase;
    private Header[] responseAllHeaders;
    private Header[] requestAllHeaders;
    private String requestBodyContent;


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("url: ").append(url).append("\n");
        builder.append("statusCode: ").append(statusCode).append("\n");
        builder.append("reasonPhrase: ").append(reasonPhrase).append("\n");
        builder.append("\n");
        builder.append("request Headers: \n");
        for (Header requestAllHeader : requestAllHeaders) {
            builder.append(requestAllHeader.getName()).append(": ").append(requestAllHeader.getValue()).append("\n");
        }
        builder.append("\n");
        builder.append("requestBodyContent: ");

        if(!StringUtils.isBlank(requestBodyContent)) {
            if(requestBodyContent.length() > 2000) {
                builder.append(requestBodyContent.substring(0, 2000)).append("\n");
            }else{
                builder.append(requestBodyContent).append("\n");
            }
        }
        builder.append("\n");
        builder.append("response Headers: \n");
        for (int i = 0; i < responseAllHeaders.length; i++) {
            builder.append(responseAllHeaders[i].getName()).append(": ").append(responseAllHeaders[i].getValue()).append("\n");
        }
        builder.append("\n");
        builder.append("responseBodyContent: ");
        if(!StringUtils.isBlank(responseBodyContent)) {
            if(responseBodyContent.length() > 2000) {
                builder.append(responseBodyContent.substring(0, 2000)).append("\n");
            }else{
                builder.append(responseBodyContent).append("\n");
            }
        }

        return builder.toString();
    }
}
