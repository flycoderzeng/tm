package com.tm.common.entities.common.enumerate;

public enum LeftOperandFromTypeEnum {
    REQUEST_URI(1, "请求路径"), REQUEST_BODY(2, "请求体"),
    REQUEST_HEADERS(3, "请求头"), COOKIES(4, "cookies"),
    RESPONSE_BODY(5, "响应体"), RESPONSE_HEADERS(6, "响应头");
    private Integer value;

    private String description;

    private LeftOperandFromTypeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer val() {
        return value;
    }

    public String desc() {
        return description;
    }

    public static LeftOperandFromTypeEnum get(int value) {
        switch (value) {
            case 1:
                return LeftOperandFromTypeEnum.REQUEST_URI;
            case 2:
                return LeftOperandFromTypeEnum.REQUEST_BODY;
            case 3:
                return LeftOperandFromTypeEnum.REQUEST_HEADERS;
            case 4:
                return LeftOperandFromTypeEnum.COOKIES;
            case 5:
                return LeftOperandFromTypeEnum.RESPONSE_BODY;
            case 6:
                return LeftOperandFromTypeEnum.RESPONSE_HEADERS;
            default:
                return null;
        }
    }
}
