package org.jmock.junit5.testdata;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.junit5.JUnit5Mockery;
import org.jmock.junit5.services.ExpectationThrows;
import org.jmock.junit5.services.ExpectationExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

@ExtendWith(ExpectationExtension.class)
public class JUnit5TestThatThrowsExpectedException {
    @RegisterExtension
    private Mockery context = new JUnit5Mockery();
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
    
    public static class  CheckedException extends Exception {
    }
    
    public interface WithException {
        void throwingMethod() throws CheckedException;
        void anotherMethod();
    }
}
