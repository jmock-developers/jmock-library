package test.jmock.builder.testsupport;

import org.jmock.Verifiable;
import org.jmock.builder.BuilderIdentityTable;
import org.jmock.builder.IdentityBuilder;
import org.jmock.builder.MatchBuilder;
import org.jmock.dynamic.InvocationMatcher;
import org.jmock.dynamic.Stub;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.expectation.ExpectationValue;
import org.jmock.util.Verifier;


public class MockMatchBuilder
    implements MatchBuilder, Verifiable
{
    ExpectationValue addedExpectationType = new ExpectationValue("added expectation type");
    
    public IdentityBuilder isVoid() {
        // TODO Auto-generated method stub
        throw new NoSuchMethodError("isVoid");
    }
    public MatchBuilder match(InvocationMatcher customMatcher) {
        // TODO Auto-generated method stub
        throw new NoSuchMethodError("match");
    }
    public IdentityBuilder stub(Stub customStub) {
        // TODO Auto-generated method stub
        throw new NoSuchMethodError("stub");
    }
    public IdentityBuilder will(Stub stubAction) {
        // TODO Auto-generated method stub
        throw new NoSuchMethodError("will");
    }
    public IdentityBuilder willReturn(boolean result) {
        // TODO Auto-generated method stub
        throw new NoSuchMethodError("willReturn");
    }
    public IdentityBuilder willReturn(byte result) {
        // TODO Auto-generated method stub
        throw new NoSuchMethodError("willReturn");
    }
    public IdentityBuilder willReturn(char result) {
        // TODO Auto-generated method stub
        throw new NoSuchMethodError("willReturn");
    }
    public IdentityBuilder willReturn(double result) {
        // TODO Auto-generated method stub
        throw new NoSuchMethodError("willReturn");
    }
    public IdentityBuilder willReturn(float result) {
        // TODO Auto-generated method stub
        throw new NoSuchMethodError("willReturn");
    }
    public IdentityBuilder willReturn(int result) {
        // TODO Auto-generated method stub
        throw new NoSuchMethodError("willReturn");
    }
    public IdentityBuilder willReturn(long result) {
        // TODO Auto-generated method stub
        throw new NoSuchMethodError("willReturn");
    }
    public IdentityBuilder willReturn(Object result) {
        // TODO Auto-generated method stub
        throw new NoSuchMethodError("willReturn");
    }
    public IdentityBuilder willReturn(short result) {
        // TODO Auto-generated method stub
        throw new NoSuchMethodError("willReturn");
    }
    public IdentityBuilder willThrow(Throwable throwable) {
        // TODO Auto-generated method stub
        throw new NoSuchMethodError("willThrow");
    }
    
    public IdentityBuilder expect(InvocationMatcher expectation) {
        addedExpectationType.setActual(expectation.getClass());
        return this;
    }
    
	public IdentityBuilder addExpectation(InvocationMatcher expectation) {
        addedExpectationType.setActual(expectation.getClass());
		return this;
	}
	
    
    ExpectationCounter expectOnceCalls = new ExpectationCounter("expectOnce #calls");
    
	public IdentityBuilder expectOnce() {
		expectOnceCalls.inc();
        return this;
	}
	
	ExpectationCounter expectAtLeastOnceCalls = new ExpectationCounter("expectAtLeastOnce #calls");
	
	public IdentityBuilder expectAtLeastOnce() {
		expectAtLeastOnceCalls.inc();
		return this;
	}
	
	ExpectationValue expectAfterPreviousCall = new ExpectationValue("expectAfter previousCall");
    
	public IdentityBuilder expectAfter(IdentityBuilder previousCall) {
        expectAfterPreviousCall.setExpected(previousCall);
		return this;
	}
    
	
	public ExpectationValue afterMock = new ExpectationValue("after mock");
	public ExpectationValue afterPreviousCallID = new ExpectationValue("after previousCallID");
	
	public MatchBuilder after( String previousCallID ) {
		afterPreviousCallID.setActual(previousCallID);
		return this;
	}
	
	public MatchBuilder after( BuilderIdentityTable otherMock, String previousCallID ) {
		afterMock.setActual(otherMock);
		return after(previousCallID);
	}
	
	public ExpectationValue id = new ExpectationValue("id");
	
	public void id( String newID ) {
		id.setActual(newID);
	}
    
    public ExpectationCounter expectNotCalledCalls = 
        new ExpectationCounter("expectNotCalled #calls");
    
    public IdentityBuilder expectNotCalled() {
        expectNotCalledCalls.inc();
        return this;
    }
	
	public void verify() {
    	Verifier.verifyObject(this);
    }
    

}
