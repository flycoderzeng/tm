package com.tm.mockagent;

import com.tm.mockagent.boot.MockAgentApplication;
import com.tm.mockagent.entities.model.MockAgentArgsInfo;
import com.tm.mockagent.utils.AgentUtils;
import javassist.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;


public class UspMockAgent {
    public static final String SPRING_REST_TEMPLATE_HTTP_MOCK_CLASS_PATH = "org.springframework.web.client.RestTemplate";

    private static final Logger logger = LoggerFactory.getLogger(UspMockAgent.class);
    public static final MockAgentApplication application = new MockAgentApplication();

    public static void main(String[] args) {
        final MockAgentArgsInfo agentInfo = AgentUtils.getAgentInfo("ip=127.0.0.1+port=8082+name=mock-server+description=mock-server+mockServerIp=127.0.0.1+mockServerPort=54321");
        application.init(agentInfo);
        application.run();
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        logger.info("agentArgs : {}", agentArgs);
        final MockAgentArgsInfo agentInfo = AgentUtils.getAgentInfo(agentArgs);
        application.init(agentInfo);
        application.run();
        logger.info("add transformer");
        inst.addTransformer(new UspMockAgentDefineTransformer(), true);
    }

    static class UspMockAgentDefineTransformer implements ClassFileTransformer {

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            String dotClassPath = className.replace("/", ".");
            switch (dotClassPath) {
                case SPRING_REST_TEMPLATE_HTTP_MOCK_CLASS_PATH:
                    logger.info("premain load class {}", className);
                    return mockSpringRestTemplateHttp(loader, classfileBuffer);
                default:
                    break;
            }

            return classfileBuffer;
        }

        private byte[] mockSpringRestTemplateHttp(ClassLoader loader, byte[] classfileBuffer) {
            ClassPool classPool = ClassPool.getDefault();
            CtClass cc = null;
            classPool.appendClassPath(new LoaderClassPath(loader));

            try {
                cc = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                String mockMethodName = "execute";
                CtMethod[] ctMethods = cc.getMethods();
                logger.info("重新定义execute方法");
                for (CtMethod ctMethod : ctMethods) {
                    if(ctMethod.getName().equals(mockMethodName)) {
                        ctMethod.insertBefore("{$1=com.tm.mockagent.UspMockAgent.application.getMockTargetUrl($1, $2+\"\");}");
                    }
                }
                return cc.toBytecode();
            } catch (CannotCompileException|IOException e) {
                logger.error("transform error, ", e);
            }

            return classfileBuffer;
        }
    }
}
