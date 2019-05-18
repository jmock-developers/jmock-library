package org.jmock.junit5.testdata.jmock.acceptance;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.jupiter.api.Test;

public class DerivedJUnit5TestThatDoesNotSatisfyExpectations extends BaseClassWithMockery {
    
    @Mock
    Runnable runnable;
    
    @Test
    public void doesNotSatisfyExpectations() {
        context.checking(new Expectations() {{
            oneOf (runnable).run();
        }});
        
        // Return without satisfying the expectation for runnable.run()
    }
}
