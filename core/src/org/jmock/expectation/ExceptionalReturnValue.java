/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.expectation;

/**
 * Sequence of exception values as required by MockMaker
 * This is a generic class that should have been introduced to the mockobjects code stream instead of
 * being separately included in org.mockobjects.
 * It is possibly similar to a ReturnObjectList?
 */

public class ExceptionalReturnValue
{
    private Throwable exception;

    public ExceptionalReturnValue( Throwable exception ) {
        this.exception = exception;
    }

    public Throwable getException() {
        return exception;
    }

}
