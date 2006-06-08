/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock;

import java.util.Collection;

import org.jmock.core.*;
import org.jmock.core.matcher.InvokeAtLeastOnceMatcher;
import org.jmock.core.matcher.InvokeAtMostOnceMatcher;
import org.jmock.core.matcher.InvokeOnceMatcher;
import org.jmock.core.matcher.TestFailureMatcher;
import org.jmock.core.matcher.InvokeCountMatcher;
import org.jmock.core.stub.DoAllStub;
import org.jmock.core.stub.ReturnIteratorStub;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.StubSequence;
import org.jmock.core.stub.ThrowStub;


/**
 * A base class for tests that use <a href="http://www.mockobjects.com">Mock Objects</a>.
 * This class provides methods for creating mock objects and expectations and automatically
 * verifying mock objects after the test has run but before the test fixture has been torn down.
 * 
 * @since 1.0.0
 */
public abstract class MockObjectTestCase
    extends MockObjectSupportTestCase
{
    public MockObjectTestCase() {
    }

    public MockObjectTestCase( String name ) {
        super(name);
    }

    /**
     * Creates a mock object that mocks the given type.  The mock object is named after the type;  the exact
     * name is calculated by {@link #defaultMockNameForType}.
     *
     * @param mockedType The type to be mocked.
     * @return A {@link Mock} object that mocks <var>mockedType</var>.
     */
    public Mock mock( Class mockedType ) {
        return mock(mockedType, defaultMockNameForType(mockedType));
    }

    /**
     * Creates a mock object that mocks the given type and is explicitly given a name.
     * The mock object is named after the type;  the exact name is calculated by {@link #defaultMockNameForType}.
     *
     * @param mockedType The type to be mocked.
     * @param roleName The name of the mock object
     * @return A {@link Mock} object that mocks <var>mockedType</var>.
     */
    public Mock mock( Class mockedType, String roleName ) {
        Mock newMock = new Mock(newCoreMock(mockedType, roleName));
        registerToVerify(newMock);
        return newMock;
    }
    
    protected DynamicMock newCoreMock( Class mockedType, String roleName ) {
        return new CoreMock(mockedType, roleName);
    }

    /**
     * Calculates
     * @param mockedType
     * @return
     */
    public String defaultMockNameForType( Class mockedType ) {
        return "mock" + Formatting.classShortName(mockedType);
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

    public Stub returnIterator(Collection collection) {
        return new ReturnIteratorStub(collection);
    }
    
    public Stub returnIterator(Object[] array) {
        return new ReturnIteratorStub(array);
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
    
	public InvocationMatcher atMostOnce() {
		return new InvokeAtMostOnceMatcher();
	}
	
    public InvocationMatcher exactly( int expectedCount ) {
        return new InvokeCountMatcher(expectedCount);
    }
    
    public InvocationMatcher never() {
        return new TestFailureMatcher("not expected");
    }

    public InvocationMatcher never( String errorMessage ) {
        return new TestFailureMatcher("not expected ("+errorMessage+")");
    }
    
    /**
     * @since 1.0.1
     */
    public Stub onConsecutiveCalls( Stub stub1, Stub stub2 ) {
        return onConsecutiveCalls(new Stub[]{stub1, stub2});
    }
    
    /**
     * @since 1.0.1
     */
    public Stub onConsecutiveCalls( Stub stub1, Stub stub2, Stub stub3 ) {
        return onConsecutiveCalls(new Stub[]{stub1, stub2, stub3});
    }
    
    /**
     * @since 1.0.1
     */
    public Stub onConsecutiveCalls( Stub stub1, Stub stub2, Stub stub3, Stub stub4 ) {
        return onConsecutiveCalls(new Stub[]{stub1, stub2, stub3, stub4});
    }
    
    /** 
     * @since 1.1.0
     */
    public Stub onConsecutiveCalls( Stub[] stubs ) {
        return new StubSequence(stubs);
    }

    /** 
     * @since 1.1.0
     */
    public Stub doAll(Stub stub1, Stub stub2) {
        return doAll(new Stub[]{stub1, stub2});
    }

    /** 
     * @since 1.1.0
     */
    public Stub doAll(Stub stub1, Stub stub2, Stub stub3) {
        return doAll(new Stub[]{stub1, stub2, stub3});
    }

    /** 
     * @since 1.1.0
     */
    public Stub doAll(Stub stub1, Stub stub2, Stub stub3, Stub stub4) {
        return doAll(new Stub[]{stub1, stub2, stub3, stub4});
    }

    /** 
     * @since 1.1.0
     */
    public Stub doAll(Stub[] stubs) {
        return new DoAllStub(stubs);
    }
}
