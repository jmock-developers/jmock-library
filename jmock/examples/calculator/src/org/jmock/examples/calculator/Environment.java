/* Copyright Dec 6, 2003 Nat Pryce, all rights reserved.
 */
package org.jmock.examples.calculator;


public interface Environment
{

	Expression getVariable( String name ) throws CalculatorException;

	void setVariable( String name, Expression expression );

}
