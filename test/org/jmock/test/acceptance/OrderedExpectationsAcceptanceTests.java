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

    public void testCanConstrainAnInvocationToOccurBeforeSubsequentInvocation() {
        context.checking(new Expectations() {{
            allowing (mock).method1();
                before("last");
            allowing (mock).method2();
                named("last");
        }});
        
        mock.method1();
        mock.method1();
        
        mock.method2();
        
        try {
            mock.method1();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            // expected
        }
    }
    
    public void testCannotCreateMultipleExpectationsWithTheSameName() {
        try {
            context.checking(new Expectations() {{
                one (mock).method1(); named("duplicated-name");
                one (mock).method1(); named("duplicated-name");
            }});
            fail("should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testCannotReferToAnUnboundNameInAfterClause() {
        try {
            context.checking(new Expectations() {{
                one (mock).method1(); after("unbound-name");
            }});
            
            mock.method1();
            
            fail("should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testCannotReferToAnUnboundNameInBeforeClause() {
        try {
            context.checking(new Expectations() {{
                one (mock).method1(); before("unbound-name");
            }});
            
            mock.method1();
            
            fail("should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
    }
}
