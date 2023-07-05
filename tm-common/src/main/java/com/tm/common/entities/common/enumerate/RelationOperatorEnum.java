package com.tm.common.entities.common.enumerate;

public enum RelationOperatorEnum {
    EQUAL("1", "等于"), NOT_EQUAL("2", "不等于"), LESS_THAN("3", "小于"),
    LESS_EQUAL("4", "小于或等于"), GREATER_THAN("5", "大于"), GREATER_EQUAL("6", "大于或等于"),
    INCLUDE("7", "包含"), NOT_INCLUDE("8", "不包含"), START_WITH("9", "开始以"),
    END_WITH("10", "结束以"), IS_NULL("11", "是null"), IS_NOT_NULL("12", "不是null"),
    IS_EMPTY("13", "是空的"), IS_NOT_EMPTY("14", "不是空的"), REGEX_PATTERN("15", "正则匹配"),
    PATH_NOT_EXISTS("16", "路径不存在"), IS_NOT_BLANK("17", "不是空白"), IS_BLANK("18", "是空白"),
    IS_NUMBER("19", "是数字");
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
        return switch (value) {
            case "1" -> EQUAL;
            case "2" -> NOT_EQUAL;
            case "3" -> LESS_THAN;
            case "4" -> LESS_EQUAL;
            case "5" -> GREATER_THAN;
            case "6" -> GREATER_EQUAL;
            case "7" -> INCLUDE;
            case "8" -> NOT_INCLUDE;
            case "9" -> START_WITH;
            case "10" -> END_WITH;
            case "11" -> IS_NULL;
            case "12" -> IS_NOT_NULL;
            case "13" -> IS_EMPTY;
            case "14" -> IS_NOT_EMPTY;
            case "15" -> REGEX_PATTERN;
            case "16" -> PATH_NOT_EXISTS;
            case "17" -> IS_NOT_BLANK;
            case "18" -> IS_BLANK;
            case "19" -> IS_NUMBER;
            default -> null;
        };
    }
}
