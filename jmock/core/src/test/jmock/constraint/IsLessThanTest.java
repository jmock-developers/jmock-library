package test.jmock.constraint;

import org.jmock.constraint.IsLessThan;


public class IsLessThanTest extends ConstraintsTest {
    public void testEvaluatesToTrueIfArgumentIsLessThanAComparableObject() {
        IsLessThan c = new IsLessThan(new Integer(1));
        
        assertTrue(c.eval(new Integer(0)));
        assertFalse(c.eval(new Integer(1)));
        assertFalse(c.eval(new Integer(2)));
    }
}
