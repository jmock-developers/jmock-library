/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.calculator.expression;

import org.jmock.examples.calculator.Expression;


public class Power extends BinaryOperator
{

    public Power( Expression left, Expression right ) {
        super(left, right);
    }

    protected double operator( double left, double right ) {
        return Math.pow(left, right);
    }
}
