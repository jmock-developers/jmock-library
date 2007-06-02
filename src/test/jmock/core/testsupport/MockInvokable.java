/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.testsupport;

import junit.framework.AssertionFailedError;
import org.jmock.core.Invocation;
import org.jmock.core.Invokable;
import org.jmock.expectation.ExpectationValue;


public class MockInvokable extends MockVerifiable implements Invokable
{

    public boolean matchesResult;
    public ExpectationValue matchesInvocation = new ExpectationValue("matches.invocation");

    public Object invokeResult;
    public ExpectationValue invokeInvocation = new ExpectationValue("invoke.invocation");
    public Throwable invokeThrow;


    public boolean matches( Invocation invocation ) {
        matchesInvocation.setActual(invocation);
        return matchesResult;
    }

    public Object invoke( Invocation invocation ) throws Throwable {
        invokeInvocation.setActual(invocation);
        if (invokeThrow != null) {
            throw invokeThrow;
        }
        return invokeResult;
    }

    public boolean hasDescription() {
        return true;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        throw new AssertionFailedError("should implement describeTo");
    }
}
