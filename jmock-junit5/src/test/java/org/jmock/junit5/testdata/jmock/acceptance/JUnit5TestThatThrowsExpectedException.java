package org.jmock.junit5.testdata.jmock.acceptance;

import org.jmock.Expectations;
import org.jmock.junit5.JUnit5Mockery;
import org.jmock.junit5.extensions.ExpectationExtension;
import org.jmock.junit5.extensions.ExpectationThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

@ExtendWith(ExpectationExtension.class)
public class JUnit5TestThatThrowsExpectedException {
    @RegisterExtension
    JUnit5Mockery context = new JUnit5Mockery();
    private WithException withException = context.mock(WithException.class);
    
    @Test
    @ExpectationThrows(expected=CheckedException.class)
    public void doesNotSatisfyExpectationsWhenExpectedExceptionIsThrown() throws CheckedException {
        context.checking(new Expectations() {{
            oneOf (withException).anotherMethod();
            oneOf (withException).throwingMethod(); will(throwException(new CheckedException()));
        }});
        
        withException.throwingMethod();
    }
    
    public static class CheckedException extends Exception {
    }
    
    public interface WithException {
        void throwingMethod() throws CheckedException;
        void anotherMethod();
    }
}
