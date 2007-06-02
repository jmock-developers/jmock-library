package org.jmock.internal;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.jmock.api.Invocation;
import org.jmock.api.Invokable;

public class ProxiedObjectIdentity implements Invokable {
    private Invokable next;
    
    public ProxiedObjectIdentity(Invokable next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return next.toString();
    }
    
    public Object invoke(Invocation invocation) throws Throwable {
        Method method = invocation.getInvokedMethod();
        if (isMethod(method, int.class, "hashCode")) {
            return System.identityHashCode(invocation.getInvokedObject());
        }
        else if (isMethod(method, String.class, "toString")) {
            return toString();
        }
        else if (isMethod(method, boolean.class, "equals", Object.class)) {
            return invocation.getParameter(0) == invocation.getInvokedObject();
        }
        else {
            return next.invoke(invocation);
        }
    }
    
    private boolean isMethod(Method method, Class<?> returnType, String name, Class<?>... parameterTypes) {
        return method.getReturnType().equals(returnType)
            && method.getName().equals(name)
            && Arrays.equals(method.getParameterTypes(), parameterTypes);
    }
}
