package test.jmock.builder;

import org.jmock.dynamic.framework.InvocationMatcher;
import org.jmock.dynamic.framework.Stub;
import org.jmock.dynamic.framework.StubMatchersCollection;
import org.jmock.expectation.ExpectationValue;
import org.jmock.expectation.Verifier;


public class MockStubMatchersCollection implements StubMatchersCollection {
    public ExpectationValue setStubType = new ExpectationValue("set stub type");
    public ExpectationValue addedMatcher = new ExpectationValue("added matcher");
    public ExpectationValue addedMatcherType = new ExpectationValue("added matcher type");
    
	public void addMatcher(InvocationMatcher matcher) {
		addedMatcher.setActual(matcher);
		addedMatcherType.setActual(matcher.getClass());
	}

    public void setStub(Stub stub) {
        setStubType.setActual(stub.getClass());
    }

    public void verifyExpectations() {
        Verifier.verifyObject(this);
    }
}