package test.jmock.dynamic.testsupport;

import org.jmock.dynamic.framework.Invocation;
import org.jmock.dynamic.framework.Stub;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.expectation.ExpectationValue;
import org.jmock.expectation.Verifiable;
import org.jmock.expectation.Verifier;


public class MockStub 
	implements Stub, Verifiable 
{
	private String name;
	
	public MockStub() {
		this("mockStub");
	}
	
	public MockStub( String name ) {
		this.name = name;
	}
	
	public ExpectationValue invokeInvocation = 
		new ExpectationValue("invoke invocation");
	public Object invokeResult;
	
	public Object invoke(Invocation invocation) throws Throwable {
		invokeInvocation.setActual(invocation);
		return invokeResult;
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
