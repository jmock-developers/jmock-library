/* Copyright Dec 7, 2003 Nat Pryce, all rights reserved.
 */
package org.jmock.examples.calculator;

import java.util.HashMap;


public class SimpleEnvironment implements Environment
{
	public HashMap variables = new HashMap();


	public Expression getVariable( String name ) throws CalculatorException {
		if (variables.containsKey(name)) {
			return (Expression)variables.get(name);
		} else {
			throw new CalculatorException("variable \"" + name + "\" not defined");
		}
	}

	public void setVariable( String name, Expression expression ) {
		variables.put(name, expression);
	}
}
