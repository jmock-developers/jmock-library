/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.expectation;


public class ExpectationDoubleValue extends AbstractExpectation
{
    private Double expectedValue = null;
    private double expectedError = 0.0;
    private boolean expectNothing = false;
    private Double actualValue = null;


    public ExpectationDoubleValue( String name ) {
        super(name);
        clearActual();
    }

    public void clearActual() {
        actualValue = null;
    }

    public void setActual( double value ) {
        actualValue = new Double(value);
        if (shouldCheckImmediately()) {
            verify();
        }
    }

    public void setExpected( double value, double error ) {
        expectedValue = new Double(value);
        expectedError = Math.abs(error);
        setHasExpectations();
    }

    public void setExpectNothing() {
        expectNothing = true;
        clearActual();
        setHasExpectations();
    }

    public void verify() {
        if (expectNothing) {
            AssertMo.assertNull(myName + " expected no value",
                                actualValue);

        } else if (expectedValue != null) {
            AssertMo.assertNotNull(myName + " expected a value",
                                   actualValue);

            double actualError = Math.abs(actualValue.doubleValue() - expectedValue.doubleValue());

            AssertMo.assertTrue(myName + " expected a value within " + expectedError +
                                " of " + expectedValue + ", was " + actualValue,
                                actualError <= expectedError);
        }
    }
}
