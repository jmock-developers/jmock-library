package org.jmock.junit5.testdata.jmock.acceptance;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class JUnit5TestThatDoesNotSatisfyExpectations {
    @RegisterExtension
    JUnit5Mockery context = new JUnit5Mockery();
    
    @Mock
    private Runnable runnable;
    
    @Test
    public void doesNotSatisfyExpectations() {
        context.checking(new Expectations() {{
            oneOf (runnable).run();
        }});
        
        // Return without satisfying the expectation for runnable.run()
    }
}
