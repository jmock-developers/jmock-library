package test.jmock.constraint;

import org.jmock.constraint.And;



public class AndTest extends ConstraintsTest {
    public void testEvaluatesToTheTheLogicalConjunctionOfTwoOtherConstraints() {
        assertTrue( new And(TRUE_CONSTRAINT,  TRUE_CONSTRAINT) .eval(ARGUMENT_IGNORED));
        assertFalse(new And(FALSE_CONSTRAINT, TRUE_CONSTRAINT) .eval(ARGUMENT_IGNORED));
        assertFalse(new And(TRUE_CONSTRAINT,  FALSE_CONSTRAINT).eval(ARGUMENT_IGNORED));
        assertFalse(new And(FALSE_CONSTRAINT, FALSE_CONSTRAINT).eval(ARGUMENT_IGNORED));
    }
    
    public void testEvaluatesArgumentsLeftToRightAndShortCircuitsEvaluation() {
        assertFalse( new And(FALSE_CONSTRAINT,NEVER_EVALUATED).eval(ARGUMENT_IGNORED) );
    }
}
