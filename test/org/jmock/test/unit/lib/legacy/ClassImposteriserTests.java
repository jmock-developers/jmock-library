package org.jmock.test.unit.lib.legacy;

import junit.framework.TestCase;
import org.jmock.api.Action;
import org.jmock.api.Imposteriser;
import org.jmock.api.Invocation;
import org.jmock.api.Invokable;
import org.jmock.lib.action.ReturnValueAction;
import org.jmock.lib.action.VoidAction;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ClassImposteriserTests {
    Action action = new ReturnValueAction("result");
    
    Imposteriser imposteriser = ClassImposteriser.INSTANCE;
    
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
    
    @Test
    public void canImposteriseInterfacesAndNonFinalInstantiableClasses() {
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

    @Test
    public void canImposteriseAConcreteClassWithoutCallingItsConstructorOrInstanceInitialiserBlocks() {
        ConcreteClassWithNastyConstructor imposter = 
            imposteriser.imposterise(action, ConcreteClassWithNastyConstructor.class);
        
        assertEquals("result", imposter.foo());
    }

    @Test
    public void canImposteriseAnInterface() {
        AnInterface imposter = 
            imposteriser.imposterise(action, AnInterface.class);
        
        assertEquals("result", imposter.foo());
    }

    @Test
    public void canImposteriseAClassWithAPrivateConstructor() {
        AClassWithAPrivateConstructor imposter = 
            imposteriser.imposterise(action, AClassWithAPrivateConstructor.class);
        
        assertEquals("result", imposter.foo());
    }

    @Test
    public void canImposteriseAClassInASignedJarFile() throws Exception {
        File jarFile = new File("build/testdata/signed.jar");
        
        assertTrue("Signed JAR file does not exist (use Ant to build it", jarFile.exists());
        
        URL jarURL = jarFile.toURI().toURL();
        ClassLoader loader = new URLClassLoader(new URL[]{jarURL});
        Class<?> typeInSignedJar = loader.loadClass("TypeInSignedJar");
        
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
    @Test
    public void cannotImposteriseAClassWithAFinalToStringMethod() {
        assertTrue("should not be able to imposterise it", !imposteriser.canImposterise(ClassWithFinalToStringMethod.class));
        
        try {
            imposteriser.imposterise(new VoidAction(), ClassWithFinalToStringMethod.class);
            fail("should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException expected) {
            
        }
    }

    // See issue JMOCK-256 (Github #36)
    @Test
    public void doesntDelegateFinalizeMethod() throws Exception {
        Invokable failIfInvokedAction = new Invokable() {
            @Override
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
    @Test
    public void worksAroundBugInCglibWhenAskedToImposteriseObject() {
        imposteriser.imposterise(new VoidAction(), Object.class);
        
        imposteriser.imposterise(new VoidAction(), Object.class, EmptyInterface.class);
        
        imposteriser.imposterise(new VoidAction(), Object.class, AnInterface.class);
    }

    private Object invokeMethod(Object object, Method method, Object... args) throws IllegalAccessException, InvocationTargetException {
        method.setAccessible(true);
        return method.invoke(object, args);
    }

}
