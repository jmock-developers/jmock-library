/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.calculator;


public interface Environment
{

    Expression getVariable( String name ) throws CalculatorException;

    void setVariable( String name, Expression expression );

}
