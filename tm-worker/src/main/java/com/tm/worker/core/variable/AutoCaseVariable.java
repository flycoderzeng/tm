package com.tm.worker.core.variable;

import lombok.Data;

@Data
public class AutoCaseVariable {
    private String name;
    // string number boolean integer
    private String type = "string";
    private String value;
    private String planVariableName;
    private String key;

}
