/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.stub;

import junit.framework.TestCase;

import org.jmock.core.Invocation;
import org.jmock.core.stub.ReturnStub;

import test.jmock.core.testsupport.MethodFactory;

public class ReturnStubTest extends TestCase {
    static final String RESULT = "result";

    MethodFactory methodFactory;

    Object invokedObject;

    Class invokedObjectClass;

    Invocation invocation;

    ReturnStub returnStub;

    public void setUp() {
        methodFactory = new MethodFactory();

        invokedObject = "INVOKED-OBJECT";
        invokedObjectClass = Void.class;

        returnStub = new ReturnStub(RESULT);
    }

    public void testReturnsValuePassedToConstructor() throws Throwable {
        invocation = new Invocation(invokedObject, methodFactory
                .newMethodReturning(RESULT.getClass()), null);

        assertSame("Should be the same result object", RESULT, returnStub
                .invoke(invocation));
    }

    public void testIncludesValueInDescription() {
        StringBuffer buffer = new StringBuffer();

        returnStub.describeTo(buffer);

        String description = buffer.toString();

        assertTrue("contains result in description", description.indexOf(RESULT
                .toString()) >= 0);
        assertTrue("contains 'returns' in description", description
                .indexOf("returns") >= 0);
    }

    public void testCanReturnNullReference() throws Throwable {
        invocation = new Invocation(invokedObject, methodFactory
                .newMethodReturning(String.class), null);

        returnStub = new ReturnStub(null);

        assertNull("should return null", returnStub.invoke(invocation));
    }
}