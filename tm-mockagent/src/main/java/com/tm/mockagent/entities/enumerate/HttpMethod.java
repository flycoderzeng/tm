package com.tm.mockagent.entities.enumerate;

public enum HttpMethod {
    GET(1, "GET"),
    POST(2, "POST");
    private final Integer value;

    private final String description;

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
        return switch (value) {
            case "GET" -> HttpMethod.GET;
            case "POST" -> HttpMethod.POST;
            default -> null;
        };
    }
}
