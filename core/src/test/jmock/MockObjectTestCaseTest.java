package test.jmock;

import junit.framework.TestCase;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Constraint;

import test.jmock.core.testsupport.AlwaysFalse;
import test.jmock.core.testsupport.AlwaysTrue;



public class MockObjectTestCaseTest extends TestCase {
    Constraint trueConstraint = AlwaysTrue.INSTANCE;
    Constraint falseConstraint = AlwaysFalse.INSTANCE;
    
    private MockObjectTestCase testCase;
    
    public void setUp() {
    	testCase = new MockObjectTestCase();
    }
    
    interface MockedType {
    }
    
    public void testHasFactoryMethodForConstructingNamedMockObjects() {
        String mockName = "MOCK-NAME";
        
        Mock mock = testCase.mock( MockedType.class, mockName );
        
        assertSame( "mocked type",MockedType.class, mock.getMockedType() );
        assertTrue( "proxy is mocked type", mock.proxy() instanceof MockedType );
        
        assertEquals( "mock name", mockName, mock.toString() );
    }
    
    public void testUsesHookMethodToGenerateDefaultNamesForMockObjects() {
        Mock mock = testCase.mock( MockedType.class );
        
        assertEquals( "mock name", 
                      testCase.defaultMockNameForType(MockedType.class), mock.toString() );
    }
    
    public void testGeneratesDefaultMockNamesFromMockedType() {
        assertEquals( "name", "mockMockedType", testCase.defaultMockNameForType(MockedType.class) );
    }
}
