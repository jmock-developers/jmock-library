/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.testsupport;

import org.jmock.core.Invocation;
import org.jmock.core.InvocationMatcher;
import org.jmock.expectation.ExpectationValue;


public class MockInvocationMatcher
        extends MockVerifiable
        implements InvocationMatcher
{
    private String name;

    public MockInvocationMatcher( String name ) {
        this.name = name;
    }

    public MockInvocationMatcher() {
        this("mockInvocationMatcher");
    }

    public String toString() {
        return name;
    }


    public ExpectationValue matchesInvocation = new ExpectationValue("matches invocation");
    public boolean matchesResult = false;

    public boolean matches( Invocation invocation ) {
        matchesInvocation.setActual(invocation);
        return matchesResult;
    }

    public ExpectationValue invokedInvocation = new ExpectationValue("invoked invocation");

    public void invoked( Invocation invocation ) {
        invokedInvocation.setActual(invocation);
    }

    public ExpectationValue describeToBuffer = new ExpectationValue("describeTo buffer");
    public String describeToOutput = "";

    public boolean hasDescription() {
        return describeToOutput.length() > 0;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        describeToBuffer.setActual(buffer);
        buffer.append(describeToOutput);
        return buffer;
    }
}
