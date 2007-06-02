/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.constraint;

import org.jmock.core.Constraint;


/**
 * Is the value a number equal to a value within some range of
 * acceptable error?
 */
public class IsCloseTo implements Constraint
{
    private double error;
    private double value;

    public IsCloseTo( double value, double error ) {
        this.error = error;
        this.value = value;
    }

    public boolean eval( Object arg ) {
        double argValue = ((Number)arg).doubleValue();
        return Math.abs((argValue - value)) <= error;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append("a numeric value within ")
                .append(error)
                .append(" of ")
                .append(value);
    }
}
