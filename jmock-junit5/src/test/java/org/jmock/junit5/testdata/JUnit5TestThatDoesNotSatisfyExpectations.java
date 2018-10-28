package org.jmock.junit5.testdata;

import org.jmock.Expectations;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.extension.RegisterExtension;

public class JUnit5TestThatDoesNotSatisfyExpectations {
    @RegisterExtension
    JUnit5Mockery context = new JUnit5Mockery();
    private Runnable runnable = context.mock(Runnable.class);
    
    @org.junit.jupiter.api.Test
    public void doesNotSatisfyExpectations() {
        context.checking(new Expectations() {{
            oneOf (runnable).run();
        }});
        
        // Return without satisfying the expectation for runnable.run()
    }
}
