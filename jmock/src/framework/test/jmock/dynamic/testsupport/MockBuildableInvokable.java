package test.jmock.dynamic.testsupport;

import org.jmock.dynamic.BuildableInvokable;
import org.jmock.dynamic.InvocationMatcher;
import org.jmock.dynamic.Stub;
import org.jmock.expectation.ExpectationSet;
import org.jmock.expectation.ExpectationValue;


public class MockBuildableInvokable
	extends MockInvokable
	implements BuildableInvokable 
{
	private String name;
	
	public MockBuildableInvokable( String name ) {
		this.name = name;
	}
	
	public MockBuildableInvokable() {
		this("mockBuildableInvokable");
	}
	
	public String toString() {
		return name;
	}
	
	public ExpectationSet addedMatchers = new ExpectationSet("added matchers");
	
	public void addMatcher(InvocationMatcher matcher) {
		addedMatchers.addActual(matcher);
	}
	
	public ExpectationValue setStub = new ExpectationValue("set stub");
	
	public void setStub(Stub stub) {
		setStub.setActual(stub);
	}
}
