package org.jmock.test.unit.lib.legacy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;

import org.jmock.api.Action;
import org.jmock.api.Imposteriser;
import org.jmock.api.Invocation;
import org.jmock.api.Invokable;
import org.jmock.lib.action.ReturnValueAction;
import org.jmock.lib.action.VoidAction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

public class ClassImposteriserTests {
    Action action = new ReturnValueAction("result");
    
    @SuppressWarnings("ClassInitializerMayBeStatic")
    public static class ConcreteClassWithNastyConstructor {
        {
            nasty("initialisation block should not be run");
        }
        
        public ConcreteClassWithNastyConstructor() {
            nasty("constructor should not be run");
        }
        
        public String foo() {
            nasty("should not be run");
            return null; // never reached
        }

        private static void nasty(String nastiness) {
            throw new IllegalStateException(nastiness);
        }
    }
    
    public interface AnInterface {
        String foo();
    }
    
    public static abstract class AnAbstractNestedClass {
        @SuppressWarnings("UnusedDeclaration")
        public abstract String foo();
    }
    
    public static class AnInnerClass {
        @SuppressWarnings("UnusedDeclaration")
        public String foo() {return "original result";}
    }
    
    public static final class AFinalClass {
        @SuppressWarnings("UnusedDeclaration")
        public String foo() {return "original result";}
    }
    
    public static class AClassWithAPrivateConstructor {
        @SuppressWarnings("unused")
        private AClassWithAPrivateConstructor(String someArgument) {}
        
        public String foo() {return "original result";}
    }
    
    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void canImposteriseInterfacesAndNonFinalInstantiableClasses(Imposteriser imposteriser) {
        assertTrue("should report that it can imposterise interfaces",
                   imposteriser.canImposterise(Runnable.class));
        assertTrue("should report that it can imposterise classes",
                   imposteriser.canImposterise(Date.class));
        assertTrue("should report that it cannot imposterise final classes",
                   !imposteriser.canImposterise(AFinalClass.class));
        assertTrue("should report that it can imposterise nested classes",
                   imposteriser.canImposterise(AnAbstractNestedClass.class));
        assertTrue("should report that it can imposterise inner classes",
                   imposteriser.canImposterise(AnInnerClass.class));
        assertTrue("should report that it can imposterise classes with non-public constructors",
                   imposteriser.canImposterise(AClassWithAPrivateConstructor.class));
        assertTrue("should report that it cannot imposterise primitive types",
                   !imposteriser.canImposterise(int.class));
        assertTrue("should report that it cannot imposterise void",
                   !imposteriser.canImposterise(void.class));
    }

    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void canImposteriseAConcreteClassWithoutCallingItsConstructorOrInstanceInitialiserBlocks(Imposteriser imposteriser) {
        ConcreteClassWithNastyConstructor imposter = 
            imposteriser.imposterise(action, ConcreteClassWithNastyConstructor.class);
        
        assertEquals("result", imposter.foo());
    }

    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void canImposteriseAnInterface(Imposteriser imposteriser) {
        AnInterface imposter = 
            imposteriser.imposterise(action, AnInterface.class);
        
        assertEquals("result", imposter.foo());
    }

    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void canImposteriseAClassWithAPrivateConstructor(Imposteriser imposteriser) {
        AClassWithAPrivateConstructor imposter = 
            imposteriser.imposterise(action, AClassWithAPrivateConstructor.class);
        
        assertEquals("result", imposter.foo());
    }

    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void canImposteriseAClassInASignedJarFile(Imposteriser imposteriser) throws Exception {
        File jarFile = new File("../testjar/target/signed.jar");
        
        assertTrue("Signed JAR file does not exist (use Ant to build it", jarFile.exists());
        
        URL jarURL = jarFile.toURI().toURL();
        
        // Can't close as we might be in java 6
        @SuppressWarnings("resource")
        URLClassLoader loader = new URLClassLoader(new URL[]{jarURL});
        
        Class<?> typeInSignedJar = loader.loadClass("org.jmock.testjar.TypeInSignedJar");
        
        Object o = imposteriser.imposterise(new VoidAction(), typeInSignedJar);
        
        assertTrue(typeInSignedJar.isInstance(o));
    }
    
    public static class ClassWithFinalToStringMethod {
        @Override
        public final String toString() {
            return "you can't override me!";
        }
    }
    
    // See issue JMOCK-150
    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void cannotImposteriseAClassWithAFinalToStringMethod(Imposteriser imposteriser) {
        assertTrue("should not be able to imposterise it", !imposteriser.canImposterise(ClassWithFinalToStringMethod.class));
        
        try {
            imposteriser.imposterise(new VoidAction(), ClassWithFinalToStringMethod.class);
            fail("should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException expected) {
            
        }
    }

    // See issue JMOCK-256 (Github #36)
    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void doesntDelegateFinalizeMethod(Imposteriser imposteriser) throws Exception {
        Invokable failIfInvokedAction = new Invokable() {
            public Object invoke(Invocation invocation) throws Throwable {
                fail("invocation should not have happened");
                return null;
            }
        };

        Object imposter = imposteriser.imposterise(failIfInvokedAction, Object.class);
        invokeMethod(imposter, Object.class.getDeclaredMethod("finalize"));
    }

    public interface EmptyInterface {}
    
    // See issue JMOCK-145
    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void worksAroundBugInCglibWhenAskedToImposteriseObject(Imposteriser imposteriser) {
        imposteriser.imposterise(new VoidAction(), Object.class);
        
        imposteriser.imposterise(new VoidAction(), Object.class, EmptyInterface.class);
        
        imposteriser.imposterise(new VoidAction(), Object.class, AnInterface.class);
    }

    private Object invokeMethod(Object object, Method method, Object... args) throws IllegalAccessException, InvocationTargetException {
        method.setAccessible(true);
        return method.invoke(object, args);
    }

}
