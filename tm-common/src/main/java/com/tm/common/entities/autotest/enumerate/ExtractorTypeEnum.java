package com.tm.common.entities.autotest.enumerate;

public enum ExtractorTypeEnum {
    RESPONSE_BODY("1", "响应体"), COOKIE("2", "cookie"),
    RESPONSE_HEADER("3", "响应头"), RESPONSE_STATUS("4", "http响应码");
    String value;
    String description;
    ExtractorTypeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String val() {
        return value;
    }

    public String desc() {
        return description;
    }

    public static ExtractorTypeEnum get(String value) {
        return switch (value) {
            case "1" -> ExtractorTypeEnum.RESPONSE_BODY;
            case "2" -> ExtractorTypeEnum.COOKIE;
            case "3" -> ExtractorTypeEnum.RESPONSE_HEADER;
            case "4" -> ExtractorTypeEnum.RESPONSE_STATUS;
            default -> null;
        };
    }
}
