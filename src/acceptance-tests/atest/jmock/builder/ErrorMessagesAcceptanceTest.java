/*
 * Created on Dec 17, 2003
 */
package atest.jmock.builder;

import org.jmock.builder.Mock;
import org.jmock.builder.MockObjectTestCase;
import org.jmock.dynamic.DynamicMockError;

public class ErrorMessagesAcceptanceTest extends MockObjectTestCase {

    public void testUnexpectedCallAlsoShowsExpectedCalls() {
        String arg1 = "arg1";
        String notArg1 = "not "+arg1;
        Object arg2 = new Object();
        Object notArg2 = new Object();
        Mock mock = new Mock(Types.WithTwoMethods.class, "MOCK_NAME");
        
        mock.method("twoArgsReturnsInt").with(ANYTHING, ANYTHING).willReturn(1)
            .expectOnce();
        mock.method("twoArgsReturnsInt").with(eq(arg1), same(arg2)).willReturn(1)
            .expectOnce();
        
        try {
            ((Types.WithTwoMethods)mock.proxy()).twoArgsReturnInt(notArg1, notArg2);
        } catch (DynamicMockError error) {
            String errorMessage = error.getMessage();
            
            assertStringContains( "should contain mock name", 
                                  errorMessage, "MOCK_NAME" );
            assertStringContains( "should report cause of error",
                                   errorMessage, "No match found" );
            assertSubstringOrder( "mock name should appear before cause of error",
                                  errorMessage, "MOCK_NAME", "No match found" );
            
            assertStringContains( "should report method that caused error",
                errorMessage, 
                "twoArgsReturnInt(<"+notArg1+">, <" + notArg2 + ">)" );
            
            assertStringContains( "should report acceptable methods (first method)",
                errorMessage, 
                "twoArgsReturnsInt, (<any value>, <any value>), expected once, returns <1>" );
            
            assertStringContains( "should report acceptable methods (second method)",
                errorMessage, 
                "twoArgsReturnsInt, (<= "+arg1+">, <== <"+arg2+">>), expected once, returns <1>" );
            
            assertSubstringOrder( "should report acceptable methods in search order",
                errorMessage,
                "twoArgsReturnsInt, (<any value>, <any value>), expected once, returns <1>",
                "twoArgsReturnsInt, (<= "+arg1+">, <== <"+arg2+">>), expected once, returns <1>" );
            
            return;
        }
        
        fail("Should have thrown a DynamocMockError");
    }
    
    public void testShowsNoExpectationsStringWhenNoExpectationsSet() {
        Mock mock = new Mock(Types.WithTwoMethods.class);
        try {
            ((Types.WithTwoMethods)mock.proxy()).twoArgsReturnInt("arg1", "arg2");
        } catch (Error error) {
            String errorMessage = error.getMessage();
            
            assertStringContains( "should report no expectations have been set",
                errorMessage, "No expectations set" );
            return;
        }

        fail("Should have throw exception");
    }
    
    public static void assertStringContains( String message, String string, String substring ) {
        assertTrue( message + ": expected \"" + string + "\" to contain \"" + substring + "\"",
                    string.indexOf(substring) >= 0 );
    }
    
    public static void assertSubstringOrder( String message, String string, 
											 String earlierSubstring, String laterSubstring )
    {
        assertStringContains( message, string, earlierSubstring );
        assertStringContains( message, string, laterSubstring );
        
        int earlierIndex = string.indexOf(earlierSubstring);
        int laterIndex = string.indexOf(laterSubstring);
        
        assertTrue( message+": expected \""+earlierSubstring+"\" "+
                        "to appear before \"" + laterSubstring+"\" in \""+string+"\"",
                    earlierIndex < laterIndex );
                    
    }
}
