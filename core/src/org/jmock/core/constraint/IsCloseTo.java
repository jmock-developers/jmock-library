/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core.constraint;

import org.jmock.core.Constraint;

/**
 * Is the value a number equal to a value within some range of
 * acceptable error?
 */
public class IsCloseTo implements Constraint {
    private double error;
    private double value;

    public IsCloseTo(double value, double error) {
        this.error = error;
        this.value = value;
    }

    public boolean eval(Object arg) {
        double argValue = ((Number)arg).doubleValue();
        return Math.abs((argValue - value)) <= error;
    }

    public String toString() {
        return "a numeric value within " + error + " of " + value;
    }
}
