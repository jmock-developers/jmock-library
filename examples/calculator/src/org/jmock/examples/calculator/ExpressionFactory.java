/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.calculator;


public interface ExpressionFactory
{
    Expression newLiteral( double value );

    Expression newAddition( Expression left, Expression right );

    Expression newSubtraction( Expression left, Expression right );

    Expression newMultiplication( Expression left, Expression right );

    Expression newDivision( Expression left, Expression right );

    Expression newPower( Expression left, Expression right );

    Expression newVariableReference( String variableName );
}
