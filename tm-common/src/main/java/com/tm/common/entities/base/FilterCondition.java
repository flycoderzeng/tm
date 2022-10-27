package com.tm.common.entities.base;

import javax.validation.constraints.Pattern;

public class FilterCondition {
    public static final String LIKE_OPERATOR = "like";
    @Pattern(regexp = "[a-zA-Z_]{0,30}", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String columnName;
    @Pattern(regexp = "=|like|!=|>|<|>=|<=", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String operator = LIKE_OPERATOR;
    private String value;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
