package org.jmock.internal;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.jmock.api.Invocation;
import org.jmock.api.Invokable;


public abstract class FakeObjectMethods implements Invokable {
    private final Invokable next;

    public FakeObjectMethods(Invokable next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return next.toString();
    }

    public Object invoke(Invocation invocation) throws Throwable {
        Method method = invocation.getInvokedMethod();
        if (isMethod(method, int.class, "hashCode")) {
            return fakeHashCode(invocation.getInvokedObject());
        }
        else if (isMethod(method, String.class, "toString")) {
            return fakeToString(invocation.getInvokedObject());
        }
        else if (isMethod(method, boolean.class, "equals", Object.class)) {
            return fakeEquals(invocation.getInvokedObject(), invocation.getParameter(0));
        }
        else if (isMethod(method, void.class, "finalize")) {
            fakeFinalize(invocation.getInvokedObject());
            return null;
        }
        else {
            return next.invoke(invocation);
        }
    }

    protected abstract int fakeHashCode(Object invokedObject);

    protected abstract String fakeToString(Object invokedObject);

    protected abstract boolean fakeEquals(Object invokedObject, Object other);

    protected abstract void fakeFinalize(Object invokedObject);

    private boolean isMethod(Method method, Class<?> returnType, String name, Class<?>... parameterTypes) {
        return method.getReturnType().equals(returnType)
            && method.getName().equals(name)
            && Arrays.equals(method.getParameterTypes(), parameterTypes);
    }

}
