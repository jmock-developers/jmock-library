/*
 * Created on Dec 17, 2003
 */
package test.jmock.builder;

import org.jmock.builder.*;

public class Mock_ErrorMessage_IntegrationTest extends MockObjectTestCase {

    // TODO first cut test, needs stucture
    public void testUnexpectedCallAlsoShowsExpectedCalls() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Mock mock = new Mock(Types.WithOneMethod.class);
        
        mock.method("twoArgsReturnsInt").with(ANYTHING, ANYTHING).willReturn(1)
            .expectOnce();
        mock.method("twoArgsReturnsInt").with(eq("arg1"), same(obj1)).willReturn(1)
            .expectOnce();
        
        try {
            ((Types.WithOneMethod)mock.proxy()).twoArgsReturnInt("not arg1", obj2);
        } catch (Error error) {
            assertEquals("Should be error message", 
                    "No match found\n" +
                    "Invoked: mockTypes$WithOneMethod.twoArgsReturnInt(<not arg1>, <" + obj2 + ">)\n" +
                    "in:\n" +
                    "Method  = twoArgsReturnsInt, (<any value>, <any value>), called once, returns <1>\n" +
                    "Method  = twoArgsReturnsInt, (< = arg1>, <== <" + obj1 + ">>), called once, returns <1>",
                    error.getMessage().trim());
            return;
        }

        fail("Should have throw exception");
   }
}
