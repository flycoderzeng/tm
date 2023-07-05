package com.tm.common.entities.autotest.enumerate;

public enum RawTypeNum {
    TEXT("text"), JSON("json"), XML("xml");
    String value;
    RawTypeNum(String value) {this.value = value;}
    RawTypeNum() {}
    public String value() {return value;}
    public static RawTypeNum get(String value) {
        return switch (value) {
            case "text" -> RawTypeNum.TEXT;
            case "json" -> RawTypeNum.JSON;
            case "xml" -> RawTypeNum.XML;
            default -> null;
        };
    }
}
