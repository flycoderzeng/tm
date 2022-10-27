package com.tm.worker.core.variable;

import lombok.Data;

@Data
public class AutoCaseVariable {
    private String name;
    // string number boolean integer
    private String type = "string";
    private String value;
    private String key;

    public AutoCaseVariable() {

    }

    public AutoCaseVariable(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public AutoCaseVariable(String name, String value) {
        this.name = name;
        this.type = "string";
        this.value = value;
    }
}
