package test.jmock.dynamock;

import junit.framework.TestCase;

import org.jmock.C;
import org.jmock.dynamic.InvocationMatcher;
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
	MockStub mockVoidStub, mockThrowStub, mockReturnStub, mockCustomStub;
	Mock mock;

	public void setUp() {
		mockCoreMock = new MockDynamicMock();
		mockFactory = new MockBuildableInvokableFactory();
		mockNameMatcher = new MockInvocationMatcher("mockNameMatcher");
		mockArgumentsMatcher = new MockInvocationMatcher("mockArgumentsMatcher");
		mockCallOnceMatcher = new MockInvocationMatcher("mockCallOnceMatcher");
		mockInvocationMocker = new MockBuildableInvokable("mockInvocationMocker");
		mockVoidStub = new MockStub("mockVoidStub");
		mockReturnStub = new MockStub("mockReturnStub");
		mockThrowStub = new MockStub("mockThrowStub");
		mockCustomStub = new MockStub("mockCustomStub");
		
		mock = new Mock( mockCoreMock, mockFactory );
	}
	
	private void verifyAll() {
		mockCoreMock.verifyExpectations();
		mockFactory.verify();
		mockNameMatcher.verifyExpectations();
		mockArgumentsMatcher.verifyExpectations();
		mockCallOnceMatcher.verifyExpectations();
		mockInvocationMocker.verifyExpectations();
		mockVoidStub.verifyExpectations();
		mockReturnStub.verifyExpectations();
		mockThrowStub.verifyExpectations();
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
		expectConstructionOfInvocationMocker();
		expectConstructionOfMethodMatchers(mockArgumentsMatcher);
		expectConstructionOfCustomStub();
		
		mock.stub( methodName, mockArgumentsMatcher, mockCustomStub );
		
		verifyAll();
	}
	
	public void testStubVoidMethodCreatesMatchersAndVoidStub() {
		expectConstructionOfInvocationMocker();
		expectConstructionOfMethodMatchers(mockArgumentsMatcher);
		expectConstructionOfVoidStub();
		
		mock.stubVoid( methodName, mockArgumentsMatcher );
		
		verifyAll();
	}
	
	public void testStubAndReturnMethodCreatesMatchersAndReturnStub() {
		expectConstructionOfInvocationMocker();
		expectConstructionOfMethodMatchers(mockArgumentsMatcher);
		expectConstructionOfReturnStub();
		
		mock.stubAndReturn( methodName, mockArgumentsMatcher, result );
		
		verifyAll();
	}
	
	
	public void testStubAndThrowMethodCreatesMatchersAndThrowStub() {
		expectConstructionOfInvocationMocker();
		expectConstructionOfMethodMatchers(mockArgumentsMatcher);
		expectConstructionOfThrowStub();
		
		mock.stubAndThrow( methodName, mockArgumentsMatcher, throwable );
		
		verifyAll();
	}
	
	public void testStubMethodWithNoArgumentsMatcherImpliesNoArguments() {
		expectConstructionOfInvocationMocker();
		expectConstructionOfMethodMatchers(C.NO_ARGS);
		expectConstructionOfCustomStub();
		
		mock.stub( methodName, mockCustomStub );
		
		verifyAll();
	}
	
	public void testStubVoidMethodWithNoArgumentsMatcherImpliesNoArguments() {
		expectConstructionOfInvocationMocker();
		expectConstructionOfMethodMatchers(C.NO_ARGS);
		expectConstructionOfVoidStub();
		
		mock.stubVoid( methodName );
		
		verifyAll();
	}
	
	public void testStubAndReturnMethodWithNoArgumentsMatcherImpliesNoArguments() {
		expectConstructionOfInvocationMocker();
		expectConstructionOfMethodMatchers(C.NO_ARGS);
		expectConstructionOfReturnStub();
		
		mock.stubAndReturn( methodName, result );
		
		verifyAll();
	}
	
	public void testStubAndThrowMethodWithNoArgumentsMatcherImpliesNoArguments() {
		expectConstructionOfInvocationMocker();
		expectConstructionOfMethodMatchers(C.NO_ARGS);
		expectConstructionOfThrowStub();
		
		mock.stubAndThrow( methodName, throwable );
		
		verifyAll();
	}
	

	public void testExpectMethodCreatesMatchersAndUsesSuppliedStub() {
		expectConstructionOfInvocationMocker();
		expectConstructionOfMethodMatchers(mockArgumentsMatcher);
		expectConstructionOfCallOnceMatcher();
		expectConstructionOfCustomStub();
		
		mock.expect( methodName, mockArgumentsMatcher, mockCustomStub );
		
		verifyAll();
	}
	
	public void testExpectVoidMethodCreatesMatchersAndVoidStub() {
		expectConstructionOfInvocationMocker();
		expectConstructionOfMethodMatchers(mockArgumentsMatcher);
		expectConstructionOfCallOnceMatcher();
		expectConstructionOfVoidStub();
		
		mock.expectVoid( methodName, mockArgumentsMatcher );
		
		verifyAll();
	}
	
	public void testExpectAndReturnMethodCreatesMatchersAndReturnStub() {
		expectConstructionOfInvocationMocker();
		expectConstructionOfMethodMatchers(mockArgumentsMatcher);
		expectConstructionOfCallOnceMatcher();
		expectConstructionOfReturnStub();
		
		mock.expectAndReturn( methodName, mockArgumentsMatcher, result );
		
		verifyAll();
	}

	public void testExpectAndThrowMethodCreatesMatchersAndThrowStub() {
		expectConstructionOfInvocationMocker();
		expectConstructionOfMethodMatchers(mockArgumentsMatcher);
		expectConstructionOfCallOnceMatcher();
		expectConstructionOfThrowStub();
		
		mock.expectAndThrow( methodName, mockArgumentsMatcher, throwable );
		
		verifyAll();
	}
	
	public void testExpectMethodWithNoArgumentsMatcherImpliesNoArguments() {
		expectConstructionOfInvocationMocker();
		expectConstructionOfMethodMatchers(C.NO_ARGS);
		expectConstructionOfCallOnceMatcher();
		expectConstructionOfCustomStub();
		
		mock.expect( methodName, mockCustomStub );
		
		verifyAll();
	}
	
	public void testExpectVoidMethodWithNoArgumentsMatcherImpliesNoArguments() {
		expectConstructionOfInvocationMocker();
		expectConstructionOfMethodMatchers(C.NO_ARGS);
		expectConstructionOfCallOnceMatcher();
		expectConstructionOfVoidStub();
		
		mock.expectVoid( methodName );
		
		verifyAll();
	}
	
	public void testExpectAndReturnMethodWithNoArgumentsMatcherImpliesNoArguments() {
		expectConstructionOfInvocationMocker();
		expectConstructionOfMethodMatchers(C.NO_ARGS);
		expectConstructionOfCallOnceMatcher();
		expectConstructionOfReturnStub();
		
		mock.expectAndReturn( methodName, result );
		
		verifyAll();
	}
	
	public void testExpectAndThrowMethodWithNoArgumentsMatcherImpliesNoArguments() {
		expectConstructionOfInvocationMocker();
		expectConstructionOfMethodMatchers(C.NO_ARGS);
		expectConstructionOfCallOnceMatcher();
		expectConstructionOfThrowStub();
		
		mock.expectAndThrow( methodName, throwable );
		
		verifyAll();
	}


	

	private void expectConstructionOfInvocationMocker() {
		mockFactory.createBuildableInvokableCalls.setExpected(1);
		mockFactory.createBuildableInvokableResult = mockInvocationMocker;
		mockCoreMock.addInvokable.setExpected( mockInvocationMocker );
	}
	
	private void expectConstructionOfMethodMatchers( InvocationMatcher argumentsMatcher ) {
		mockFactory.createMethodNameMatcherName.setExpected(methodName);
		mockFactory.createMethodNameMatcherResult = mockNameMatcher;
		
		mockInvocationMocker.addedMatchers.addExpected(mockNameMatcher);
		mockInvocationMocker.addedMatchers.addExpected(argumentsMatcher);
	}
	
	private void expectConstructionOfCallOnceMatcher() {
		mockFactory.createCallOnceMatcherCalls.setExpected(1);
		mockFactory.createCallOnceMatcherResult = mockCallOnceMatcher;

		mockInvocationMocker.addedMatchers.addExpected(mockCallOnceMatcher);
	}

	private void expectConstructionOfCustomStub() {
		mockInvocationMocker.setStub.setExpected(mockCustomStub);
	}
	
	private void expectConstructionOfVoidStub() {
		mockFactory.createVoidStubCalls.setExpected(1);
		mockFactory.createVoidStubResult = mockVoidStub;
		mockInvocationMocker.setStub.setExpected(mockVoidStub);
	}
	
	private void expectConstructionOfReturnStub() {
		mockFactory.createReturnStubValue.setExpected(result);
		mockFactory.createReturnStubResult = mockReturnStub;
		mockInvocationMocker.setStub.setExpected(mockReturnStub);
	}
	
	private void expectConstructionOfThrowStub() {
		mockFactory.createThrowStubThrowable.setExpected(throwable);
		mockFactory.createThrowStubResult = mockThrowStub;
		mockInvocationMocker.setStub.setExpected(mockThrowStub);
	}
}
