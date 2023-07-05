package com.tm.common.entities.autotest.enumerate;

public enum StepNodeTypeDefineEnum {
    ROOT("root"), SETUP("setUp"), ACTION("action"), TEARDOWN("teardown"),
    HTTP("http"),
    HTTP_REQUEST("http request"),
    JDBC("jdbc"), JDBC_REQUEST("jdbc request"), SCRIPT_ACTION_NODE("shell script"),
    IF("if"), WHILE("while"), LOOP("loop"),
    GET_RANDOM_INT("__getRandomInt"),
    JSON_MULTI_EXTRACTOR("__jsonMultiExtractor"), XML_MULTI_EXTRACTOR("__xmlMultiExtractor"),
    GET_DATE("__getDate"), GET_TIMESTAMP("__getTimestamp"), SLEEP("__sleep"),
    OPERATION_EXPRESSION("__operationExpression"),
    GET_GLOBAL_KEY_VALUE("__getGlobalKeyValue"), SET_GLOBAL_KEY_VALUE("__setGlobalKeyValue"),
    ASSERT("__assert"),
    ENCODE_URI_COMPONENT("__encodeURIComponent"), DECODE_URI_COMPONENT("__decodeURIComponent"),
    BASE_64_ENCODE("__base64Encode"), BASE_64_DECODE("__base64Decode"),
    ENCODE_CIPHER_CREDENTIAL("__encodeCipherCredential"), ENCODE_MESSAGES_DIGEST("__encodeMessagesDigest"),
    SUB_STRING("__subString"),
    MD_5("__md5");
    String value;

    StepNodeTypeDefineEnum(String value) {
        this.value = value;
    }

    public static StepNodeTypeDefineEnum get(String value) {
        return switch (value) {
            case "root" -> ROOT;
            case "setUp" -> SETUP;
            case "action" -> ACTION;
            case "teardown" -> TEARDOWN;
            case "http" -> HTTP;
            case "http request" -> HTTP_REQUEST;
            case "jdbc" -> JDBC;
            case "jdbc request" -> JDBC_REQUEST;
            case "shell script" -> SCRIPT_ACTION_NODE;
            case "if" -> IF;
            case "while" -> WHILE;
            case "loop" -> LOOP;
            case "__getRandomInt" -> GET_RANDOM_INT;
            case "__jsonMultiExtractor" -> JSON_MULTI_EXTRACTOR;
            case "__xmlMultiExtractor" -> XML_MULTI_EXTRACTOR;
            case "__getDate" -> GET_DATE;
            case "__getTimestamp" -> GET_TIMESTAMP;
            case "__sleep" -> SLEEP;
            case "__operationExpression" -> OPERATION_EXPRESSION;
            case "__getGlobalKeyValue" -> GET_GLOBAL_KEY_VALUE;
            case "__setGlobalKeyValue" -> SET_GLOBAL_KEY_VALUE;
            case "__encodeURIComponent" -> ENCODE_URI_COMPONENT;
            case "__decodeURIComponent" -> DECODE_URI_COMPONENT;
            case "__base64Encode" -> BASE_64_ENCODE;
            case "__base64Decode" -> BASE_64_DECODE;
            case "__encodeCipherCredential" -> ENCODE_CIPHER_CREDENTIAL;
            case "__encodeMessagesDigest" -> ENCODE_MESSAGES_DIGEST;
            case "__subString" -> SUB_STRING;
            case "__md5" -> MD_5;
            case "__assert" -> ASSERT;
            default -> null;
        };
    }

    public String value() {
        return value;
    }
}
