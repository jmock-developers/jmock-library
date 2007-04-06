/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.builder.testsupport;

import junit.framework.Assert;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.Stub;
import org.jmock.core.StubMatchersCollection;
import org.jmock.expectation.ExpectationValue;
import org.jmock.util.Verifier;


public class MockStubMatchersCollection implements StubMatchersCollection
{
    public ExpectationValue addedMatcher = new ExpectationValue("added matcher");
    public ExpectationValue addedMatcherType = new ExpectationValue("added matcher type");

    public void addMatcher( InvocationMatcher matcher ) {
        addedMatcher.setActual(matcher);
        addedMatcherType.setActual(matcher.getClass());
    }

    public ExpectationValue setStubType = new ExpectationValue("set stub type");
    public ExpectationValue setStub = new ExpectationValue("set stub");
    public ExpectationValue setStubReturnValue = new ExpectationValue("set stub return value");

    public void setStub( Stub stub ) {
        setStub.setActual(stub);
        setStubType.setActual(stub.getClass());

        if (setStubReturnValue.hasExpectations()) {
            try {
                setStubReturnValue.setActual(stub.invoke(null));
            }
            catch (Throwable t) {
                Assert.fail("unexpected throw from stub: " + t);
            }
        }
    }

    public ExpectationValue setName = new ExpectationValue("setName name");

    public void setName( String name ) {
        setName.setActual(name);
    }

    public void verifyExpectations() {
        Verifier.verifyObject(this);
    }
}
