package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;


/* Acceptance test for issue JMOCK-30 (http://jira.codehaus.org/browse/JMOCK-30).
 */
public class CascadedFailuresAcceptanceTests extends TestCase {
    public interface MockedType {
        void realExpectationFailure(int i);
        void invocationCausedByExpectationFailure();
        void anotherRealExpectationFailure();
    }

    private Mockery context = new Mockery();
    private MockedType mock = context.mock(MockedType.class, "mock");
    private MockedType otherMock = context.mock(MockedType.class, "otherMock");
    
    
    private void maskedExpectationFailure(MockedType mock1, MockedType mock2) {
        try {
            mock1.realExpectationFailure(2);
        }
        finally {
            mock2.invocationCausedByExpectationFailure();
        }
    }
    
    @Override
    public void setUp() {
        context.checking(new Expectations() {{
            allowing (mock).realExpectationFailure(1);
        }});
    }
    
    public void testMockeryReportsFirstFailedMethod() {
        try {
            maskedExpectationFailure(mock, mock);
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            assertSame("invoked object",
                       mock, e.invocation.getInvokedObject());
            assertEquals("invoked method", 
                         "realExpectationFailure", e.invocation.getInvokedMethod().getName() );
        }
    }

    public void testMockeryReportsFirstFailedObject() {
        try {
            maskedExpectationFailure(mock, otherMock);
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            assertSame("invoked object",
                       mock, e.invocation.getInvokedObject());
            assertEquals("invoked method", 
                         "realExpectationFailure", e.invocation.getInvokedMethod().getName() );
        }
    }

    // See issue JMOCK-183 (http://jira.codehaus.org/browse/JMOCK-183).
    public void testVerifyReportsFirstFailure() {
        try {
            mock.realExpectationFailure(2);
        }
        catch (ExpectationError e) { /* swallowed */ }
        
        try {
            context.assertIsSatisfied();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            assertSame("invoked object",
                       mock, e.invocation.getInvokedObject());
            assertEquals("invoked method", 
                         "realExpectationFailure", e.invocation.getInvokedMethod().getName() );
        }
    }
}
