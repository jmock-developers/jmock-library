package test.jmock.cglib;

import org.jmock.cglib.MockObjectTestCase;

import junit.framework.TestCase;

public class CGLIBMockObjectTestCaseTest extends TestCase {
    private static final String TEST_NAME = "TEST-NAME";
    
    public void testCanPassTestNameToConstructor() {
        TestCase testCase = new MockObjectTestCase(TEST_NAME) {};
        assertEquals("test name", TEST_NAME, testCase.getName());
    }
    
    public void testHasNoArgumentConstructor() {
        TestCase testCase = new MockObjectTestCase() {};
        testCase.setName(TEST_NAME);
        assertEquals("test name", TEST_NAME, testCase.getName());
    }
}
