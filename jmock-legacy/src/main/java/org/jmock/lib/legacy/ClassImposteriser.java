package org.jmock.lib.legacy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.jmock.api.Imposteriser;
import org.jmock.api.Invocation;
import org.jmock.api.Invokable;
import org.jmock.internal.SearchingClassLoader;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * This class lets you imposterise abstract and concrete classes
 * <em>without</em> calling the constructors of the mocked class.
 * 
 * @author npryce
 */
public class ClassImposteriser implements Imposteriser {
    public static final Imposteriser INSTANCE = new ClassImposteriser();

    private ClassImposteriser() {
    }

    private static final Method FINALIZE_METHOD = findFinalizeMethod();

    private static final NamingStrategy.AbstractBase NAMING_POLICY_THAT_ALLOWS_IMPOSTERISATION_OF_CLASSES_IN_SIGNED_PACKAGES = new NamingStrategy.AbstractBase() {
        @Override
        protected String name(TypeDescription superClass) {
            return "org.jmock.codegen." + superClass.getActualName();
        }
    };

    /*
     * private static final CallbackFilter IGNORED_METHODS = new CallbackFilter() {
     * public int accept(Method method) { if (method.isBridge()) return 1; else if
     * (method.equals(FINALIZE_METHOD)) return 1; else return 0; } };
     */
    private final Objenesis objenesis = new ObjenesisStd();

    public boolean canImposterise(Class<?> type) {
        return !type.isPrimitive() &&
                !Modifier.isFinal(type.getModifiers()) &&
                (type.isInterface() || !toStringMethodIsFinal(type));
    }

    public <T> T imposterise(final Invokable mockObject, Class<T> mockedType, Class<?>... ancilliaryTypes) {
        if (!mockedType.isInterface() && toStringMethodIsFinal(mockedType)) {
            throw new IllegalArgumentException(mockedType.getName() + " has a final toString method");
        }

        try {
            setConstructorsAccessible(mockedType, true);
            return mockedType.cast(proxy(mockObject, mockedType, ancilliaryTypes));
        } finally {
            setConstructorsAccessible(mockedType, false);
        }
    }

    private boolean toStringMethodIsFinal(Class<?> type) {
        try {
            Method toString = type.getMethod("toString");
            return Modifier.isFinal(toString.getModifiers());

        } catch (SecurityException e) {
            throw new IllegalStateException("not allowed to reflect on toString method", e);
        } catch (NoSuchMethodException e) {
            throw new Error("no public toString method found", e);
        }
    }

    private void setConstructorsAccessible(Class<?> mockedType, boolean accessible) {
        for (Constructor<?> constructor : mockedType.getDeclaredConstructors()) {
            constructor.setAccessible(accessible);
        }
    }

    private Object proxy(final Invokable mockObject, Class<?> mockedType, Class<?>... ancilliaryTypes) {

        Builder<?> builder = new ByteBuddy().subclass(mockedType)
                .implement(ancilliaryTypes)
                .method(ElementMatchers.any())
                .intercept(InvocationHandlerAdapter.of(new InvocationHandler() {
                    public Object invoke(Object receiver, Method method, Object[] args) throws Throwable {
                        return mockObject.invoke(new Invocation(receiver, method, args));
                    }
                }));

        // enhancer.setCallbackFilter(IGNORED_METHODS);
        // if (mockedType.getSigners() != null) {
        // enhancer.setNamingPolicy(NAMING_POLICY_THAT_ALLOWS_IMPOSTERISATION_OF_CLASSES_IN_SIGNED_PACKAGES);
        // }

        // From
        // https://mydailyjava.blogspot.com/2018/04/jdk-11-and-proxies-in-world-past.html
        try {
            ClassLoadingStrategy<ClassLoader> strategy;
            if (ClassInjector.UsingLookup.isAvailable()) {
                Class<?> methodHandles = Class.forName("java.lang.invoke.MethodHandles");
                Object lookup = methodHandles.getMethod("lookup").invoke(null);
                Method privateLookupIn = methodHandles.getMethod("privateLookupIn",
                        Class.class,
                        Class.forName("java.lang.invoke.MethodHandles$Lookup"));
                Object privateLookup = privateLookupIn.invoke(null, mockedType, lookup);
                strategy = ClassLoadingStrategy.UsingLookup.of(privateLookup);
            } else if (ClassInjector.UsingReflection.isAvailable()) {
                strategy = ClassLoadingStrategy.Default.INJECTION;
            } else {
                throw new IllegalStateException("No code generation strategy available");
            }

            return objenesis.newInstance(builder.make()
                    .load(SearchingClassLoader.combineLoadersOf(mockedType, ancilliaryTypes), strategy)
                    .getLoaded());
            
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | ClassNotFoundException e) {
            throw new RuntimeException("Exception in code generation strategy available", e);
        }
    }

    private static Method findFinalizeMethod() {
        try {
            return Object.class.getDeclaredMethod("finalize");
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Could not find finalize method on Object");
        }
    }

    public static class ClassWithSuperclassToWorkAroundCglibBug {
    }
}
