/* Copyright Dec 6, 2003 Nat Pryce, all rights reserved.
 */
package org.jmock.examples.calculator;


public interface Parser {

    Expression parse(String expressionString) throws ParseException;

}
