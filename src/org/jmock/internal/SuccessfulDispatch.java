/**
 * 
 */
package org.jmock.internal;

import org.jmock.api.Expectation;
import org.jmock.api.Invocation;

public class SuccessfulDispatch {
    private final Invocation invocation;
    private final Expectation expectation;
    public final Object result;

    public SuccessfulDispatch(Invocation invocation, Expectation expectation, Object result) {
        this.invocation = invocation;
        this.expectation = expectation;
        this.result = result;
    }
}