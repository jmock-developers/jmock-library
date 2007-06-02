/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core;

import java.util.List;
import org.jmock.core.InvocationMocker;
import org.jmock.core.Stub;
import org.jmock.core.Verifiable;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.expectation.ExpectationList;
import org.jmock.expectation.ExpectationValue;
import org.jmock.util.Verifier;


public class MockDescriber implements InvocationMocker.Describer, Verifiable
{
    public ExpectationCounter hasDescriptionCalls =
            new ExpectationCounter("hasDescription #calls");
    public boolean hasDescriptionResult = false;

    public boolean hasDescription() {
        hasDescriptionCalls.inc();
        return hasDescriptionResult;
    }

    public ExpectationValue describeToBuf =
            new ExpectationValue("describeTo buf");
    public ExpectationList describeToMatchers =
            new ExpectationList("describeTo matchers");
    public ExpectationValue describeToStub =
            new ExpectationValue("describeTo stub");
    public ExpectationValue describeToName =
            new ExpectationValue("describeTo name");

    public void describeTo( StringBuffer buf, List matchers, Stub stub, String name ) {
        describeToBuf.setActual(buf);
        describeToMatchers.addActualMany(matchers.iterator());
        describeToStub.setActual(stub);
        describeToName.setActual(name);
    }

    public void verify() {
        Verifier.verifyObject(this);
    }
}
