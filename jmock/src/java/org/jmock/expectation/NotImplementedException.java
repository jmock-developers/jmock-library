/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.expectation;

import junit.framework.AssertionFailedError;

public class NotImplementedException extends AssertionFailedError {

    /**
     * NotImplementedException constructor comment.
     */
    public NotImplementedException() {
        super();
    }

    /**
     * NotImplementedException constructor comment.
     * 
     * @param message java.lang.String
     */
    public NotImplementedException(String message) {
        super(message);
    }
}
