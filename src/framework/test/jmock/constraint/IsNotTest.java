package test.jmock.constraint;

import org.jmock.constraint.IsNot;


public class IsNotTest extends ConstraintsTest {
    public void testEvaluatesToTheTheLogicalNegationOfAnotherConstraint() {
        assertFalse( new IsNot(TRUE_CONSTRAINT).eval(ARGUMENT_IGNORED) );
        assertTrue(  new IsNot(FALSE_CONSTRAINT).eval(ARGUMENT_IGNORED) );
    }
}
