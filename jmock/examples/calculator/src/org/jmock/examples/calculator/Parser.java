/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.calculator;


public interface Parser
{

    Expression parse( String expressionString ) throws ParseException;

}
