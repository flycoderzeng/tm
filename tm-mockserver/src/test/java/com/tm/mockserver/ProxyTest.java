package com.tm.mockserver;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTest {

    public static void main(String[] args) {
        final AI a1 = new A();
        AI a = (AI)Proxy.newProxyInstance(a1.getClass().getClassLoader(), a1.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("proxy code");
                final Object invoke = method.invoke(a1, args);
                return invoke;
            }
        });
        a.test();

        final CglibA cglibA = new CglibA();
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CglibA.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("cglib proxy code");
                final Object invoke = method.invoke(cglibA, objects);
                return invoke;
            }
        });
        final CglibA o = (CglibA) enhancer.create();
        o.test();
    }

    public interface AI {
        void test();
    }

    public static class A implements AI {
        public void test() {
            System.out.println("test ha ha ha");
        }
    }

    public static class CglibA {
        public void test() {
            System.out.println("cglib test ha ha ha");
        }
    }
}
