/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic;

import junit.framework.TestCase;

import org.jmock.Constraint;
import org.jmock.constraint.IsEqual;
import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.InvocationMatcher;
import org.jmock.dynamic.InvocationMocker;
import org.jmock.dynamic.Stub;
import org.jmock.dynamic.matcher.ArgumentsMatcher;
import org.jmock.dynamic.matcher.MethodNameMatcher;
import org.jmock.dynamic.matcher.StatelessInvocationMatcher;
import org.jmock.dynamic.stub.VoidStub;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.expectation.ExpectationValue;
import org.jmock.util.Verifier;


public class InvocationMockerTest extends TestCase {
    private InvocationMatcher matchAll = new StatelessInvocationMatcher() {
        public boolean matches(Invocation invocation) {
            return true;
        }

        public StringBuffer writeTo(StringBuffer buffer) {
            return buffer.append("match all");
        }
    };
    private InvocationMatcher matchNone = new StatelessInvocationMatcher() {
        public boolean matches(Invocation invocation) {
            return false;
        }

        public StringBuffer writeTo(StringBuffer buffer) {
            return buffer.append("match none");
        }
    };

    public class MockInvocationMatcher implements InvocationMatcher {
        public ExpectationValue invocation = new ExpectationValue("MockInvocationMatcher.invoked");
        public ExpectationValue match = new ExpectationValue("MockInvocationMatcher.matches");
        public ExpectationCounter verifyCalls = new ExpectationCounter("Verify calls");

        public boolean matches(Invocation invocation) {
            match.setActual(invocation);
            return true;
        }

        public void invoked(Invocation invocation) {
            this.invocation.setActual(invocation);
        }

        public StringBuffer writeTo(StringBuffer buffer) {
            return buffer.append("Mock matcher");
        }

        public void verify() {
            verifyCalls.inc();
        }
    }

    public class MockStub implements Stub {
        public ExpectationValue stubInvocation = new ExpectationValue("Stub invocation");

        public Object invoke(Invocation invocation) throws Throwable {
            stubInvocation.setActual(invocation);
            return "stub result";
        }

        public StringBuffer writeTo(StringBuffer buffer) {
            return buffer.append("Mock stub");
        }

    };

    private static final String ARG2 = "arg2";
    private static final String ARG1 = "arg1";
    private Invocation exampleInvocation =
            new Invocation(Void.class, "example", new Class[]{String.class, String.class}, Void.class,
                    new Object[]{ARG1, ARG2});


    public void testMatchesIfEverythingMatches() {
        InvocationMocker invocationMocker =
                new InvocationMocker(new InvocationMatcher[]{matchAll, matchAll}, null);

        assertTrue("Should have matched", invocationMocker.matches(exampleInvocation));
    }

    public void testDoesNotMatchIfEverythingMatches() {
        InvocationMocker invocationMocker =
                new InvocationMocker(new InvocationMatcher[]{matchAll, matchNone}, null);

        assertFalse("Should not have matched", invocationMocker.matches(exampleInvocation));
    }

    public void testMatchesInvocationWithParameters() {
        InvocationMocker invocationMocker = new InvocationMocker(
                new InvocationMatcher[]{
                    new MethodNameMatcher("example"),
                    new ArgumentsMatcher(makeConstraintArray(ARG1, ARG2))}, null);

        assertTrue("Should have matched", invocationMocker.matches(exampleInvocation));
    }

    public void testDoesNotMatchWithDifferentParameter() {
        InvocationMocker invocationMocker = new InvocationMocker(
                new InvocationMatcher[]{
                    new MethodNameMatcher("example"),
                    new ArgumentsMatcher(makeConstraintArray(ARG1, "not " + ARG2))}, null);

        assertFalse("Should not have matched", invocationMocker.matches(exampleInvocation));
    }

    public void testMatchesInvocationBeforeCallingStub() throws Throwable {
        MockInvocationMatcher mockInvocationMatcher = new MockInvocationMatcher();

        InvocationMocker mocker = new InvocationMocker(new InvocationMatcher[]{mockInvocationMatcher}, new VoidStub());
        mockInvocationMatcher.invocation.setExpected(exampleInvocation);

        mocker.invoke(exampleInvocation);

        Verifier.verifyObject(mockInvocationMatcher);
    }

    public void testDelegatesVerifyToInvocationMatchers() throws Throwable {
        MockInvocationMatcher mockInvocationMatcher = new MockInvocationMatcher();

        InvocationMocker mocker = new InvocationMocker(new InvocationMatcher[]{mockInvocationMatcher}, new VoidStub());
        mockInvocationMatcher.verifyCalls.setExpected(1);

        mocker.verify();

        Verifier.verifyObject(mockInvocationMatcher);
    }


    public void testDelegatesInvocationToStubObject() throws Throwable {
        MockStub mockStub = new MockStub();

        InvocationMocker mocker = new InvocationMocker(new InvocationMatcher[0], mockStub);

        mockStub.stubInvocation.setExpected(exampleInvocation);

        assertEquals("Should be invoke result", "stub result", mocker.invoke(exampleInvocation));

        Verifier.verifyObject(mockStub);
    }

    public void testCanAddExtraMatchers() throws Throwable {
        MockInvocationMatcher mockInvocationMatcher = new MockInvocationMatcher();

        InvocationMocker mocker = new InvocationMocker(new InvocationMatcher[0], new VoidStub());
        mockInvocationMatcher.match.setExpected(exampleInvocation);
        mockInvocationMatcher.invocation.setExpected(exampleInvocation);

        mocker.addMatcher(mockInvocationMatcher);
        mocker.matches(exampleInvocation);
        mocker.invoke(exampleInvocation);

        Verifier.verifyObject(mockInvocationMatcher);
    }

    private Constraint[] makeConstraintArray(Object arg1, Object arg2) {
        return new Constraint[] { new IsEqual(arg1), new IsEqual(arg2) };
    }
}
