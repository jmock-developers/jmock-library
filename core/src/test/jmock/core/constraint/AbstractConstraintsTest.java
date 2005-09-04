/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.constraint;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.core.Constraint;
import org.jmock.core.constraint.IsAnything;
import org.jmock.core.constraint.IsNothing;


public abstract class AbstractConstraintsTest extends TestCase
{
    protected static final Constraint TRUE_CONSTRAINT = new IsAnything("always true");
    protected static final Constraint FALSE_CONSTRAINT = new IsNothing("always false");

    protected static final Constraint NEVER_EVALUATED = new Constraint()
    {
        public boolean eval( Object o ) {
            throw new AssertionFailedError("constraint should not have been evaluated");
        }

        public StringBuffer describeTo( StringBuffer buffer ) {
            return buffer.append("NEVER_EVALUATED");
        }
    };

    protected static final Object ARGUMENT_IGNORED = new Object();
    protected static final Object ANY_NON_NULL_ARGUMENT = new Object();
    
    public void assertMatches(String message, Constraint c, Object arg) {
        assertTrue(message, c.eval(arg));   
    }

    public void assertDoesNotMatch(String message, Constraint c, Object arg) {
        assertFalse(message, c.eval(arg));   
    }
}
