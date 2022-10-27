package com.tm.mockagent.entities.enumerate;

public enum HttpMethod {
    GET(1, "GET"),
    POST(2, "POST");
    private Integer value;

    private String description;

    HttpMethod(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer val() {
        return value;
    }

    public String desc() {
        return description;
    }

    public static HttpMethod get(String value) {
        switch (value) {
            case "GET":
                return HttpMethod.GET;
            case "POST":
                return HttpMethod.POST;
            default:
                return null;
        }
    }
}
