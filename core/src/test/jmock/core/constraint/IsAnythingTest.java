package test.jmock.core.constraint;

import org.jmock.core.constraint.IsAnything;


public class IsAnythingTest extends ConstraintsTest
{
    public void testAlwaysEvaluatesToTrue() {
        IsAnything isAnything = new IsAnything();

        assertTrue(isAnything.eval(null));
        assertTrue(isAnything.eval(new Object()));
    }


}
