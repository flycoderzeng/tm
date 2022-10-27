package com.tm.mockserver;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.aop.*;
import org.springframework.aop.aspectj.AspectJPointcutAdvisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.lang.reflect.Method;

@EnableAspectJAutoProxy
@SpringBootTest
class MockServerApplicationTests {

    public static class P {
        @Pointcut(value = "within(com.tm.mockserver.MockServerApplicationTests.A.test)")
        public void testDemo() {
            System.out.println("test demo");
        }
    }

    public static class A {
        @Before("com.tm.mockserver.MockServerApplicationTests.P.testDemo()")
        public void test() {
            System.out.println("test");
        }
    }

    @Test
    void contextLoads() {

    }

}
