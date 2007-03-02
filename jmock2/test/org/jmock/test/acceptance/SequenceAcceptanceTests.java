package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;

public class SequenceAcceptanceTests extends TestCase {
    Mockery context = new Mockery();
    MockedType mock = context.mock(MockedType.class, "mock");
    
    public void testCanConstrainInvocationsToOccurInOrder() {
        context.checking(new Expectations() {{
            allowing (mock).method1();
                inSequence("s");
            allowing (mock).method2();
                inSequence("s");
        }});
        
        try {
            mock.method2();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            // expected
        }
    }
    
    public void testAllowsInvocationsToSequence() {
        context.checking(new Expectations() {{
            allowing (mock).method1(); inSequence("s");
            allowing (mock).method2(); inSequence("s");
        }});
        
        mock.method1();
        mock.method2();
    }
    
    public void testSequencesAreIndependentOfOneAnother() {
        context.checking(new Expectations() {{
            allowing (mock).method1(); inSequence("s");
            allowing (mock).method2(); inSequence("s");
            
            allowing (mock).method3(); inSequence("t");
            allowing (mock).method4(); inSequence("t");
        }});
        
        mock.method1();
        mock.method3();
        mock.method2();
        mock.method4();
    }
}
