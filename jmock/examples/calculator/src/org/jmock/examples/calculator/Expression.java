/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.calculator;


public interface Expression
{
    double evaluate( Environment environment ) throws CalculatorException;
}
