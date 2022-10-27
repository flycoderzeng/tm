package com.tm.common.entities.base;

import lombok.Data;

@Data
public class BaseResponse {
    private Integer code;
    private String message;
    private Object data;
}
