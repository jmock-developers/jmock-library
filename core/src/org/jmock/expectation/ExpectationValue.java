/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.expectation;


public class ExpectationValue extends AbstractExpectation
{
    private Object myExpectedValue;
    private Object myActualValue;

    public ExpectationValue( String name ) {
        super(name);
        clearActual();
    }

    public void clearActual() {
        myActualValue = new Null("Nothing");
    }

    public void setActual( int aValue ) {
        setActual(new Integer(aValue));
    }

    public void setActual( long aValue ) {
        setActual(new Long(aValue));
    }

    public void setActual( double aValue ) {
        setActual(new Double(aValue));
    }

    public void setActual( Object aValue ) {
        myActualValue = aValue;
        if (shouldCheckImmediately()) {
            verify();
        }
    }

    public void setActual( boolean aValue ) {
        setActual(new Boolean(aValue));
    }

    public void setExpected( int aValue ) {
        setExpected(new Integer(aValue));
    }

    public void setExpected( long aValue ) {
        setExpected(new Long(aValue));
    }

    public void setExpected( double aValue ) {
        setExpected(new Double(aValue));
    }

    public void setExpected( Object aValue ) {
        myExpectedValue = aValue;
        setHasExpectations();
    }

    public void setExpected( boolean aValue ) {
        setExpected(new Boolean(aValue));
    }

    public void setExpectNothing() {
        setExpected(new Null("Nothing"));
        myActualValue = myExpectedValue;
    }

    public void verify() {
        assertEquals("did not receive the expected Value.",
                     myExpectedValue,
                     myActualValue);
    }
}
