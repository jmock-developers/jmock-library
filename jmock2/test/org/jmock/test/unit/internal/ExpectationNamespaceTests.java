package org.jmock.test.unit.internal;

import junit.framework.TestCase;

import org.jmock.api.Expectation;
import org.jmock.internal.ExpectationNamespace;
import org.jmock.test.unit.support.MockExpectation;


public class ExpectationNamespaceTests extends TestCase {
    ExpectationNamespace namespace = new ExpectationNamespace();
    
    public void testBindsAndResolvesNamesOfExpectations() {
        Expectation a = new MockExpectation();
        Expectation b = new MockExpectation();
        
        namespace.bind("a", a);
        namespace.bind("b", b);
        
        assertSame(a, namespace.resolve("a"));
        assertSame(b, namespace.resolve("b"));
    }
    
    public void testDoesNotAllowNamesToBeRebound() {
        namespace.bind("a", new MockExpectation());
        
        try {
            namespace.bind("a", new MockExpectation());
            fail("should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testFailsIfCannotResolveName() {
        try {
            namespace.resolve("unbound-name");
            fail("should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
    }
}
