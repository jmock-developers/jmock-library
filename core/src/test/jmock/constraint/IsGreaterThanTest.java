package test.jmock.constraint;

import org.jmock.constraint.IsGreaterThan;


public class IsGreaterThanTest extends ConstraintsTest {
    public void testEvaluatesToTrueIfArgumentIsGreaterThanAComparableObject() {
        IsGreaterThan c = new IsGreaterThan(new Integer(1));

        assertFalse(c.eval(new Integer(0)));
        assertFalse(c.eval(new Integer(1)));
        assertTrue(c.eval(new Integer(2)));
    }
}
