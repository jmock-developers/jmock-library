package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;

// See issue JMOCK-184
public class DefineExpectationWithinExpectationsAcceptanceTests_WORK_IN_PROGRESS extends TestCase {
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
            one (a).a(aNewMockWithExpectations());
        }});
    }
    
    private MockedTypeB aNewMockWithExpectations() {
        final MockedTypeB mock = context.mock(MockedTypeB.class);
        
        context.checking(new Expectations() {{
            ignoring (mock);
        }});
            
        return mock;
    }
}
