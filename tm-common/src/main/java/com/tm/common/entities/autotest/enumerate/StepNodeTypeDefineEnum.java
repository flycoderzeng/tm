package com.tm.common.entities.autotest.enumerate;

public enum StepNodeTypeDefineEnum {
    ROOT("root"), SETUP("setUp"), ACTION("action"), TEARDOWN("teardown"),
    HTTP("http"),
    HTTP_REQUEST("http request"),
    JDBC("jdbc"), JDBC_REQUEST("jdbc request"), SCRIPT_ACTION_NODE("shell script"),
    IF("if"), WHILE("while"), LOOP("loop"),
    __getRandomInt("__getRandomInt"),
    __jsonMultiExtractor("__jsonMultiExtractor"), __xmlMultiExtractor("__xmlMultiExtractor"),
    __getDate("__getDate"), __getTimestamp("__getTimestamp"), __sleep("__sleep"),
    __operationExpression("__operationExpression"),
    __getGlobalKeyValue("__getGlobalKeyValue"), __setGlobalKeyValue("__setGlobalKeyValue"),
    __assert("__assert"),
    __encodeURIComponent("__encodeURIComponent"), __decodeURIComponent("__decodeURIComponent"),
    __base64Encode("__base64Encode"), __base64Decode("__base64Decode"),
    __encodeCipherCredential("__encodeCipherCredential"),
    __subString("__subString"),
    __md5("__md5");
    String value;
    StepNodeTypeDefineEnum(String value) {
        this.value = value;
    }
    public String value() {return value;}
    public static StepNodeTypeDefineEnum get(String value) {
        switch (value) {
            case "root":
                return StepNodeTypeDefineEnum.ROOT;
            case "setUp":
                return StepNodeTypeDefineEnum.SETUP;
            case "action":
                return StepNodeTypeDefineEnum.ACTION;
            case "teardown":
                return StepNodeTypeDefineEnum.TEARDOWN;
            case "http":
                return StepNodeTypeDefineEnum.HTTP;
            case "http request":
                return StepNodeTypeDefineEnum.HTTP_REQUEST;
            case "jdbc":
                return StepNodeTypeDefineEnum.JDBC;
            case "jdbc request":
                return StepNodeTypeDefineEnum.JDBC_REQUEST;
            case "shell script":
                return StepNodeTypeDefineEnum.SCRIPT_ACTION_NODE;
            case "if":
                return StepNodeTypeDefineEnum.IF;
            case "while":
                return StepNodeTypeDefineEnum.WHILE;
            case "loop":
                return StepNodeTypeDefineEnum.LOOP;
            case "__getRandomInt":
                return StepNodeTypeDefineEnum.__getRandomInt;
            case "__jsonMultiExtractor":
                return StepNodeTypeDefineEnum.__jsonMultiExtractor;
            case "__xmlMultiExtractor":
                return StepNodeTypeDefineEnum.__xmlMultiExtractor;
            case "__getDate":
                return StepNodeTypeDefineEnum.__getDate;
            case "__getTimestamp":
                return StepNodeTypeDefineEnum.__getTimestamp;
            case "__sleep":
                return StepNodeTypeDefineEnum.__sleep;
            case "__operationExpression":
                return StepNodeTypeDefineEnum.__operationExpression;
            case "__getGlobalKeyValue":
                return StepNodeTypeDefineEnum.__getGlobalKeyValue;
            case "__setGlobalKeyValue":
                return StepNodeTypeDefineEnum.__setGlobalKeyValue;
            case "__encodeURIComponent":
                return StepNodeTypeDefineEnum.__encodeURIComponent;
            case "__decodeURIComponent":
                return StepNodeTypeDefineEnum.__decodeURIComponent;
            case "__base64Encode":
                return StepNodeTypeDefineEnum.__base64Encode;
            case "__base64Decode":
                return StepNodeTypeDefineEnum.__base64Decode;
            case "__encodeCipherCredential":
                return StepNodeTypeDefineEnum.__encodeCipherCredential;
            case "__subString":
                return StepNodeTypeDefineEnum.__subString;
            case "__md5":
                return StepNodeTypeDefineEnum.__md5;
            case "__assert":
                return StepNodeTypeDefineEnum.__assert;
            default:
                return null;
        }
    }
}
