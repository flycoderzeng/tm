package com.tm.common.entities.autotest.enumerate;

public enum ExtractorTypeEnum {
    RESPONSE_BODY("1", "响应体"), COOKIE("2", "cookie"), RESPONSE_HEADER("3", "响应头");
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
        switch (value) {
            case "1":
                return ExtractorTypeEnum.RESPONSE_BODY;
            case "2":
                return ExtractorTypeEnum.COOKIE;
            case "3":
                return ExtractorTypeEnum.RESPONSE_HEADER;
            default:
                return null;
        }
    }
}
