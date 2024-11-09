package com.tm.common.entities.autotest.enumerate;



// 1-秒；2-分；3-时；4-日；5-月；6-年
public enum DateUnitTypeNum {
    SECOND(1, "秒"), MINUTE(2, "分"),
    HOUR(3, "时"), DAY(4, "日"),
    MONTH(5, "月"), YEAR(6, "年");
    private final Integer value;

    private final String description;

    private DateUnitTypeNum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer val() {
        return value;
    }

    public String desc() {
        return description;
    }

    public static DateUnitTypeNum get(Integer value) {
        return switch (value) {
            case 1 -> DateUnitTypeNum.SECOND;
            case 2 -> DateUnitTypeNum.MINUTE;
            case 3 -> DateUnitTypeNum.HOUR;
            case 4 -> DateUnitTypeNum.DAY;
            case 5 -> DateUnitTypeNum.MONTH;
            case 6 -> DateUnitTypeNum.YEAR;
            default -> null;
        };
    }
}
