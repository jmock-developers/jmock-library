package test.jmock.dynamic.testsupport;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.InvocationMatcher;
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
	
    public boolean hasDescription() {
        return writeToOutput.length() > 0;
    }
    
	public StringBuffer describeTo(StringBuffer buffer) {
		writeToBuffer.setActual(buffer);
		buffer.append(writeToOutput);
		return buffer;
	}
}
