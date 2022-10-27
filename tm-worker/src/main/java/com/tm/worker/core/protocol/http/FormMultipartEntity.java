package com.tm.worker.core.protocol.http;


import lombok.Data;

@Data
public class FormMultipartEntity {
    private String name;
    // text or file
    private String type;
    private String value;

    public FormMultipartEntity(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }
}
