/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.constraint;

import org.jmock.Constraint;

/**
 * Is the value a number equal to a value within some range of
 * acceptable error?
 */
public class IsCloseTo implements Constraint {
    private double _error;
    private double _value;

    public IsCloseTo(double value, double error) {
        _error = error;
        _value = value;
    }

    public boolean eval(Object arg) {
        double arg_value = ((Number) arg).doubleValue();
        return Math.abs((arg_value - _value)) <= _error;
    }

    public String toString() {
        return "a numeric value within " + _error + " of " + _value;
    }
}
