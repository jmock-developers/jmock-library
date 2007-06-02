/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.calculator.expression;

import org.jmock.examples.calculator.CalculatorException;
import org.jmock.examples.calculator.Expression;


public class Division extends BinaryOperator
{

    public Division( Expression left, Expression right ) {
        super(left, right);
    }

    protected double operator( double left, double right )
            throws CalculatorException {
        if (right == 0.0) {
            throw new CalculatorException("divide by zero");
        }
        return left / right;
    }
}