package test.jmock.constraint;

import org.jmock.constraint.IsAnything;


public class IsAnythingTest extends ConstraintsTest {
    public void testAlwaysEvaluatesToTrue() {
        IsAnything isAnything = new IsAnything();
        
        assertTrue( isAnything.eval(null) );
        assertTrue( isAnything.eval(new Object()) );
    }


}
