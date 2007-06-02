/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.calculator;

import java.util.HashMap;


public class SimpleEnvironment implements Environment
{
    public HashMap variables = new HashMap();


    public Expression getVariable( String name ) throws CalculatorException {
        if (variables.containsKey(name)) {
            return (Expression)variables.get(name);
        } 
        throw new CalculatorException("variable \"" + name + "\" not defined");
    }

    public void setVariable( String name, Expression expression ) {
        variables.put(name, expression);
    }
}
