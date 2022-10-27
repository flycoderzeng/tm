package com.tm.worker.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public final class BeanUtils {
    private BeanUtils() {}
    public static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    public static <T> T mapToBean(Class<?> clazz, Map map) {
        return (T) gson.fromJson(gson.toJson(map), clazz);
    }
}
