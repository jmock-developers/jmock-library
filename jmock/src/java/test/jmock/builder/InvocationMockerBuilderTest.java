/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.builder;

import junit.framework.TestCase;
import org.jmock.expectation.ExpectationValue;
import org.jmock.expectation.Verifier;
import org.jmock.builder.InvocationMockerBuilder;
import org.jmock.dynamic.framework.InvocationMocker;
import org.jmock.dynamic.framework.Stub;
import org.jmock.dynamic.stub.ReturnStub;
import org.jmock.dynamic.stub.ThrowStub;
import org.jmock.dynamic.stub.VoidStub;

public class InvocationMockerBuilderTest extends TestCase {
    public class MockInvocationMocker extends InvocationMocker {
        public ExpectationValue setStubType = new ExpectationValue("setStub type");

        public void setStub(Stub stub) {
            setStubType.setActual(stub.getClass());
        }

        public void verifyExpectations() {
            Verifier.verifyObject(this);
        }
    }


    private MockInvocationMocker mocker = new MockInvocationMocker();
    private InvocationMockerBuilder builder = new InvocationMockerBuilder(mocker);

    public void testIsVoidSetsVoidStub() {
        mocker.setStubType.setExpected(VoidStub.class);

        assertNotNull("Should be expectation builder", builder.isVoid());

        mocker.verifyExpectations();
    }

    public void testReturnsSetsReturnStub() {
        mocker.setStubType.setExpected(ReturnStub.class);

        assertNotNull("Should be expectation builder", builder.returns("return value"));

        mocker.verifyExpectations();
    }

    public void testThrowsSetsThrowStub() {
        mocker.setStubType.setExpected(ThrowStub.class);

        assertNotNull("Should be expectation builder", builder.willThrow(new Exception("thrown value")));

        mocker.verifyExpectations();
    }
}
