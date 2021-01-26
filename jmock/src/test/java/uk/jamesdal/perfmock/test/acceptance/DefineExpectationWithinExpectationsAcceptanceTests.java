package uk.jamesdal.perfmock.test.acceptance;

import junit.framework.TestCase;

import uk.jamesdal.perfmock.Expectations;
import uk.jamesdal.perfmock.Mockery;

// See issue JMOCK-184
public class DefineExpectationWithinExpectationsAcceptanceTests extends TestCase {
    public interface MockedTypeA {
        void a(MockedTypeB b);
    }
    
    public interface MockedTypeB {
        void b();
    }
    
    
    Mockery context = new Mockery();
    
    MockedTypeA a = context.mock(MockedTypeA.class, "a");
    
    public void testCanDefineExpectationsWithinExpectations() {
        context.checking(new Expectations() {{
            oneOf (a).a(aNewMockWithExpectations());
        }});
    }
    
    private MockedTypeB aNewMockWithExpectations() {
        final MockedTypeB mock = context.mock(MockedTypeB.class);
        
        context.checking(new Expectations() {{
            ignoring (mock);
        }});
            
        return mock;
    }
    
    public void testCanStillIgnoreEntireMockObjectsBeforeAnotherExpectation() {
        final MockedTypeA a2 = context.mock(MockedTypeA.class, "a2");
        
        context.checking(new Expectations() {{
            ignoring(a2);
            oneOf (a).a(aNewMockWithExpectations());
        }});
    }
}
