package com.tm.common.entities.common.enumerate;

public enum RelationOperatorEnum {
    EQUAL("1", "等于"), NOT_EQUAL("2", "不等于"), LESS_THAN("3", "小于"),
    LESS_EQUAL("4", "小于或等于"), MORE_THAN("5", "大于"), MORE_EQUAL("6", "大于或等于"),
    INCLUDE("7", "包含"), NOT_INCLUDE("8", "不包含"), START_WITH("9", "开始以"),
    END_WITH("10", "结束以"), IS_NULL("11", "是null"), IS_NOT_NULL("12", "不是null"),
    IS_EMPTY("13", "是空的"), IS_NOT_EMPTY("14", "不是空的"), REGEX_PATTERN("15", "正则匹配");
    String value;
    String description;
    RelationOperatorEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String val() {
        return value;
    }

    public String desc() {
        return description;
    }

    public static RelationOperatorEnum get(String value) {
        switch (value) {
            case "1":
                return EQUAL;
            case "2":
                return NOT_EQUAL;
            case "3":
                return LESS_THAN;
            case "4":
                return LESS_EQUAL;
            case "5":
                return MORE_THAN;
            case "6":
                return MORE_EQUAL;
            case "7":
                return INCLUDE;
            case "8":
                return NOT_INCLUDE;
            case "9":
                return START_WITH;
            case "10":
                return END_WITH;
            case "11":
                return IS_NULL;
            case "12":
                return IS_NOT_NULL;
            case "13":
                return IS_EMPTY;
            case "14":
                return IS_NOT_EMPTY;
            case "15":
                return REGEX_PATTERN;
            default:
                return null;
        }
    }
}
