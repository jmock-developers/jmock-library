/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.calculator.expression;

import org.jmock.examples.calculator.CalculatorException;
import org.jmock.examples.calculator.Environment;
import org.jmock.examples.calculator.Expression;


public class VariableReference implements Expression
{

    private String variableName;

    public VariableReference( String variableName ) {
        this.variableName = variableName;
    }

    public double evaluate( Environment environment )
            throws CalculatorException {
        return environment.getVariable(variableName).evaluate(environment);
    }
}
