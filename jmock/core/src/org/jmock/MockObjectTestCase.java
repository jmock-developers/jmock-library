/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock;

import org.jmock.core.*;
import org.jmock.core.matcher.InvokeAtLeastOnceMatcher;
import org.jmock.core.matcher.InvokeOnceMatcher;
import org.jmock.core.matcher.TestFailureMatcher;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.ThrowStub;
import test.jmock.core.stub.StubSequence;


/**
 * A base class for tests that use <a href="http://www.mockobjects.com">Mock Objects</a>.
 * This class provides methods for creating mock objects and expectations and automatically
 * verifying mock objects after the test has run, but before the test fixture has been torn down.
 */
public abstract class MockObjectTestCase
        extends MockObjectSupportTestCase
{
    public MockObjectTestCase() {
    }

    public MockObjectTestCase( String name ) {
        super(name);
    }

    public Mock mock( Class mockedType ) {
        return mock(mockedType, defaultMockNameForType(mockedType));
    }

    public Mock mock( Class mockedType, String roleName ) {
        Mock newMock = new Mock(newCoreMock(mockedType, roleName));
        registerToVerify(newMock);
        return newMock;
    }

    protected DynamicMock newCoreMock( Class mockedType, String roleName ) {
        return new CoreMock(mockedType, roleName);
    }

    public String defaultMockNameForType( Class mockedType ) {
        String fullTypeName = mockedType.getName();
        String typeName =
                fullTypeName.substring(Math.max(fullTypeName.lastIndexOf('.'), fullTypeName.lastIndexOf('$')) + 1);

        return "mock" + typeName;
    }

    public Stub returnValue( Object o ) {
        return new ReturnStub(o);
    }

    public Stub returnValue( boolean result ) {
        return returnValue(new Boolean(result));
    }

    public Stub returnValue( byte result ) {
        return returnValue(new Byte(result));
    }

    public Stub returnValue( char result ) {
        return returnValue(new Character(result));
    }

    public Stub returnValue( short result ) {
        return returnValue(new Short(result));
    }

    public Stub returnValue( int result ) {
        return returnValue(new Integer(result));
    }

    public Stub returnValue( long result ) {
        return returnValue(new Long(result));
    }

    public Stub returnValue( float result ) {
        return returnValue(new Float(result));
    }

    public Stub returnValue( double result ) {
        return returnValue(new Double(result));
    }

    public Stub throwException( Throwable throwable ) {
        return new ThrowStub(throwable);
    }

    public InvocationMatcher once() {
        return new InvokeOnceMatcher();
    }

    public InvocationMatcher atLeastOnce() {
        return new InvokeAtLeastOnceMatcher();
    }

    public InvocationMatcher never() {
        return new TestFailureMatcher("expect not called");
    }

    public Stub onConsecutiveCalls( Stub stub1, Stub stub2 ) {
        return new StubSequence(new Stub[]{stub1, stub2});
    }

    public Stub onConsecutiveCalls( Stub stub1, Stub stub2, Stub stub3 ) {
        return new StubSequence(new Stub[]{stub1, stub2, stub3});
    }

    public Stub onConsecutiveCalls( Stub stub1, Stub stub2, Stub stub3, Stub stub4 ) {
        return new StubSequence(new Stub[]{stub1, stub2, stub3, stub4});
    }
}
