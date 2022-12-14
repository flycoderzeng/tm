package com.tm.common.entities.common.enumerate;

public enum DbTypeEnum {
    MYSQL(1);
    int value;
    DbTypeEnum(int value) {this.value = value;}
    DbTypeEnum() {}
    public int value() {return value;}
    public static DbTypeEnum get(int value) {
        switch (value) {
            case 1:
                return DbTypeEnum.MYSQL;
            default:
                return null;
        }
    }
}
