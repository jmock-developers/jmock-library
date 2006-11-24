package org.jmock.test.unit.lib.nonstd;

import junit.framework.TestCase;

import org.jmock.api.Imposteriser;
import org.jmock.lib.action.ReturnValueAction;
import org.jmock.lib.nonstd.UnsafeHackConcreteClassImposteriser;

public class UnsafeHackConcreteClassImposteriserTests extends TestCase {
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
    
    public void testCanImposteriseAConcreteClassWithoutCallingItsConstructorOrInstanceInitialiserBlocks() {
        Imposteriser imposteriser = new UnsafeHackConcreteClassImposteriser();
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
