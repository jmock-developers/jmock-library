package test.jmock.core.constraint;

import org.jmock.core.constraint.IsInstanceOf;


public class IsInstanceOfTest extends ConstraintsTest
{
    public void testEvaluatesToTrueIfArgumentIsInstanceOfASpecificClass() {
        IsInstanceOf isInstanceOf = new IsInstanceOf(Number.class);

        assertTrue(isInstanceOf.eval(new Integer(1)));
        assertTrue(isInstanceOf.eval(new Double(1.0)));
        assertFalse(isInstanceOf.eval("a string"));
        assertFalse(isInstanceOf.eval(null));
    }
}
