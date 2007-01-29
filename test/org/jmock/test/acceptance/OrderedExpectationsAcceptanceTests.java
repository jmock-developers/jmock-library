package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;

public class OrderedExpectationsAcceptanceTests extends TestCase {
    Mockery context = new Mockery();
    MockedType mock = context.mock(MockedType.class, "mock");
    
    public void testCanConstrainAnInvocationToOccurAfterPreviousInvocation() {
        context.checking(new Expectations() {{
            allowing (mock).method1();
                named("first");
            allowing (mock).method2();
                after("first");
        }});
        
        try {
            mock.method2();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            // expected
        }
    }
    
    public void testAllowsInvocationsToOccurInOrderSpecifiedByAfterClause() {
        context.checking(new Expectations() {{
            allowing (mock).method1();
                named("first");
            allowing (mock).method2();
                after("first");
        }});
        
        mock.method1();
        mock.method2();
    }
}
