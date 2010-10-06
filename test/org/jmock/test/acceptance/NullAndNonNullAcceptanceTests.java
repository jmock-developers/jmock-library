package org.jmock.test.acceptance;

import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;

public class NullAndNonNullAcceptanceTests extends TestCase {
    Mockery context = new Mockery();
    MockedType mock = context.mock(MockedType.class);
    
    public void testNullParameterMatcher() {
        context.checking(new Expectations() {{
            allowing (mock).doSomethingWith(with(aNull(String.class)));
        }});
        
        mock.doSomethingWith(null);
        
        try {
            mock.doSomethingWith("not null");
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError expected) {}
    }
    
    public void testNonNullParameterMatcher() {
        context.checking(new Expectations() {{
            allowing (mock).doSomethingWith(with(aNonNull(String.class)));
        }});
        
        mock.doSomethingWith("not null");
        
        try {
            mock.doSomethingWith(null);
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError expected) {}
    }

    // A defect in Hamcrest
    public void DISABLED_testNullArrayParameter() {
        context.checking(new Expectations() {{
            allowing (mock).doSomethingWithArray(null);
        }});

        mock.doSomethingWithArray(null);

        try {
            mock.doSomethingWithArray(new String[]{"not null"});
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError expected) {}
    }
}
