package org.jmock.imposters;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Random;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;
import org.jmock.api.Imposteriser;
import org.jmock.api.Invocation;
import org.jmock.api.Invocation.ExpectationMode;
import org.jmock.api.Invokable;
import org.jmock.internal.CaptureControl;
import org.jmock.internal.SearchingClassLoader;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

/**
 * This class lets you imposterise abstract and concrete classes
 * <em>without</em> calling the constructors of the mocked class.
 * 
 * @author olibye
 */
public class ByteBuddyClassImposteriser implements Imposteriser {

    private static final String MOCK_TYPE_SUFFIX = "JMock";
    public static final Imposteriser INSTANCE = new ByteBuddyClassImposteriser();

    private final Random random = new Random();
    private final Objenesis objenesis = new ObjenesisStd();

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

    public static class CaptureControlInterceptor {
        private Invokable mockObject;

        public CaptureControlInterceptor(Invokable mockObject) {
            this.mockObject = mockObject;
        }

        @RuntimeType
        public Object interceptNoThrow(
                @This Object receiver,
                @Origin Method method,
                @AllArguments Object[] args,
                @Origin MethodHandle methodHandle
                ) throws Throwable {
            Object reply = mockObject.invoke(new Invocation(ExpectationMode.LEGACY, receiver, method, args));
            return reply;
        }

    }
    
    private <T> T proxy(
            final Invokable mockObject, final Class<T> mockedType, Class<?>... ancilliaryTypes) {

        Builder<?> builder = new ByteBuddy()
                .with(namingStrategy(mockedType))
                .subclass(mockedType)
                .implement(ancilliaryTypes)
                .method(ElementMatchers.isDeclaredBy(CaptureControl.class))
                .intercept(MethodDelegation.withDefaultConfiguration().to(new CaptureControlInterceptor(mockObject)))
                .method(ElementMatchers.not(ElementMatchers.isDeclaredBy(CaptureControl.class)))
                .intercept(MethodDelegation.withDefaultConfiguration().to(new CaptureControlInterceptor(mockObject)))
                ;

        // From
        // https://mydailyjava.blogspot.com/2018/04/jdk-11-and-proxies-in-world-past.html
        try {
            ClassLoadingStrategy<ClassLoader> strategy;
            if (ClassInjector.UsingLookup.isAvailable() && !protectedPackageNameSpaces(mockedType)
                    && !defaultPackage(mockedType)) {
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

            return (T) objenesis.newInstance(builder.make()
                    .load(SearchingClassLoader.combineLoadersOf(mockedType, ancilliaryTypes), strategy)
                    .getLoaded());

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | ClassNotFoundException e) {
            throw new RuntimeException("Exception in code generation strategy available", e);
        }
    }

    private boolean defaultPackage(Class<?> mockedType) {
        return mockedType.getPackage().getName().isEmpty();
    }

    private NamingStrategy namingStrategy(Class<?> mockedType) {
        if (protectedPackageNameSpaces(mockedType)) {
            return new NamingStrategy.SuffixingRandom(MOCK_TYPE_SUFFIX);
        }
        return new NamingStrategy.AbstractBase() {
            @Override
            protected String name(TypeDescription superClass) {
                String possiblePackageName = superClass.getPackage().getName();
                String validPackageName = possiblePackageName.isEmpty() ? "" : possiblePackageName + ".";
                return validPackageName + superClass.getSimpleName() + MOCK_TYPE_SUFFIX
                        + random.nextInt(Integer.MAX_VALUE);
            }
        };
    }

    private boolean protectedPackageNameSpaces(Class<?> mockedType) {
        return mockedType.getName().startsWith("java.");
    }
}
