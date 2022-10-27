package com.tm.common.base.model;


import lombok.Data;

@Data
public class HttpApi extends CommonAddModifyInfoModel {
    private Integer id;
    private Integer type = 1;
    private Integer method = 1;
    private String url = "";
    private Integer reqBodyType = 1;
    private String reqBodyForm;
    private String reqHeaders;
    private String reqParams;
    private String reqBodyKv;
    private String reqBodyOther;
    private String remark;
    private Integer resBodyType = 1;
    private String resBody;
    private String reqBodyMessage;
    private String resBodyMessage;
    private Integer status;
}
