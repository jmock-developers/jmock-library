package test.jmock.core.constraint;

import org.jmock.core.constraint.IsGreaterThan;


public class IsGreaterThanTest extends ConstraintsTest
{
    public void testEvaluatesToTrueIfArgumentIsGreaterThanAComparableObject() {
        IsGreaterThan c = new IsGreaterThan(new Integer(1));

        assertFalse(c.eval(new Integer(0)));
        assertFalse(c.eval(new Integer(1)));
        assertTrue(c.eval(new Integer(2)));
    }
}
