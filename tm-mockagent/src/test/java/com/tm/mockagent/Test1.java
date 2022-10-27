package com.tm.mockagent;

import javassist.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test1 {
    public static enum A {
        RED,BLUE
    }
    public static void main(String[] args) throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        final ClassPool pool = ClassPool.getDefault();
        final CtClass ctClass = pool.makeClass("com.tm.mockagent.Hello");
        final CtField name = new CtField(pool.get("java.lang.String"), "name", ctClass);
        name.setModifiers(Modifier.PRIVATE);
        ctClass.addField(name, CtField.Initializer.constant("xiaoming"));
        final CtMethod ctMethod = new CtMethod(CtClass.voidType, "test", new CtClass[]{}, ctClass);
        ctMethod.setModifiers(Modifier.PUBLIC);
        ctMethod.setBody("{System.out.println(name);}");
        ctMethod.setName("test1");

        ctClass.addMethod(ctMethod);
        final Class<?> aClass = ctClass.toClass();
        final Object o = aClass.getDeclaredConstructor().newInstance();
        final Method test = o.getClass().getMethod("test1");
        test.invoke(o);

        final Field name1 = aClass.getDeclaredField("name");
        name1.setAccessible(true);
        name1.set(o, "1111");
        name1.setAccessible(false);
        test.invoke(o);
        ctClass.detach();

        System.out.println(A.RED.name() instanceof String);

        System.out.println(A.RED.name().equals("RED"));

        System.out.println(A.RED + "");
    }
}
