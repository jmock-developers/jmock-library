/*  Copyright (c) 2000-2004 jMock.org
 */
package atest.jmock;

import junit.framework.AssertionFailedError;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.DynamicMockError;


public class ErrorMessagesAcceptanceTest extends MockObjectTestCase
{
    private static final String MOCK_NAME = "MOCK_NAME";

    public void testUnexpectedCallAlsoShowsExpectedCalls() {
        String arg1 = "arg1";
        String callID = "callID";
        Object arg2 = newDummy("arg2");
        Mock mock = mock(Types.WithTwoMethods.class, MOCK_NAME);
        unregisterToVerify(mock); // don't automatically verify

        mock.expects(once()).method("twoArgsReturnsInt").with(ANYTHING, ANYTHING)
                .will(returnValue(1)).id(callID);
        mock.expects(once()).method("twoArgsReturnsInt").with(eq(arg1), same(arg2))
                .after(callID)
                .will(returnValue(1));

        try {
            ((Types.WithTwoMethods)mock.proxy()).noArgsReturnsNothing();
        }
        catch (DynamicMockError error) {
            String errorMessage = error.getMessage();

            String causeOfError = "no match found";
            String expectedMethod1 =
                    "expected once: twoArgsReturnsInt( ANYTHING, ANYTHING ), returns <1> [" + callID + "]";
            String expectedMethod2 =
                    "expected once: twoArgsReturnsInt( eq(<" + arg1 + ">), same(<" + arg2 + ">) ), after " + callID + ", returns <1>";

            assertStringContains("should contain mock name",
                                 errorMessage, MOCK_NAME);

            assertStringContains("should report cause of error",
                                 errorMessage, causeOfError);

            assertSubstringOrder("mock name should appear before cause of error",
                                 errorMessage, MOCK_NAME, causeOfError);

            assertStringContains("should report invokedMethod that caused error",
                                 errorMessage,
                                 "noArgsReturnsNothing()");

            assertStringContains("should report acceptable methods (#1)",
                                 errorMessage, expectedMethod1);

            assertStringContains("should report acceptable methods (#2)",
                                 errorMessage, expectedMethod2);

            assertSubstringOrder("should report acceptable methods in search order",
                                 errorMessage, expectedMethod1, expectedMethod2);

            return;
        }

        fail("expected DynamicMockError");
    }

    public void testShowsNoExpectationsStringWhenNoExpectationsSet() {
        Mock mock = mock(Types.WithTwoMethods.class);
        try {
            ((Types.WithTwoMethods)mock.proxy()).twoArgsReturnsInt("arg1", "arg2");
        }
        catch (DynamicMockError error) {
            String errorMessage = error.getMessage();

            assertStringContains("should report no expectations have been set",
                                 errorMessage, "No expectations set");
            return;
        }

        fail("expected DynamicMockError");
    }

    public void testShowsExpectationThatDoesNotVerify() {
        String arg1 = "arg1";
        Object arg2 = new Object();
        String expectedMethod =
                "expected once: twoArgsReturnsInt( eq(<" + arg1 + ">), same(<" + arg2 + ">) ), returns <1>";

        Mock mock = mock(Types.WithTwoMethods.class, MOCK_NAME);
        unregisterToVerify(mock); // don't automatically verify

        mock.expects(once()).method("twoArgsReturnsInt").with(eq(arg1), same(arg2))
                .will(returnValue(1));

        try {
            mock.verify();
        }
        catch (AssertionFailedError ex) {
            String message = ex.getMessage();

            assertStringContains("error message should contain expected invokedMethod",
                                 message, expectedMethod);
            assertStringContains("error message should describe error",
                                 message, "was not invoked");

            return;
        }

        fail("expected AssertionFailedError");
    }

    public static void assertStringContains( String message, String string, String substring ) {
        assertTrue(message + ": expected \"" + string + "\" to contain \"" + substring + "\"",
                   string.indexOf(substring) >= 0);
    }

    public static void assertSubstringOrder( String message, String string,
                                             String earlierSubstring, String laterSubstring ) {
        assertStringContains(message, string, earlierSubstring);
        assertStringContains(message, string, laterSubstring);

        int earlierIndex = string.indexOf(earlierSubstring);
        int laterIndex = string.indexOf(laterSubstring);

        assertTrue(message + ": expected \"" + earlierSubstring + "\" " +
                   "to appear before \"" + laterSubstring + "\" in \"" + string + "\"",
                   earlierIndex < laterIndex);

    }
}
