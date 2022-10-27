package com.tm.worker;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReflectTest {
    public static void main(String[] args) throws IllegalAccessException {
        A a = new A();
        // 只返回公有的
        //Field[] fields = A.class.getFields();

        // 返回所有
        Field[] fields = a.getClass().getDeclaredFields();
        for (Field field : fields) {
            //field.setAccessible(true);
            System.out.println(field.getType() + " " + field.getName());
            field.set(a, "1111");
            //field.setAccessible(false);
        }
        System.out.println(a.getStr());
        System.out.println(a.getString());
        System.out.println(a.getStrr());

        Method[] methods = a.getClass().getMethods();
        for (Method method : methods) {
            System.out.println(method);
        }

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        Object[] objects = list.stream().filter(s -> s.equals("1")).toArray();
        for (Object object : objects) {
            System.out.println(object);
        }

    }

    @Data
    public static class A {
        private String str;
        public String string;
        protected String strr;

        public void a(@NotNull String str) {
            Objects.requireNonNull(str);
        }

    }
}
