/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.testsupport;

import org.jmock.core.Constraint;


public class AlwaysFalse implements Constraint
{
    public static AlwaysFalse INSTANCE = new AlwaysFalse();

    public boolean eval( Object o ) {
        return false;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append("always false");
    }
}
