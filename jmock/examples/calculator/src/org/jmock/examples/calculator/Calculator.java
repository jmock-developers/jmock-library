/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.calculator;

public class Calculator
{
    private Parser parser;
    private Environment environment;

    public Calculator() {
        environment = new SimpleEnvironment();
        parser = new InfixParser();
    }

    public Calculator( Parser parser, Environment environment ) {
        this.parser = parser;
        this.environment = environment;
    }

    public double calculate( String expressionString ) throws CalculatorException {
        Expression expression = parser.parse(expressionString);
        return expression.evaluate(environment);
    }

    public void setVariable( String name, String valueString ) throws CalculatorException {
        environment.setVariable(name, parser.parse(valueString));
    }
}
