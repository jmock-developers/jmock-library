package org.jmock.junit5.testdata;

import org.jmock.Expectations;
import org.junit.jupiter.api.Test;

public class DerivedJUnit4TestThatDoesNotSatisfyExpectations extends BaseClassWithMockery {
    private Runnable runnable = context.mock(Runnable.class);
    
    @Test
    public void doesNotSatisfyExpectations() {
        context.checking(new Expectations() {{
            oneOf (runnable).run();
        }});
        
        // Return without satisfying the expectation for runnable.run()
    }
}
