package test.jmock.builder;

import org.jmock.dynamic.framework.InvocationMatcher;
import org.jmock.dynamic.framework.Stub;
import org.jmock.dynamic.framework.StubMatchersCollection;
import org.jmock.expectation.ExpectationValue;
import org.jmock.expectation.Verifier;


public class MockStubMatchersCollection implements StubMatchersCollection {
    public ExpectationValue setStubType = new ExpectationValue("setStub type");
    public ExpectationValue addedMatcherType = new ExpectationValue("add matcher type");
    
	public void addMatcher(InvocationMatcher matcher) {
		addedMatcherType.setActual(matcher.getClass());
	}

    public void setStub(Stub stub) {
        setStubType.setActual(stub.getClass());
    }

    public void verifyExpectations() {
        Verifier.verifyObject(this);
    }
}