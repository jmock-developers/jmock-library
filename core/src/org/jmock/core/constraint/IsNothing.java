package org.jmock.core.constraint;

import org.jmock.core.Constraint;


public class IsNothing implements Constraint
{
    public static final IsNothing INSTANCE = new IsNothing();
    private String description;

    public IsNothing() {
        this("NOTHING");
    }

    public IsNothing( String description ) {
        this.description = description;
    }

    public boolean eval( Object o ) {
        return false;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append(description);
    }
}
