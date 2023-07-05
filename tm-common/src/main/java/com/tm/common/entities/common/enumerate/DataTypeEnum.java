package com.tm.common.entities.common.enumerate;

public enum DataTypeEnum {
    APP_API(2),AUTO_SHELL(3),GLOBAL_VARIABLE(4),PLATFORM_API(5),AUTO_CASE(6),AUTO_PLAN(7);
    int value;
    DataTypeEnum(int value) {this.value = value;}
    DataTypeEnum() {}
    public int value() {return value;}
    public static DataTypeEnum get(int value) {
        return switch (value) {
            case 2 -> DataTypeEnum.APP_API;
            case 3 -> DataTypeEnum.AUTO_SHELL;
            case 4 -> DataTypeEnum.GLOBAL_VARIABLE;
            case 5 -> DataTypeEnum.PLATFORM_API;
            case 6 -> DataTypeEnum.AUTO_CASE;
            case 7 -> DataTypeEnum.AUTO_PLAN;
            default -> null;
        };
    }
}
