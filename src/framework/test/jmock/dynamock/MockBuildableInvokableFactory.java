package test.jmock.dynamock;

import org.jmock.Constraint;
import org.jmock.Verifiable;
import org.jmock.dynamic.BuildableInvokable;
import org.jmock.dynamic.InvocationMatcher;
import org.jmock.dynamic.Stub;
import org.jmock.dynamock.BuildableInvokableFactory;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.expectation.ExpectationList;
import org.jmock.expectation.ExpectationValue;
import org.jmock.util.Verifier;



public class MockBuildableInvokableFactory 
	implements BuildableInvokableFactory, Verifiable 
{
	public ExpectationCounter createBuildableInvokableCalls = 
		new ExpectationCounter("createBuildableInvokable calls");
	public BuildableInvokable createBuildableInvokableResult;
	
	public BuildableInvokable createBuildableInvokable() {
		createBuildableInvokableCalls.inc();
		return createBuildableInvokableResult;
	}
	
	public ExpectationValue createMethodNameMatcherName = 
		new ExpectationValue("createMethodNameMatcher name");
	public InvocationMatcher createMethodNameMatcherResult;
	
	public InvocationMatcher createMethodNameMatcher( String name ) {
		createMethodNameMatcherName.setActual(name);
		return createMethodNameMatcherResult;
	}
	
	public ExpectationList createArgumentsMatcherConstraints =
		new ExpectationList("createArgumentsMatcher constraints");
	public InvocationMatcher createArgumentsMatcherResult;
	
	public InvocationMatcher createArgumentsMatcher( Constraint[] constraints ) {
		createArgumentsMatcherConstraints.addActualMany(constraints);
		return createArgumentsMatcherResult;
	}
	
	public ExpectationCounter createCallOnceMatcherCalls =
		new ExpectationCounter("createCallOnceMatcher #calls");
	public InvocationMatcher createCallOnceMatcherResult;
	
	public InvocationMatcher createCallOnceMatcher() {
		createCallOnceMatcherCalls.inc();
		return createCallOnceMatcherResult;
	}
	
	public ExpectationCounter createVoidStubCalls =
		new ExpectationCounter("createVoidStub calls");
	public Stub createVoidStubResult;
	
	public Stub createVoidStub() {
		createVoidStubCalls.inc();
		return createVoidStubResult;
	}
	
	public ExpectationValue createReturnStubValue =
		new ExpectationValue("createReturnStub value");
	public Stub createReturnStubResult;
	
	public Stub createReturnStub( Object value ) {
		createReturnStubValue.setActual(value);
		return createReturnStubResult;
	}
	
	public ExpectationValue createThrowStubThrowable =
		new ExpectationValue("createThrowStub throwable");
	public Stub createThrowStubResult;
    
    public Stub createThrowStub( Throwable throwable ) {
		createThrowStubThrowable.setActual(throwable);
		return createThrowStubResult;
	}
	
    public ExpectationValue createTestFailureStubErrorMessage =
        new ExpectationValue("createTestFailureStub errorMessage");
    public Stub createTestFailureStubResult;
    
    public Stub createTestFailureStub(String errorMessage) {
        createTestFailureStubErrorMessage.setActual(errorMessage);
        return createTestFailureStubResult;
    }

	public void verify() {
		Verifier.verifyObject(this);
	}
}
