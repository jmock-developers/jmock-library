/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.calculator;


public class CalculatorException extends Exception
{
    public CalculatorException() {
        super();
    }

    public CalculatorException( String message ) {
        super(message);
    }

    public CalculatorException( String message, Throwable cause ) {
        super(message, cause);
    }

    public CalculatorException( Throwable cause ) {
        super(cause);
    }
}
