/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.testsupport;

import org.jmock.core.Constraint;


public class AlwaysTrue implements Constraint
{
    public static AlwaysTrue INSTANCE = new AlwaysTrue();

    public boolean eval( Object o ) {
        return true;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append("always true");
    }
}
