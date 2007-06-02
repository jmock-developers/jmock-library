/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.calculator;

import org.jmock.examples.calculator.expression.*;


public class SimpleExpressionFactory implements ExpressionFactory
{
    public Expression newLiteral( double value ) {
        return new Literal(value);
    }

    public Expression newAddition( Expression left, Expression right ) {
        return new Addition(left, right);
    }

    public Expression newSubtraction( Expression left, Expression right ) {
        return new Subtraction(left, right);
    }

    public Expression newMultiplication( Expression left, Expression right ) {
        return new Multiplication(left, right);
    }

    public Expression newDivision( Expression left, Expression right ) {
        return new Division(left, right);
    }

    public Expression newPower( Expression left, Expression right ) {
        return new Power(left, right);
    }

    public Expression newVariableReference( String variableName ) {
        return new VariableReference(variableName);
    }
}
