package test.jmock.core.constraint;

import org.jmock.core.constraint.IsNull;


public class IsNullTest extends ConstraintsTest
{
    public void testEvaluatesToTrueIfArgumentIsNull() {
        IsNull isNull = new IsNull();

        assertTrue(isNull.eval(null));
        assertFalse(isNull.eval(ANY_NON_NULL_ARGUMENT));
    }
}
