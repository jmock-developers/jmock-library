package org.jmock.test.unit.lib.nonstd;

import junit.framework.TestCase;

import org.jmock.core.Imposteriser;
import org.jmock.lib.action.ReturnValueAction;
import org.jmock.lib.nonstd.UnsafeHackConcreteClassImposteriser;

public class UnsafeHackConcreteClassImposteriserTests extends TestCase {
    public static class ConcreteClassWithNastyConstructor {
        public ConcreteClassWithNastyConstructor() {
            throw new IllegalStateException("should not be called");
        }
        
        public String foo() {
            throw new IllegalStateException("should not be called");
        }
    }
    
    public interface AnInterface {
        String foo();
    }
    
    public void testCanImposteriseAConcreteClassWithoutCallingItsConstructor() {
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
