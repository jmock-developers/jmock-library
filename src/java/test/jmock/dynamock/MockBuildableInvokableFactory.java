package test.jmock.dynamock;

import org.jmock.Constraint;
import org.jmock.dynamic.BuildableInvokable;
import org.jmock.dynamic.InvocationMatcher;
import org.jmock.dynamic.Invokable;
import org.jmock.dynamic.Stub;
import org.jmock.dynamock.BuildableInvokableFactory;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.expectation.ExpectationList;
import org.jmock.expectation.ExpectationValue;
import org.jmock.expectation.NotImplementedException;
import org.jmock.expectation.Verifiable;
import org.jmock.expectation.Verifier;



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
	
	public void verify() {
		Verifier.verifyObject(this);
	}
	
	/*  The following methods are here during the cleanup of the dynamock.Mock class and
	 *  will eventually be refactored into nothingness.
	 */

	public Invokable createReturnExpectation( String methodName,
											  InvocationMatcher arguments,
											  Object result ) 
	{
		throw new NotImplementedException("not implemented");
	}

	public Invokable createReturnStub( String methodName,
									   InvocationMatcher arguments,
									   Object result ) 
	{
		throw new NotImplementedException("not implemented");
	}

	public Invokable createThrowableExpectation( String methodName,
												 InvocationMatcher arguments,
												 Throwable throwable) 
	{
		throw new NotImplementedException("not implemented");
	}

	public Invokable createThrowableStub( String methodName,
										  InvocationMatcher arguments,
										  Throwable throwable ) 
	{
		throw new NotImplementedException("not implemented");
	}

	public Invokable createVoidExpectation( String methodName,
											InvocationMatcher arguments) 
	{
		throw new NotImplementedException("not implemented");
	}

	public Invokable createVoidStub( String methodName,
									 InvocationMatcher arguments ) 
	{
		throw new NotImplementedException("not implemented");
	}

}
