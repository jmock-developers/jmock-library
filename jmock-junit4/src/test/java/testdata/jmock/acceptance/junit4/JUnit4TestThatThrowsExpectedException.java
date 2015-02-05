package testdata.jmock.acceptance.junit4;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class JUnit4TestThatThrowsExpectedException {
    private Mockery context = new JUnit4Mockery();
    private WithException withException = context.mock(WithException.class);
    
    @Test(expected=CheckedException.class)
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
