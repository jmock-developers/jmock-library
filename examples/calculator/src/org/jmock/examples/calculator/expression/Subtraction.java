/* Copyright Dec 7, 2003 Nat Pryce, all rights reserved.
 */
package org.jmock.examples.calculator.expression;

import org.jmock.examples.calculator.Expression;


public class Subtraction extends BinaryOperator
{

	public Subtraction( Expression left, Expression right ) {
		super(left, right);
	}

	protected double operator( double left, double right ) {
		return left - right;
	}
}
