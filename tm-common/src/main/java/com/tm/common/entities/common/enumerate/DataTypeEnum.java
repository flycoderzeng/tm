package com.tm.common.entities.common.enumerate;

public enum DataTypeEnum {
    APP_API(2),AUTO_SHELL(3),GLOBAL_VARIABLE(4),PLATFORM_API(5),AUTO_CASE(6),AUTO_PLAN(7);
    int value;
    DataTypeEnum(int value) {this.value = value;}
    DataTypeEnum() {}
    public int value() {return value;}
    public static DataTypeEnum get(int value) {
        switch (value) {
            case 2:
                return DataTypeEnum.APP_API;
            case 3:
                return DataTypeEnum.AUTO_SHELL;
            case 4:
                return DataTypeEnum.GLOBAL_VARIABLE;
            case 5:
                return DataTypeEnum.PLATFORM_API;
            case 6:
                return DataTypeEnum.AUTO_CASE;
            case 7:
                return DataTypeEnum.AUTO_PLAN;
            default:
                return null;
        }
    }
}
