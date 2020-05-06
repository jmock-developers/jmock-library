package org.jmock.imposters;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import net.bytebuddy.dynamic.scaffold.TypeValidation;
import org.jmock.api.Imposteriser;
import org.jmock.api.Invocation;
import org.jmock.api.Invokable;
import org.jmock.internal.SearchingClassLoader;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.FieldValue;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * This class lets you imposterise abstract and concrete classes
 * <em>without</em> calling the constructors of the mocked class.
 * 
 * @author olibye
 */
public class ByteBuddyClassImposteriser implements Imposteriser {

    public static final Imposteriser INSTANCE = new ByteBuddyClassImposteriser();
    private static final String JMOCK_KEY = "jMock";

    private final Objenesis objenesis = new ObjenesisStd();
    private final Map<Set<Class<?>>, Class<?>> types = new ConcurrentHashMap<Set<Class<?>>, Class<?>>();

    private ByteBuddyClassImposteriser() {
    }

    public boolean canImposterise(Class<?> type) {
        return !type.isPrimitive() &&
                !Modifier.isFinal(type.getModifiers()) &&
                (type.isInterface() || !toStringMethodIsFinal(type));
    }

    @Override
    public <T> T imposterise(final Invokable mockObject, Class<T> mockedType, Class<?>... ancilliaryTypes) {
        if (!mockedType.isInterface() && toStringMethodIsFinal(mockedType)) {
            throw new IllegalArgumentException(mockedType.getName() + " has a final toString method");
        }

        try {
            setConstructorsAccessible(mockedType, true);
            return proxy(mockObject, mockedType, ancilliaryTypes);
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

    private <T> T proxy(
            final Invokable mockObject, final Class<T> mockedType, final Class<?>... ancilliaryTypes) {

        try {
            Set<Class<?>> mockTypeKey = mockTypeKey(mockedType, ancilliaryTypes);
            Class<?> proxyType = types.computeIfAbsent(mockTypeKey,
                    new Function<Object, Class<?>>() {
                        @Override
                        public Class<?> apply(Object t) {
                            try {
                                return proxyClass(mockObject, mockedType, ancilliaryTypes);
                            } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException
                                    | NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

            InjectInvokable invokable = (InjectInvokable) objenesis.newInstance(proxyType);
            invokable.setJMock(mockObject);
            return (T) invokable;

        } catch (IllegalArgumentException | SecurityException e) {
            throw new RuntimeException("Exception in code generation strategy available", e);
        }
    }

    public interface InjectInvokable {
        void setJMock(Invokable invokable);
        Invokable getJMock();
    }
    
    public static class Interceptor {
        @RuntimeType
        static public Object intercept(
                @This Object receiver,
                @Origin Method method,
                @FieldValue(value=JMOCK_KEY) Invokable invokable,
                @AllArguments Object[] args
                ) throws Throwable {
            return invokable.invoke(new Invocation(receiver, method, args));
        }
    }

    private Class<?> proxyClass(final Invokable mockObject, final Class<?> mockedType, Class<?>... ancilliaryTypes)
            throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Builder<?> builder = new ByteBuddy()
                .with(TypeValidation.DISABLED)
                .with(new NamingStrategy.SuffixingRandom(JMOCK_KEY, JMOCK_KEY.toLowerCase()))
                .subclass(mockedType)
                .implement(ancilliaryTypes)
                .defineField(JMOCK_KEY, Invokable.class, Visibility.PRIVATE)
                .implement(InjectInvokable.class).intercept(FieldAccessor.ofField(JMOCK_KEY))
                .method(ElementMatchers.not(ElementMatchers.isDeclaredBy(InjectInvokable.class)))
                .intercept(MethodDelegation.to(Interceptor.class));
/*                
                .intercept(InvocationHandlerAdapter.of(new InvocationHandler() {
                    public Object invoke(Object receiver, Method method, Object[] args) throws Throwable {
                        return mockObject.invoke(new Invocation(receiver, method, args));
                    }
                }));
*/
        // From
        // https://mydailyjava.blogspot.com/2018/04/jdk-11-and-proxies-in-world-past.html
        ClassLoadingStrategy<ClassLoader> strategy;
        if (ClassInjector.UsingLookup.isAvailable() && !protectedPackageNameSpaces(mockedType)
                && !defaultPackage(mockedType)
                && mockedType.getClassLoader() == this.getClass().getClassLoader()) {
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

        Class<?> proxyType = builder.make()
                .load(SearchingClassLoader.combineLoadersOf(mockedType, ancilliaryTypes), strategy)
                .getLoaded();
        return proxyType;
    }

    private Set<Class<?>> mockTypeKey(final Class<?> mockedType, Class<?>... ancilliaryTypes) {
        Set<Class<?>> types = new HashSet<>();
        types.add(mockedType);
        for (Class<?> class1 : ancilliaryTypes) {
            types.add(class1);
        }
        return types;
    }

    private boolean defaultPackage(Class<?> mockedType) {
        return mockedType.getPackage().getName().isEmpty();
    }

    private boolean protectedPackageNameSpaces(Class<?> mockedType) {
        return mockedType.getName().startsWith("java.");
    }
}
