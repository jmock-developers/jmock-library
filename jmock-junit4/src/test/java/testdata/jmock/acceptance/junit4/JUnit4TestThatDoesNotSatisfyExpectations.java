package testdata.jmock.acceptance.junit4;

import uk.jamesdal.perfmock.Expectations;
import uk.jamesdal.perfmock.Mockery;
import uk.jamesdal.perfmock.integration.junit4.JMock;
import uk.jamesdal.perfmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class JUnit4TestThatDoesNotSatisfyExpectations {
    private Mockery context = new JUnit4Mockery();
    private Runnable runnable = context.mock(Runnable.class);
    
    @Test
    public void doesNotSatisfyExpectations() {
        context.checking(new Expectations() {{
            oneOf (runnable).run();
        }});
        
        // Return without satisfying the expectation for runnable.run()
    }
}
