/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.expectation;


public class ExpectationSegment extends AbstractExpectation
{
    private String myExpectedSegment;
    private String myActualString;

    public ExpectationSegment( String name ) {
        super(name);
        clearActual();
    }

    public void clearActual() {
        myActualString = null;
    }

    public void setActual( String aString ) {
        myActualString = aString;
        if (shouldCheckImmediately()) {
            verify();
        }
    }

    public void setExpected( String segment ) {
        myExpectedSegment = segment;
        setHasExpectations();
    }

    public void setExpectNothing() {
        myActualString = null;
        setExpected(null);
    }

    public void verify() {
        if (hasExpectations()) {
            if (null == myExpectedSegment) {
                AssertMo.assertNull("Expecting nothing", myActualString);
            } else {
                AssertMo.assertIncludes("Should include string segment",
                                        myExpectedSegment,
                                        myActualString);
            }
        }
    }
}
