package test.jmock.builder;

import org.jmock.Verifiable;
import org.jmock.builder.ExpectationBuilder;
import org.jmock.dynamic.InvocationMatcher;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.expectation.ExpectationValue;
import org.jmock.util.Verifier;


public class MockExpectationBuilder 
    implements ExpectationBuilder, Verifiable
{
    private String name;
    
    public MockExpectationBuilder() {
    	this("mockExpectationBuilder");
    }
    
    public MockExpectationBuilder(String name) {
       this.name = name;
    }
    
    
    ExpectationValue addedExpectationType = new ExpectationValue("added expectation type");
    
	public ExpectationBuilder addExpectation(InvocationMatcher expectation) {
        addedExpectationType.setActual(expectation.getClass());
		return this;
	}
	
    
    ExpectationCounter expectOnceCalls = new ExpectationCounter("expectOnce #calls");
    
	public ExpectationBuilder expectOnce() {
		expectOnceCalls.inc();
        return this;
	}
	
    ExpectationValue expectAfterPreviousCall = new ExpectationValue("expectAfter previousCall");
    
	public ExpectationBuilder expectAfter(ExpectationBuilder previousCall) {
        expectAfterPreviousCall.setExpected(previousCall);
		return this;
	}
    
    public void verify() {
    	Verifier.verifyObject(this);
    }
}
