package test.jmock.dynamic.testsupport;

import junit.framework.AssertionFailedError;

import org.jmock.Verifiable;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.util.Verifier;

public class MockVerifiable implements Verifiable {
	public ExpectationCounter verifyCalls = 
        new ExpectationCounter("verify #calls");
	public AssertionFailedError verifyFailure;
	
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
