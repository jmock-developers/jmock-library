/*
 * Created on Dec 17, 2003
 */
package atest.jmock.builder;

import org.jmock.builder.Mock;
import org.jmock.builder.MockObjectTestCase;

public class Mock_ErrorMessage_IntegrationTest extends MockObjectTestCase {

    // TODO first cut test, needs stucture
    public void testUnexpectedCallAlsoShowsExpectedCalls() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Mock mock = new Mock(Types.WithTwoMethods.class, "MOCK_NAME");
        
        mock.method("twoArgsReturnsInt").with(ANYTHING, ANYTHING).willReturn(1)
            .expectOnce();
        mock.method("twoArgsReturnsInt").with(eq("arg1"), same(obj1)).willReturn(1)
            .expectOnce();
        
        try {
            ((Types.WithTwoMethods)mock.proxy()).twoArgsReturnInt("not arg1", obj2);
        } catch (Error error) {
            assertEquals("Should be error message", 
                    (Object)"MOCK_NAME: No match found\n" +
                    "Invoked: twoArgsReturnInt(<not arg1>, <" + obj2 + ">)\n" +
                    "in:\n" +
                    "Method  = twoArgsReturnsInt, (<any value>, <any value>), expected once, returns <1>\n" +
                    "Method  = twoArgsReturnsInt, (< = arg1>, <== <" + obj1 + ">>), expected once, returns <1>",
                    error.getMessage().trim());
            return;
        }

        fail("Should have throw exception");
   }
    
    public void testShowsNoExpectationsStringWhenNoExpectationsSet() {
        Mock mock = new Mock(Types.WithTwoMethods.class);
        try {
            ((Types.WithTwoMethods)mock.proxy()).twoArgsReturnInt("arg1", "arg2");
        } catch (Error error) {
            assertEquals("Should be error message", 
                (Object)"mockTypes$WithTwoMethods: No match found\n" +
                "Invoked: twoArgsReturnInt(<arg1>, <arg2>)\n" +
                "in:\n" +
                "No expectations set",
                (Object)error.getMessage().trim());
            return;
        }

        fail("Should have throw exception");
    }
}
