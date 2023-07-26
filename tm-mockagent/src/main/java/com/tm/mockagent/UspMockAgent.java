package com.tm.mockagent;

import com.sun.tools.attach.*;
import com.tm.mockagent.boot.MockAgentApplication;
import com.tm.mockagent.entities.model.MockAgentArgsInfo;
import com.tm.mockagent.utils.AgentUtils;
import javassist.*;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.util.List;


public class UspMockAgent {
    public static final String SPRING_REST_TEMPLATE_HTTP_MOCK_CLASS_PATH = "org.springframework.web.client.RestTemplate";

    private static final Logger logger = Logger.getLogger(UspMockAgent.class);
    public static final MockAgentApplication application = new MockAgentApplication();

    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor vmd : list) {
            if (vmd.displayName().indexOf("MockServerApplication") > -1) {
                VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                try {
                    virtualMachine.loadAgent("C:\\mockagent-1.0.0-jar-with-dependencies.jar", "applicationName=com.tm.mockserver.MockServerApplication+ip=127.0.0.1+port=8082+name=mock-server+description=mock-server+mockServerIp=127.0.0.1+mockServerPort=54321");
                } finally {
                    virtualMachine.detach();
                }
                break;
            }
        }
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException, ClassNotFoundException {
        runAgent(agentArgs);
        addTransformer(inst);
        retransformClasses(inst);
    }
    public static void premain(String agentArgs, Instrumentation inst) {
        // "applicationName=com.tm.mockserver.MockServerApplication+ip=127.0.0.1+port=8082+name=mock-server+description=mock-server+mockServerIp=127.0.0.1+mockServerPort=54321"
        runAgent(agentArgs);
        addTransformer(inst);
    }

    private static void runAgent(String agentArgs) {
        logger.info("agentArgs : " + agentArgs);
        final MockAgentArgsInfo agentInfo = AgentUtils.getAgentInfo(agentArgs);
        application.init(agentInfo);
        application.run();
    }

    private static void addTransformer(Instrumentation inst) {
        inst.addTransformer(new UspMockAgentDefineTransformer(), true);
    }

    public static void retransformClasses(Instrumentation inst) throws UnmodifiableClassException {
        Class<?>[] allClasses = inst.getAllLoadedClasses();
        for (Class<?> clz : allClasses) {
            final String name = clz.getName();
            String dotClassPath = name.replace("/", ".");
            if (SPRING_REST_TEMPLATE_HTTP_MOCK_CLASS_PATH.equals(dotClassPath)) {
                logger.info("agentmain retransformClasses " + dotClassPath);
                inst.retransformClasses(clz);
            }
        }
    }


    static class UspMockAgentDefineTransformer implements ClassFileTransformer {

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            String dotClassPath = className.replace("/", ".");
            return getBytes(dotClassPath, loader, classfileBuffer);
        }

        public static byte[] getBytes(String dotClassPath, ClassLoader loader, byte[] classfileBuffer) {
            if (SPRING_REST_TEMPLATE_HTTP_MOCK_CLASS_PATH.equals(dotClassPath)) {
                logger.info("premain load class " + dotClassPath);
                return mockSpringRestTemplateHttp(loader, classfileBuffer);
            }

            return classfileBuffer;
        }

        public static byte[] mockSpringRestTemplateHttp(ClassLoader loader, byte[] classfileBuffer) {
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
                        break;
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
