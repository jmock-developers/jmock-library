/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.builder;

import junit.framework.AssertionFailedError;

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
import org.jmock.expectation.AssertMo;
import org.jmock.util.Dummy;

import test.jmock.builder.testsupport.MockBuilderIdentityTable;
import test.jmock.builder.testsupport.MockStubMatchersCollection;


public class InvocationMockerBuilderTest extends MockObjectSupportTestCase
{
    public interface MockedInterface {
        void method();
    }

    private MockStubMatchersCollection mocker;
    private MockBuilderIdentityTable idTable;
    private InvocationMockerBuilder builder;

    public void setUp() {
        mocker = new MockStubMatchersCollection();
        idTable = new MockBuilderIdentityTable();

        builder = new InvocationMockerBuilder(mocker, idTable, MockedInterface.class);
    }

    public void testSpecifyingMethodNameNameAddsMethodNameMatcherAndAddsSelfToIdentityTable() {
        mocker.addedMatcherType.setExpected(MethodNameMatcher.class);
        idTable.registerMethodName.setExpected("method");
        idTable.registerMethodNameBuilder.setExpected(builder);

        assertNotNull("Should be Stub Builder", builder.method("method"));

        mocker.verifyExpectations();
        idTable.verify();
    }

    public void testSpecifyingMethodWithIllegalNameThrowsIllegalArgumentError() {
        String illegalMethodName = "illegalMethodName()";

        try {
            builder.method(illegalMethodName);
        }
        catch( IllegalArgumentException ex ) {
            AssertMo.assertIncludes( "should contain illegal method name",
                                     illegalMethodName, ex.getMessage() );
            return;
        }
        fail("should have thrown IllegalArgumentException");
    }

    public void testMethodNameNotInMockedTypeCausesTestFailure() {
        String methodNameNotInMockedInterface = "methodNameNotInMockedInterface";

        try {
            builder.method(methodNameNotInMockedInterface);
        }
        catch( AssertionFailedError ex ) {
            AssertMo.assertIncludes( "should contain wrong method name",
                                     methodNameNotInMockedInterface, ex.getMessage() );
            AssertMo.assertIncludes( "should contain name of mocked type",
                                     MockedInterface.class.getName(), ex.getMessage() );
            return;
        }
        fail("should have thrown AssertionFailedError");
    }

    public void testSpecifyingMethodWithConstraintAddsMethodNameMatcherButDoesNotAddSelfToIdentityTable() {
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
