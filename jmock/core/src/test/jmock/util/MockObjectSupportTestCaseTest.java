package test.jmock.util;

import org.jmock.util.MockObjectSupportTestCase;

import junit.framework.TestCase;


public abstract class MockObjectSupportTestCaseTest extends TestCase {
    
    private static final String DUMMY_NAME = "DUMMY NAME";
    
    interface ExampleInterface {
        void method1();
    }
    
    
    MockObjectSupportTestCase testCase;
    
    public void setUp() {
        testCase = new MockObjectSupportTestCase();
    }
    
    public void testCanCreateNamedDummyObjects() {
        Object dummy = testCase.newDummy(DUMMY_NAME);
        
        assertEquals( "should return name from toString",
                      DUMMY_NAME, dummy.toString() );
    }
    
    public void testCanCreateNamedDummyObjectsThatImplementASpecificInterface() {
        Object dummy = testCase.newDummy(ExampleInterface.class,DUMMY_NAME);
        
        assertTrue( "should be instanceof ExampleInterface",
                    dummy instanceof ExampleInterface );
        assertEquals( "should return name from toString",
                      DUMMY_NAME, dummy.toString() );
    }
    
    public void testGeneratesUsefulNamesForDummiesFromTheDummiedInterface() {
        Object dummy = testCase.newDummy(ExampleInterface.class);
        
        assertEquals( "should return name from toString",
                      "dummyDummyTest$ExampleInterface", dummy.toString() );
    }
}
