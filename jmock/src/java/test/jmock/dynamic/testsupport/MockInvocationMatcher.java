package test.jmock.dynamic.testsupport;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.InvocationMatcher;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.expectation.ExpectationValue;
import org.jmock.expectation.Verifier;


public class MockInvocationMatcher 
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
	
	public boolean matches(Invocation invocation) {
		matchesInvocation.setActual(invocation);
		return matchesResult;
	}

	public ExpectationValue invokedInvocation = new ExpectationValue("invoked invocation");
	
	public void invoked(Invocation invocation) {
		invokedInvocation.setActual(invocation);
	}
	
	public ExpectationValue writeToBuffer = new ExpectationValue("writeTo buffer");
	public String writeToOutput = "";
	
	public StringBuffer writeTo(StringBuffer buffer) {
		writeToBuffer.setActual(buffer);
		buffer.append(writeToOutput);
		return buffer;
	}
	
	public ExpectationCounter verifyCalls = new ExpectationCounter("verify #calls");
	
	public void verify() {
		verifyCalls.inc();
	}
	
	public void verifyExpectations() {
		Verifier.verifyObject(this);
	}
}
