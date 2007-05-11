package org.jmock.lib.legacy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.NoOp;

import org.jmock.api.Imposteriser;
import org.jmock.api.Invocation;
import org.jmock.api.Invokable;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

/**
 * This class lets you imposterise abstract and concrete classes 
 * <em>without</em> calling the constructors of the mocked class.
 *   
 * @author npryce
 */
public class ClassImposteriser implements Imposteriser {
    public static final Imposteriser INSTANCE = new ClassImposteriser();
    
    private ClassImposteriser() {}
    
    private static final NamingPolicy NAMING_POLICY_THAT_ALLOWS_IMPOSTERISATION_OF_CLASSES_IN_SIGNED_PACKAGES = new DefaultNamingPolicy() {
        @Override
        public String getClassName(String prefix, String source, Object key, Predicate names) {
            return "org.jmock.codegen." + super.getClassName(prefix, source, key, names);
        }
    };
    
    private static final CallbackFilter IGNORE_BRIDGE_METHODS = new CallbackFilter() {
        public int accept(Method method) {
            return method.isBridge() ? 1 : 0;
        }
    };
    
    private final Objenesis objenesis = new ObjenesisStd();
    
    public boolean canImposterise(Class<?> type) {
        return !type.isPrimitive() && !Modifier.isFinal(type.getModifiers());
    }
    
    public <T> T imposterise(final Invokable mockObject, Class<T> mockedType, Class<?>... ancilliaryTypes) {
        Class<?> proxyClass = createProxyClass(mockedType, ancilliaryTypes);
        return mockedType.cast(createProxy(proxyClass, mockObject));
	}
    
    private <T> Class<?> createProxyClass(Class<T> mockedType, Class<?>... ancilliaryTypes) {
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(mockedType.getClassLoader());
        enhancer.setUseFactory(true);
        if (mockedType.isInterface()) {
            enhancer.setSuperclass(Object.class);
            enhancer.setInterfaces(prepend(mockedType, ancilliaryTypes));
        }
        else {
            enhancer.setSuperclass(mockedType);
            enhancer.setInterfaces(ancilliaryTypes);
        }
        enhancer.setCallbackTypes(new Class[]{InvocationHandler.class, NoOp.class});
        enhancer.setCallbackFilter(IGNORE_BRIDGE_METHODS);
        if (mockedType.getSigners() != null) {
            enhancer.setNamingPolicy(NAMING_POLICY_THAT_ALLOWS_IMPOSTERISATION_OF_CLASSES_IN_SIGNED_PACKAGES);
        }
        
        Class<?> proxyClass = enhancer.createClass();
        return proxyClass;
    }
    
    private Object createProxy(Class<?> proxyClass, final Invokable mockObject) {
        Factory proxy = (Factory)objenesis.newInstance(proxyClass);
        proxy.setCallbacks(new Callback[] {
            new InvocationHandler() {
                public Object invoke(Object receiver, Method method, Object[] args) throws Throwable {
                    return mockObject.invoke(new Invocation(receiver, method, args));
                }
            },
            NoOp.INSTANCE
        });
        return proxy;
    }
    
    private Class<?>[] prepend(Class<?> first, Class<?>... rest) {
        Class<?>[] all = new Class<?>[rest.length+1];
        all[0] = first;
        System.arraycopy(rest, 0, all, 1, rest.length);
        return all;
    }
}
