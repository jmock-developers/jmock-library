package testdata.jmock.acceptance.junit4;

import org.jmock.Expectations;
import org.junit.Test;

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
