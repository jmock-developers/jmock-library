/* Copyright Dec 6, 2003 Nat Pryce, all rights reserved.
 */
package org.jmock.examples.calculator;


public class ParseException extends CalculatorException
{
	public ParseException() {
		super();
	}

	public ParseException( String message ) {
		super(message);
	}

	public ParseException( String message, Throwable cause ) {
		super(message, cause);
	}

	public ParseException( Throwable cause ) {
		super(cause);
	}
}
