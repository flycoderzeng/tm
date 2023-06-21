package com.tm.common.entities.common;

import lombok.Data;

@Data
public class KeyValueRow extends BaseNameValue {
    // text file
    private String type;
    // 1-响应体 2-cookies 3-响应头
    private String extractorType;
    private String description;
    private String domain;
    private String path;
    private String relationOperator;
    // error warning
    private String assertLevel;

    private String rowNumber;
}
