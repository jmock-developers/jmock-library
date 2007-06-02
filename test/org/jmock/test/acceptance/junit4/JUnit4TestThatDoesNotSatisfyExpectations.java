package org.jmock.test.acceptance.junit4;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class JUnit4TestThatDoesNotSatisfyExpectations {
    Mockery context = new JUnit4Mockery();
    Runnable runnable = context.mock(Runnable.class);
    
    @Test
    public void doesNotSatisfyExpectations() {
        context.checking(new Expectations() {{
            one (runnable).run();
        }});
        
        // Return without satisfying the expectation for runnable.run()
    }
}
