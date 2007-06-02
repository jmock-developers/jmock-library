/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.calculator.expression;

import org.jmock.examples.calculator.CalculatorException;
import org.jmock.examples.calculator.Environment;
import org.jmock.examples.calculator.Expression;


public abstract class BinaryOperator implements Expression
{

    private Expression left, right;

    protected BinaryOperator( Expression left, Expression right ) {
        this.left = left;
        this.right = right;
    }

    public double evaluate( Environment environment )
            throws CalculatorException {
        return operator(left.evaluate(environment),
                        right.evaluate(environment));
    }

    protected abstract double operator( double leftArg, double rightArg )
            throws CalculatorException;
}
