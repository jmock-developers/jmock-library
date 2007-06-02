/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.constraint;

import org.jmock.core.constraint.And;


public class AndTest extends AbstractConstraintsTest
{
    public void testEvaluatesToTheTheLogicalConjunctionOfTwoOtherConstraints() {
        assertTrue(new And(TRUE_CONSTRAINT, TRUE_CONSTRAINT).eval(ARGUMENT_IGNORED));
        assertFalse(new And(FALSE_CONSTRAINT, TRUE_CONSTRAINT).eval(ARGUMENT_IGNORED));
        assertFalse(new And(TRUE_CONSTRAINT, FALSE_CONSTRAINT).eval(ARGUMENT_IGNORED));
        assertFalse(new And(FALSE_CONSTRAINT, FALSE_CONSTRAINT).eval(ARGUMENT_IGNORED));
    }

    public void testEvaluatesArgumentsLeftToRightAndShortCircuitsEvaluation() {
        assertFalse(new And(FALSE_CONSTRAINT, NEVER_EVALUATED).eval(ARGUMENT_IGNORED));
    }
}
