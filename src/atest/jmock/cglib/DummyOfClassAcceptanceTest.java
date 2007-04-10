package atest.jmock.cglib;

import org.jmock.cglib.MockObjectTestCase;

/*
 * See Jira issue JMOCK-79
 */
public class DummyOfClassAcceptanceTest extends MockObjectTestCase {
    public static class MockedType {
        public void doSomething() {
        }
    }
    
    public void testCanCreateADummyOfAClassWithCGLIB() {
        Object dummy = newDummy(MockedType.class);
        
        assertTrue(dummy instanceof MockedType);
    }
}
