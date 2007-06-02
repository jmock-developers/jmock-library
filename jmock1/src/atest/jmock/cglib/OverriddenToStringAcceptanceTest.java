package atest.jmock.cglib;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.jmock.core.DynamicMockError;

/* 
 * See Jira issue JMOCK-96
 */
public class OverriddenToStringAcceptanceTest extends MockObjectTestCase {
    public static class ClassThatOverridesToString {
        public String toString() {
            return "this will be mocked";
        }
        
        public void doSomething() {}
    }
    
    Mock mock = mock(ClassThatOverridesToString.class, "mock-name");
    
    public void testCanMockClassThatOverridesToString() {
        assertEquals("mock-name", mock.proxy().toString());
        
        mock.stubs().method("toString").will(returnValue("string result"));
        
        assertEquals("string result", mock.proxy().toString());
    }
    
    public void testCanGenerateErrorMessageFromMockedClassThatOverridesToString() {
        try {
            ((ClassThatOverridesToString)mock.proxy()).doSomething(); // unexpected
            fail("should have thrown DynamicMockError");
        }
        catch (DynamicMockError e) {
            String errorMessage = e.writeTo(new StringBuffer()).toString();
            assertNotNull(errorMessage); // actually we just care that e.writeTo doesn't enter an infinite loop
        }
    }
}
