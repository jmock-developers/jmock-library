package org.jmock.test.unit.lib.nonstd;

import java.util.Date;

import junit.framework.TestCase;

import org.jmock.api.Imposteriser;
import org.jmock.lib.action.ReturnValueAction;
import org.jmock.lib.nonstd.UnsafeHackConcreteClassImposteriser;

public class UnsafeHackConcreteClassImposteriserTests extends TestCase {
    Imposteriser imposteriser = new UnsafeHackConcreteClassImposteriser();
    
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
        abstract String foo();
    }
    
    public static class AnInnerClass {
        void foo() {}
    }
    
    public void testCanImposteriseInterfacesAndInstantiableClasses() {
        assertTrue("should report that it can imposterise interfaces",
                   imposteriser.canImposterise(Runnable.class));
        assertTrue("should report that it can imposterise classes",
                   imposteriser.canImposterise(Date.class));
        assertTrue("should report that it can imposterise nested classes",
                   imposteriser.canImposterise(AnAbstractNestedClass.class));
        assertTrue("should report that it can imposterise inner classes",
                   imposteriser.canImposterise(AnInnerClass.class));
        assertTrue("should report that it cannot imposterise primitive types",
                   !imposteriser.canImposterise(int.class));
        assertTrue("should report that it cannot imposterise void",
                   !imposteriser.canImposterise(void.class));
    }

    public void testCanImposteriseAConcreteClassWithoutCallingItsConstructorOrInstanceInitialiserBlocks() {
        ConcreteClassWithNastyConstructor imposter = 
            imposteriser.imposterise(new ReturnValueAction("result"), 
                                     ConcreteClassWithNastyConstructor.class);
        
        assertEquals("result", imposter.foo());
    }
    
    public void testCanImposteriseAnInterface() {
        Imposteriser imposteriser = new UnsafeHackConcreteClassImposteriser();
        AnInterface imposter = 
            imposteriser.imposterise(new ReturnValueAction("result"), 
                                     AnInterface.class);
        
        assertEquals("result", imposter.foo());
    }
}
