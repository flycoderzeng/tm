package com.tm.common.entities.autotest;

import lombok.Data;

@Data
public class ParameterDefineRow {
    private String name;
    private String description;
    private String inout;
    private String defaultValue;
    private String type;
}
