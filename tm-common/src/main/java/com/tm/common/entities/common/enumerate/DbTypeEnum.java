package com.tm.common.entities.common.enumerate;

public enum DbTypeEnum {
    MYSQL(1), POSTGRESQL(2), DM(3);
    int value;
    DbTypeEnum(int value) {this.value = value;}
    DbTypeEnum() {}
    public int value() {return value;}
    public static DbTypeEnum get(int value) {
        if (value == 1) {
            return DbTypeEnum.MYSQL;
        }
        if (value == 2) {
            return DbTypeEnum.POSTGRESQL;
        }
        if (value == 3) {
            return DbTypeEnum.DM;
        }
        return null;
    }
}
