package test.jmock.dynamock;

import org.jmock.C;
import org.jmock.Constraint;
import org.jmock.dynamic.framework.Stub;
import org.jmock.dynamic.stub.ReturnStub;
import org.jmock.dynamic.stub.ThrowStub;
import org.jmock.dynamic.stub.VoidStub;
import org.jmock.dynamock.Mock;


import test.jmock.dynamic.testsupport.MockBuildableInvokable;
import test.jmock.dynamic.testsupport.MockConstraint;
import test.jmock.dynamic.testsupport.MockDynamicMock;
import test.jmock.dynamic.testsupport.MockInvocationMatcher;
import test.jmock.dynamic.testsupport.MockInvokable;
import junit.framework.TestCase;


public class MockTest 
	extends TestCase 
{
	final String methodName = "method name";
	
	MockBuildableInvokableFactory mockFactory;
	MockDynamicMock mockCoreMock;
	MockInvocationMatcher mockNameMatcher;
	MockInvocationMatcher mockArgumentsMatcher;
	MockBuildableInvokable mockInvocationMocker;
	Constraint[] argumentConstraints;
	Mock mock;
	
	
	public void setUp() {
		mockCoreMock = new MockDynamicMock();
		mockFactory = new MockBuildableInvokableFactory();
		mockNameMatcher = new MockInvocationMatcher("mockNameMatcher");
		mockArgumentsMatcher = new MockInvocationMatcher("mockArgumentsMatcher");
		mockInvocationMocker = new MockBuildableInvokable("mockInvocationMocker");
		
		argumentConstraints = new Constraint[] {
			new MockConstraint("ARGS[0]",null,true),
			new MockConstraint("ARGS[1]",null,true)
		};
		
		mock = new Mock( mockCoreMock, mockFactory );
	}
	
	private void verifyAll() {
		mockCoreMock.verifyExpectations();
		mockFactory.verify();
		mockInvocationMocker.verifyExpectations();
		mockNameMatcher.verifyExpectations();
		mockArgumentsMatcher.verifyExpectations();
	}
	
	public void testDelegatesToStringToCoreMock() {
		final String coreMockName = "core mock name";
		
		mockCoreMock.toStringResult = coreMockName;
		
		assertEquals( "expected core mock name", coreMockName, mock.toString() );
		
		verifyAll();
	}
	
	public void testDelegatesVerifyToCoreMock() {
		mockCoreMock.verifyCalls.setExpected(1);
		
		mock.verify();
		
		verifyAll();
	}
	
	public void testDelegatesResetToCoreMock() {
		mockCoreMock.resetCalls.setExpected(1);
		
		mock.reset();
		
		verifyAll();
	}
	
	public void testExposesProxyOfCoreMock() {
		Object proxy = new Object();
		
		mockCoreMock.proxyResult = proxy;
		
		assertSame( "expected proxy of core mock", proxy, mock.proxy() );
	}
	
	public void testDelegatesAddToCoreMock() {
		MockInvokable invokable = new MockInvokable();
		
		mockCoreMock.addInvokable.setExpected(invokable);
		
		mock.add(invokable);
		
		verifyAll();
	}
	
	public void testStubMethodCreatesMatchersAndUsesSuppliedStub() {
		Stub stub = new VoidStub();
		
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		
		mockInvocationMocker.setStub.setExpected(stub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(mockArgumentsMatcher);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.stub( methodName, mockArgumentsMatcher, stub );
		
		verifyAll();
	}
	
	public void testStubVoidMethodCreatesMatchersAndVoidStub() {
		Stub stub = new VoidStub();
		
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		mockFactory.createVoidStubCalls.setExpected(1);
		mockFactory.createVoidStubResult = stub;
		
		mockInvocationMocker.setStub.setExpected(stub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(mockArgumentsMatcher);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.stubVoid( methodName, mockArgumentsMatcher );
		
		verifyAll();
	}
	
	public void testStubAndReturnMethodCreatesMatchersAndReturnStub() {
		Object result = new Object() { public String toString() { return "result"; } };
		Stub stub = new ReturnStub(result);
		
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		mockFactory.createReturnStubValue.setExpected(result);
		mockFactory.createReturnStubResult = stub;
		
		mockInvocationMocker.setStub.setExpected(stub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(mockArgumentsMatcher);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.stubAndReturn( methodName, mockArgumentsMatcher, result );
		
		verifyAll();
	}
	
	public void testStubAndThrowMethodCreatesMatchersAndThrowStub() {
		Throwable throwable = new Throwable("dummy throwable");
		Stub stub = new ThrowStub(throwable);
		
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		mockFactory.createThrowStubThrowable.setExpected(throwable);
		mockFactory.createThrowStubResult = stub;
		
		mockInvocationMocker.setStub.setExpected(stub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(mockArgumentsMatcher);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.stubAndThrow( methodName, mockArgumentsMatcher, throwable );
		
		verifyAll();
	}
	
	public void testStubMethodWithNoArgumentsMatcherImpliesNoArguments() {
		Stub stub = new VoidStub();
		
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		
		mockInvocationMocker.setStub.setExpected(stub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(C.NO_ARGS);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.stub( methodName, stub );
		
		verifyAll();
	}
	
	public void testStubVoidMethodWithNoArgumentsMatcherImpliesNoArguments() {
		Stub stub = new VoidStub();
		
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		mockFactory.createVoidStubCalls.setExpected(1);
		mockFactory.createVoidStubResult = stub;
		
		mockInvocationMocker.setStub.setExpected(stub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(C.NO_ARGS);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.stubVoid( methodName );
		
		verifyAll();
	}
	
	public void testStubAndReturnMethodWithNoArgumentsMatcherImpliesNoArguments() {
		Object result = new Object() { public String toString() { return "result"; } };
		Stub stub = new ReturnStub(result);
		
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		mockFactory.createReturnStubValue.setExpected(result);
		mockFactory.createReturnStubResult = stub;
		
		mockInvocationMocker.setStub.setExpected(stub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(C.NO_ARGS);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.stubAndReturn( methodName, result );
		
		verifyAll();
	}
	
	public void testStubAndThrowMethodWithNoArgumentsMatcherImpliesNoArguments() {
		Throwable throwable = new Throwable("dummy throwable");
		Stub stub = new ThrowStub(throwable);
		
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		mockFactory.createThrowStubThrowable.setExpected(throwable);
		mockFactory.createThrowStubResult = stub;
		
		mockInvocationMocker.setStub.setExpected(stub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(C.NO_ARGS);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.stubAndThrow( methodName, throwable );
		
		verifyAll();
	}
}
