/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.testsupport;

import junit.framework.AssertionFailedError;
import org.jmock.core.Verifiable;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.util.Verifier;


public class MockVerifiable implements Verifiable
{
    public ExpectationCounter verifyCalls =
            new ExpectationCounter("verify #calls");
    public AssertionFailedError verifyFailure;

    public void setExpectedVerifyCalls( int expectedCalls ) {
        verifyCalls.setExpected(expectedCalls);
    }

    /**
     * @deprecated Use verifyExpectations to verify this object
     */
    public void verify() {
        verifyCalls.inc();
        if (verifyFailure != null) {
            throw verifyFailure;
        }
    }

    public void verifyExpectations() {
        Verifier.verifyObject(this);
    }

}
