package com.tm.common.entities.autotest.enumerate;

public enum BodyTypeNum {
    NONE("none"), FORM_DATA("form-data"), X_WWW_FORM_URLENCODED("x-www-form-urlencoded"), RAW("raw");
    String value;
    BodyTypeNum(String value) {this.value = value;}
    BodyTypeNum() {}
    public String value() {return value;}
    public static BodyTypeNum get(String value) {
        switch (value) {
            case "none":
                return BodyTypeNum.NONE;
            case "form-data":
                return BodyTypeNum.FORM_DATA;
            case "x-www-form-urlencoded":
                return BodyTypeNum.X_WWW_FORM_URLENCODED;
            case "raw":
                return BodyTypeNum.RAW;
            default:
                return null;
        }
    }
}
