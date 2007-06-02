/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.calculator.expression;

import org.jmock.examples.calculator.Environment;
import org.jmock.examples.calculator.Expression;


public class Literal implements Expression
{
    private double value;

    public Literal( double value ) {
        this.value = value;
    }

    public double evaluate( Environment environment ) {
        return value;
    }
}
