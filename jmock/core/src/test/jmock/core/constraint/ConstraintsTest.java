/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.core.constraint;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.core.Constraint;
import test.jmock.core.testsupport.AlwaysFalse;
import test.jmock.core.testsupport.AlwaysTrue;


public abstract class ConstraintsTest extends TestCase
{
    protected static final Constraint TRUE_CONSTRAINT = AlwaysTrue.INSTANCE;
    protected static final Constraint FALSE_CONSTRAINT = AlwaysFalse.INSTANCE;

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
}
