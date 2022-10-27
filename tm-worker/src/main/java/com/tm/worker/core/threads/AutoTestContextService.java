package com.tm.worker.core.threads;



public class AutoTestContextService {
    private static final ThreadLocal<AutoTestContext> threadContext = ThreadLocal.withInitial(AutoTestContext::new);

    private AutoTestContextService() {}

    public static AutoTestContext getContext() {
        return threadContext.get();
    }

    public static void removeContext() {
        threadContext.remove();
    }

    public static void replaceContext(AutoTestContext context) {
        threadContext.remove();
        threadContext.set(context);
    }
}
