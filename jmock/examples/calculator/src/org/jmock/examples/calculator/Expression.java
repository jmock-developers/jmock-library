/* Copyright Dec 6, 2003 Nat Pryce, all rights reserved.
 */
package org.jmock.examples.calculator;


public interface Expression {
    double evaluate( Environment environment ) throws CalculatorException;
}
