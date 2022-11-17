package com.tm.common.entities.autotest.enumerate;

public enum RawTypeNum {
    TEXT("text"), JSON("json"), XML("xml");
    String value;
    RawTypeNum(String value) {this.value = value;}
    RawTypeNum() {}
    public String value() {return value;}
    public static RawTypeNum get(String value) {
        switch (value) {
            case "text":
                return RawTypeNum.TEXT;
            case "json":
                return RawTypeNum.JSON;
            case "xml":
                return RawTypeNum.XML;
            default:
                return null;
        }
    }
}
