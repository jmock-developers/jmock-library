package org.jmock.lib.nonstd;

import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

import org.jmock.api.Imposteriser;
import org.jmock.api.Invocation;
import org.jmock.api.Invokable;

import sun.misc.Unsafe;

/**
 * This class lets you imposterise concrete classes with CGLIB 
 * <em>without</em> calling the constructors of the mocked class.
 *   
 * However, it uses undocumented, internal features of Sun's JVM.
 * It will not work on JVMs from other vendors. Sun may remove
 * the undocumented feature this class relies upon in any future
 * versions.
 * 
 * <strong>You have been warned!</strong>
 * 
 * @author npryce
 */
public class UnsafeHackConcreteClassImposteriser implements Imposteriser {
    public static final Imposteriser INSTANCE = new UnsafeHackConcreteClassImposteriser();
    
    private UnsafeHackConcreteClassImposteriser() {}
    
    private static final NamingPolicy NAMING_POLICY_THAT_ALLOWS_IMPOSTERISATION_OF_CLASSES_IN_SIGNED_PACKAGES = new DefaultNamingPolicy() {
        @Override
        public String getClassName(String prefix, String source, Object key, Predicate names) {
            return "org.jmock.codegen." + super.getClassName(prefix, source, key, names);
        }
    };
    
    private static final Unsafe unsafe = obtainAnUnsafeObjectByADodgyReflectionHack();

    public boolean canImposterise(Class<?> type) {
        return !type.isPrimitive(); 
    }
    
    public <T> T imposterise(final Invokable mockObject, Class<T> mockedType, Class<?>... ancilliaryTypes) {
        Class<?> proxyClass = createProxyClass(mockedType, ancilliaryTypes);
        return mockedType.cast(createProxy(proxyClass, mockObject));
	}
    
    private <T> Class<?> createProxyClass(Class<T> mockedType, Class<?>... ancilliaryTypes) {
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(mockedType.getClassLoader());
        if (mockedType.isInterface()) {
            enhancer.setSuperclass(Object.class);
            enhancer.setInterfaces(prepend(mockedType, ancilliaryTypes));
        }
        else {
            enhancer.setSuperclass(mockedType);
            enhancer.setInterfaces(ancilliaryTypes);
        }
        enhancer.setCallbackType(InvocationHandler.class);
        enhancer.setNamingPolicy(NAMING_POLICY_THAT_ALLOWS_IMPOSTERISATION_OF_CLASSES_IN_SIGNED_PACKAGES);
        enhancer.setUseFactory(true);
        
        Class<?> proxyClass = enhancer.createClass();
        return proxyClass;
    }
	
    private Object createProxy(Class<?> proxyClass, final Invokable mockObject) {
        try {
            Object proxy = unsafe.allocateInstance(proxyClass);
            Field callbackField = proxyClass.getDeclaredField("CGLIB$CALLBACK_0");
            callbackField.setAccessible(true);
            callbackField.set(proxy, new InvocationHandler() {
                public Object invoke(Object receiver, Method method, Object[] args) throws Throwable {
                    return mockObject.invoke(new Invocation(receiver, method, args));
                }
            });
            return proxy;
        }
        catch (InstantiationException e) {
            throw new IllegalStateException("proxy class not instantiable", e);
        }
        catch (SecurityException e) {
            throw new IllegalStateException("cannot access private callback field", e);
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException("cannot access private callback field", e);
        }
        catch (NoSuchFieldException e) {
            throw new IllegalStateException("callback field does not exist", e);
        }
    }

    private Class<?>[] prepend(Class<?> first, Class<?>... rest) {
        Class<?>[] all = new Class<?>[rest.length+1];
        all[0] = first;
        System.arraycopy(rest, 0, all, 1, rest.length);
        return all;
    }
    
    static Unsafe obtainAnUnsafeObjectByADodgyReflectionHack() {
        try {
            Class<?> fieldReflectorClass = 
                Class.forName(ObjectStreamClass.class.getName() + "$FieldReflector");
            Field unsafeField = fieldReflectorClass.getDeclaredField("unsafe");
            unsafeField.setAccessible(true);
            
            return (Unsafe)unsafeField.get(null);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException("cannot load FieldReflector class", e);
        }
        catch (SecurityException e) {
            throw new IllegalStateException("cannot reflect on private field of FieldReflector", e);
        }
        catch (NoSuchFieldException e) {
            throw new IllegalStateException("cannot find 'unsafe' field of FieldReflector", e);
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException("field not accessible despite being made accessible", e);
        }
    }
}
