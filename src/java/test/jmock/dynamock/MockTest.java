package test.jmock.dynamock;

import junit.framework.TestCase;

import org.jmock.C;
import org.jmock.dynamic.framework.Stub;
import org.jmock.dynamic.stub.ReturnStub;
import org.jmock.dynamic.stub.VoidStub;
import org.jmock.dynamock.Mock;

import test.jmock.dynamic.testsupport.MockBuildableInvokable;
import test.jmock.dynamic.testsupport.MockDynamicMock;
import test.jmock.dynamic.testsupport.MockInvocationMatcher;
import test.jmock.dynamic.testsupport.MockInvokable;
import test.jmock.dynamic.testsupport.MockStub;


public class MockTest 
	extends TestCase 
{
	final String methodName = "method name";
	final Object result = new Object() { public String toString() { return "result"; } };
	final Throwable throwable = new Throwable("dummy throwable");
	
	
	MockBuildableInvokableFactory mockFactory;
	MockDynamicMock mockCoreMock;
	MockInvocationMatcher mockNameMatcher;
	MockInvocationMatcher mockArgumentsMatcher;
	MockInvocationMatcher mockCallOnceMatcher;
	MockBuildableInvokable mockInvocationMocker;
	MockStub mockStub;
	Mock mock;

	public void setUp() {
		mockCoreMock = new MockDynamicMock();
		mockFactory = new MockBuildableInvokableFactory();
		mockNameMatcher = new MockInvocationMatcher("mockNameMatcher");
		mockArgumentsMatcher = new MockInvocationMatcher("mockArgumentsMatcher");
		mockCallOnceMatcher = new MockInvocationMatcher("mockCallOnceMatcher");
		mockInvocationMocker = new MockBuildableInvokable("mockInvocationMocker");
		mockStub = new MockStub("mockStub");
		
		mock = new Mock( mockCoreMock, mockFactory );
	}
	
	private void verifyAll() {
		mockCoreMock.verifyExpectations();
		mockFactory.verify();
		mockNameMatcher.verifyExpectations();
		mockArgumentsMatcher.verifyExpectations();
		mockCallOnceMatcher.verifyExpectations();
		mockInvocationMocker.verifyExpectations();
		mockStub.verifyExpectations();
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
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		
		mockInvocationMocker.setStub.setExpected(mockStub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(mockArgumentsMatcher);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.stub( methodName, mockArgumentsMatcher, mockStub );
		
		verifyAll();
	}
	
	public void testStubVoidMethodCreatesMatchersAndVoidStub() {
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		mockFactory.createVoidStubCalls.setExpected(1);
		mockFactory.createVoidStubResult = mockStub;
		
		mockInvocationMocker.setStub.setExpected(mockStub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(mockArgumentsMatcher);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.stubVoid( methodName, mockArgumentsMatcher );
		
		verifyAll();
	}
	
	public void testStubAndReturnMethodCreatesMatchersAndReturnStub() {
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
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		mockFactory.createThrowStubThrowable.setExpected(throwable);
		mockFactory.createThrowStubResult = mockStub;
		
		mockInvocationMocker.setStub.setExpected(mockStub);
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
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		mockFactory.createVoidStubCalls.setExpected(1);
		mockFactory.createVoidStubResult = mockStub;
		
		mockInvocationMocker.setStub.setExpected(mockStub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(C.NO_ARGS);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.stubVoid( methodName );
		
		verifyAll();
	}
	
	public void testStubAndReturnMethodWithNoArgumentsMatcherImpliesNoArguments() {
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		mockFactory.createReturnStubValue.setExpected(result);
		mockFactory.createReturnStubResult = mockStub;
		
		mockInvocationMocker.setStub.setExpected(mockStub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(C.NO_ARGS);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.stubAndReturn( methodName, result );
		
		verifyAll();
	}
	
	public void testStubAndThrowMethodWithNoArgumentsMatcherImpliesNoArguments() {
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		mockFactory.createThrowStubThrowable.setExpected(throwable);
		mockFactory.createThrowStubResult = mockStub;
		
		mockInvocationMocker.setStub.setExpected(mockStub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(C.NO_ARGS);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.stubAndThrow( methodName, throwable );
		
		verifyAll();
	}


	public void testExpectMethodCreatesMatchersAndUsesSuppliedStub() {
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		mockFactory.createCallOnceMatcherCalls.setExpected(1);
		mockFactory.createCallOnceMatcherResult = mockCallOnceMatcher;
		
		mockInvocationMocker.setStub.setExpected(mockStub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(mockArgumentsMatcher);
		mockInvocationMocker.addedMatchers.addExpected(mockCallOnceMatcher);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.expect( methodName, mockArgumentsMatcher, mockStub );
		
		verifyAll();
	}
	
	public void testExpectVoidMethodCreatesMatchersAndVoidStub() {
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		mockFactory.createCallOnceMatcherCalls.setExpected(1);
		mockFactory.createCallOnceMatcherResult = mockCallOnceMatcher;
		mockFactory.createVoidStubCalls.setExpected(1);
		mockFactory.createVoidStubResult = mockStub;
		
		mockInvocationMocker.setStub.setExpected(mockStub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(mockArgumentsMatcher);
		mockInvocationMocker.addedMatchers.addExpected(mockCallOnceMatcher);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.expectVoid( methodName, mockArgumentsMatcher );
		
		verifyAll();
	}
	
	public void testExpectAndReturnMethodCreatesMatchersAndReturnStub() {
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		mockFactory.createCallOnceMatcherCalls.setExpected(1);
		mockFactory.createCallOnceMatcherResult = mockCallOnceMatcher;
		mockFactory.createReturnStubValue.setExpected(result);
		mockFactory.createReturnStubResult = mockStub;
		
		mockInvocationMocker.setStub.setExpected(mockStub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(mockArgumentsMatcher);
		mockInvocationMocker.addedMatchers.addExpected(mockCallOnceMatcher);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.expectAndReturn( methodName, mockArgumentsMatcher, result );
		
		verifyAll();
	}

	public void testExpectAndThrowMethodCreatesMatchersAndThrowStub() {
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		mockFactory.createCallOnceMatcherCalls.setExpected(1);
		mockFactory.createCallOnceMatcherResult = mockCallOnceMatcher;
		mockFactory.createThrowStubThrowable.setExpected(throwable);
		mockFactory.createThrowStubResult = mockStub;
		
		mockInvocationMocker.setStub.setExpected(mockStub);
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(mockArgumentsMatcher);
		mockInvocationMocker.addedMatchers.addExpected(mockCallOnceMatcher);
		
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
		
		mock.expectAndThrow( methodName, mockArgumentsMatcher, throwable );
		
		verifyAll();
	}
}
