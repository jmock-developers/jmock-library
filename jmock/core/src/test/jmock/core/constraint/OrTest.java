/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.constraint;

import org.jmock.core.constraint.Or;


public class OrTest extends ConstraintsTest
{
    public void testEvaluatesToTheTheLogicalDisjunctionOfTwoOtherConstraint() {
        assertTrue(new Or(TRUE_CONSTRAINT, TRUE_CONSTRAINT).eval(ARGUMENT_IGNORED));
        assertTrue(new Or(FALSE_CONSTRAINT, TRUE_CONSTRAINT).eval(ARGUMENT_IGNORED));
        assertTrue(new Or(TRUE_CONSTRAINT, FALSE_CONSTRAINT).eval(ARGUMENT_IGNORED));
        assertFalse(new Or(FALSE_CONSTRAINT, FALSE_CONSTRAINT).eval(ARGUMENT_IGNORED));
    }


    public void testEvaluatesArgumentsLeftToRightAndShortCircuitsEvaluation() {
        assertTrue(new Or(TRUE_CONSTRAINT, NEVER_EVALUATED).eval(ARGUMENT_IGNORED));
    }
}
