package test.jmock.core.constraint;

import org.jmock.core.constraint.IsLessThan;


public class IsLessThanTest extends ConstraintsTest
{
    public void testEvaluatesToTrueIfArgumentIsLessThanAComparableObject() {
        IsLessThan c = new IsLessThan(new Integer(1));

        assertTrue(c.eval(new Integer(0)));
        assertFalse(c.eval(new Integer(1)));
        assertFalse(c.eval(new Integer(2)));
    }
}
