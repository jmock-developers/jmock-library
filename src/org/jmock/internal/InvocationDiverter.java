package org.jmock.internal;

import org.jmock.api.Invocation;
import org.jmock.api.Invokable;

public class InvocationDiverter<T> implements Invokable {
    private final Class<T> declaringType;
    private final T target;
    private final Invokable next;

    public InvocationDiverter(Class<T> declaringType, T target, Invokable next) {
        this.declaringType = declaringType;
        this.target = target;
        this.next = next;
    }
    
    @Override
    public String toString() {
        return next.toString();
    }
    
    public Object invoke(Invocation invocation) throws Throwable {
        if (invocation.getInvokedMethod().getDeclaringClass() == declaringType) {
            return invocation.applyTo(target);
        }
        else {
            return next.invoke(invocation);
        }
    }
}
