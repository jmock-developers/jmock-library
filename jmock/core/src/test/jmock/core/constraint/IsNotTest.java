/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.constraint;

import org.jmock.core.constraint.IsNot;


public class IsNotTest extends ConstraintsTest
{
    public void testEvaluatesToTheTheLogicalNegationOfAnotherConstraint() {
        assertFalse(new IsNot(TRUE_CONSTRAINT).eval(ARGUMENT_IGNORED));
        assertTrue(new IsNot(FALSE_CONSTRAINT).eval(ARGUMENT_IGNORED));
    }
}
