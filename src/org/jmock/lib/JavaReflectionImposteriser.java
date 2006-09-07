package org.jmock.lib;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.jmock.core.Imposteriser;
import org.jmock.core.Invocation;
import org.jmock.core.Invokable;

/**
 * An {@link org.jmock.core.Imposteriser} that uses the
 * {@link java.lang.reflect.Proxy} class of the Java Reflection API.
 * 
 * @author npryce
 *
 */
public class JavaReflectionImposteriser implements Imposteriser {
    public static final JavaReflectionImposteriser INSTANCE = new JavaReflectionImposteriser();
    
    @SuppressWarnings("unchecked")
    public <T> T imposterise(final Invokable mockObject, Class<T> mockedType, Class<?>... ancilliaryTypes) {
        Class[] proxiedClasses = new Class[ancilliaryTypes.length+1];
        proxiedClasses[0] = mockedType;
        System.arraycopy(ancilliaryTypes, 0, proxiedClasses, 1, ancilliaryTypes.length);
        
        return (T)Proxy.newProxyInstance(mockedType.getClassLoader(), proxiedClasses, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return mockObject.invoke(new Invocation(proxy, method, args));
            }
        });
    }
}
