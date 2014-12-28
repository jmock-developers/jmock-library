package testdata.jmock.acceptance.junit4;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class JUnit4TestThatDoesSatisfyExpectations {
    private Mockery context = new JUnit4Mockery();
    private Runnable runnable = context.mock(Runnable.class);
    
    @Test
    public void doesSatisfyExpectations() {
        context.checking(new Expectations() {{
            oneOf (runnable).run();
        }});
        
        runnable.run();
    }
}
