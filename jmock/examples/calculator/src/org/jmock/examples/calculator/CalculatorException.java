/* Copyright Dec 6, 2003 Nat Pryce, all rights reserved.
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
