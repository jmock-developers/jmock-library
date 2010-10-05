package org.jmock.test.acceptance;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

import junit.framework.TestCase;

public class MockingClassesAcceptanceTests extends TestCase {
    public static final class FinalClass {}
    
    public static class ClassToMock {
        public FinalClass returnInstanceOfFinalClass() {
            return null;
        }
    }
    
    Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
    ClassToMock mock = context.mock(ClassToMock.class);
    
    public void testCanMockClassesWithMethodsThatReturnFinalClasses() {
        final FinalClass result = new FinalClass();
        
        context.checking(new Expectations() {{
            oneOf (mock).returnInstanceOfFinalClass(); will(returnValue(result));
        }});
        
        // This should not crash
        
        assertSame(result, mock.returnInstanceOfFinalClass());
    }
}

