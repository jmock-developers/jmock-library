package test.jmock.core.testsupport;

import org.jmock.core.Invocation;
import org.jmock.core.Stub;
import org.jmock.core.Verifiable;
import org.jmock.expectation.ExpectationValue;
import org.jmock.util.Verifier;


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

	public String toString() {
		return name;
	}

	public ExpectationValue invokeInvocation =
	        new ExpectationValue("invoke invocation");
	public Object invokeResult;

	public Object invoke( Invocation invocation ) throws Throwable {
		invokeInvocation.setActual(invocation);
		return invokeResult;
	}


	public ExpectationValue describeToBuffer = new ExpectationValue("describeTo buffer");
	public String describeToOutput = "";

	public StringBuffer describeTo( StringBuffer buffer ) {
		describeToBuffer.setActual(buffer);
		buffer.append(describeToOutput);
		return buffer;
	}

	public void verify() {
		Verifier.verifyObject(this);
	}
}
