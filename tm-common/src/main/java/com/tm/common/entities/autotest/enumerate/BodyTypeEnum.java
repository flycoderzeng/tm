package com.tm.common.entities.autotest.enumerate;

public enum BodyTypeEnum {
    NONE("none"), FORM_DATA("form-data"), X_WWW_FORM_URLENCODED("x-www-form-urlencoded"), RAW("raw");
    String value;
    BodyTypeEnum(String value) {this.value = value;}
    BodyTypeEnum() {}
    public String value() {return value;}
    public static BodyTypeEnum get(String value) {
        return switch (value) {
            case "none" -> BodyTypeEnum.NONE;
            case "form-data" -> BodyTypeEnum.FORM_DATA;
            case "x-www-form-urlencoded" -> BodyTypeEnum.X_WWW_FORM_URLENCODED;
            case "raw" -> BodyTypeEnum.RAW;
            default -> null;
        };
    }
}
