/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.builder;

import org.jmock.builder.InvocationMockerBuilder;
import org.jmock.core.Constraint;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.MockObjectSupportTestCase;
import org.jmock.core.Stub;
import org.jmock.core.matcher.AnyArgumentsMatcher;
import org.jmock.core.matcher.ArgumentsMatcher;
import org.jmock.core.matcher.MethodNameMatcher;
import org.jmock.core.matcher.NoArgumentsMatcher;
import org.jmock.core.stub.VoidStub;
import org.jmock.util.Dummy;
import test.jmock.builder.testsupport.MockBuilderIdentityTable;
import test.jmock.builder.testsupport.MockStubMatchersCollection;


public class InvocationMockerBuilderTest extends MockObjectSupportTestCase
{
    private MockStubMatchersCollection mocker;
    private MockBuilderIdentityTable idTable;
    private InvocationMockerBuilder builder;

    public void setUp() {
        mocker = new MockStubMatchersCollection();
        idTable = new MockBuilderIdentityTable();

        builder = new InvocationMockerBuilder(mocker, idTable);
    }

    public void testSpecifyingMethodNameNameAddsMethodNameMatcherAndAddsSelfToIdentityTable() {
        mocker.addedMatcherType.setExpected(MethodNameMatcher.class);
        idTable.registerMethodName.setExpected("methodName");
        idTable.registerMethodNameBuilder.setExpected(builder);

        assertNotNull("Should be Stub Builder", builder.method("methodName"));

        mocker.verifyExpectations();
        idTable.verify();
    }

    public void testMethodMethodWithConstraintAddsMethodNameMatcherButDoesNotAddSelfToIdentityTable() {
        Constraint nameConstraint = (Constraint)newDummy(Constraint.class, "nameConstraint");

        mocker.addedMatcherType.setExpected(MethodNameMatcher.class);

        assertNotNull("Should be Stub Builder", builder.method(nameConstraint));

        mocker.verifyExpectations();
    }

    public void testCanAddCustomMatcher() {
        InvocationMatcher matcher =
                (InvocationMatcher)Dummy.newDummy(InvocationMatcher.class, "matcher");

        mocker.addedMatcher.setExpected(matcher);

        assertNotNull("Should be Stub Builder", builder.match(matcher));

        mocker.verifyExpectations();
    }

    public void testWithMethodAddsArgumentsMatcher() {
        mocker.addedMatcherType.setExpected(ArgumentsMatcher.class);

        assertNotNull("Should be Stub Builder", builder.with(new Constraint[0]));

        mocker.verifyExpectations();
    }

    public void testWithMethodWithOneObjectArgumentAddsArgumentsMatcher() {
        mocker.addedMatcherType.setExpected(ArgumentsMatcher.class);

        assertNotNull("Should be Stub Builder", builder.with(eq(new Object())));

        mocker.verifyExpectations();
    }

    public void testWithMethodWithTwoObjectArgumentsAddsArgumentsMatcher() {
        mocker.addedMatcherType.setExpected(ArgumentsMatcher.class);

        assertNotNull("Should be Stub Builder",
                      builder.with(eq(new Object()), eq(new Object())));

        mocker.verifyExpectations();
    }

    public void testNoParamsAddsNoArgumentMatcher() {
        mocker.addedMatcher.setExpected(NoArgumentsMatcher.INSTANCE);

        assertNotNull("Should be Stub Builder", builder.withNoArguments());

        mocker.verifyExpectations();
    }

    public void testAnyParamsAddsAnyArgumentMatcher() {
        mocker.addedMatcher.setExpected(AnyArgumentsMatcher.INSTANCE);

        assertNotNull("Should be Stub Builder", builder.withAnyArguments());

        mocker.verifyExpectations();
    }

    public void testCanSetCustomStub() {
        Stub stub = (Stub)Dummy.newDummy(Stub.class, "stub");

        mocker.setStub.setExpected(stub);

        assertNotNull("should be expectation builder", builder.will(stub));
    }

    public void testIsVoidSetsVoidStub() {
        mocker.setStubType.setExpected(VoidStub.class);

        assertNotNull("Should be expectation builder", builder.isVoid());

        mocker.verifyExpectations();
    }

    static final String INVOCATION_ID = "INVOCATION-ID";

    public void testUniquelyIdentifyInvocationMockerAndRegisterItselfInBuilderIdentityTable() {
        mocker.setName.setExpected(INVOCATION_ID);
        idTable.registerUniqueID.setExpected(INVOCATION_ID);
        idTable.registerUniqueIDBuilder.setExpected(builder);

        builder.id(INVOCATION_ID);

        idTable.verify();
        mocker.verifyExpectations();
    }
}
