package com.tm.common.entities.autotest.enumerate;

public enum BodyTypeNum {
    NONE("none"), FORM_DATA("form-data"), X_WWW_FORM_URLENCODED("x-www-form-urlencoded"), RAW("raw");
    String value;
    BodyTypeNum(String value) {this.value = value;}
    BodyTypeNum() {}
    public String value() {return value;}
    public static BodyTypeNum get(String value) {
        return switch (value) {
            case "none" -> BodyTypeNum.NONE;
            case "form-data" -> BodyTypeNum.FORM_DATA;
            case "x-www-form-urlencoded" -> BodyTypeNum.X_WWW_FORM_URLENCODED;
            case "raw" -> BodyTypeNum.RAW;
            default -> null;
        };
    }
}
