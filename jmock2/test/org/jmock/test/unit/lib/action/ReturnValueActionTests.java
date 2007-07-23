/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.test.unit.lib.action;

import junit.framework.TestCase;

import org.hamcrest.StringDescription;
import org.jmock.api.Invocation;
import org.jmock.lib.action.ReturnValueAction;
import org.jmock.test.unit.support.AssertThat;
import org.jmock.test.unit.support.MethodFactory;

public class ReturnValueActionTests extends TestCase {
    static final String RESULT = "result";

    MethodFactory methodFactory;
    Object invokedObject;
    Class<?> invokedObjectClass;
    Invocation invocation;
    ReturnValueAction returnValueAction;

    @Override
    public void setUp() {
        methodFactory = new MethodFactory();

        invokedObject = "INVOKED-OBJECT";
        invokedObjectClass = Void.class;

        returnValueAction = new ReturnValueAction(RESULT);
    }

    public void testReturnsValuePassedToConstructor() throws Throwable {
        invocation = new Invocation(invokedObject, methodFactory.newMethodReturning(RESULT.getClass()));

        assertSame("Should be the same result object", RESULT, returnValueAction
                .invoke(invocation));
    }

    public void testIncludesValueInDescription() {
        String description = StringDescription.toString(returnValueAction);

        AssertThat.stringIncludes("contains result in description", 
        	RESULT.toString(), description);
        AssertThat.stringIncludes("contains 'returns' in description", 
            "returns", description);
    }

    public void testCanReturnNullReference() throws Throwable {
        invocation = new Invocation(invokedObject, methodFactory.newMethodReturning(String.class));

        returnValueAction = new ReturnValueAction(null);

        assertNull("should return null", returnValueAction.invoke(invocation));
    }
}