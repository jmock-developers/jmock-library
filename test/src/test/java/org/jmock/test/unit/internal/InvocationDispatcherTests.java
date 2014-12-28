package org.jmock.test.unit.internal;

import junit.framework.TestCase;

import org.jmock.api.ExpectationError;
import org.jmock.api.Invocation;
import org.jmock.internal.InvocationDispatcher;
import org.jmock.test.unit.support.MethodFactory;
import org.jmock.test.unit.support.MockExpectation;

public class InvocationDispatcherTests extends TestCase {
	MethodFactory methodFactory = new MethodFactory();
	Invocation invocation = new Invocation(
	    "invokedObject", 
		methodFactory.newMethod("invokedMethod"),
		Invocation.NO_PARAMETERS);
	
	static final boolean NOT_RELEVANT = true;
	
    public void testInvokesFirstMatchingExpectationInGroup() throws Throwable {
        MockExpectation expectation1 = new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT);
        MockExpectation expectation2 = new MockExpectation(true, NOT_RELEVANT, NOT_RELEVANT);
        MockExpectation expectation3 = new MockExpectation(true, NOT_RELEVANT, NOT_RELEVANT);
        
        InvocationDispatcher dispatcher = new InvocationDispatcher();
        dispatcher.add(expectation1);
        dispatcher.add(expectation2);
        dispatcher.add(expectation3);
        
        expectation1.shouldNotBeInvoked();
        expectation2.shouldBeInvokedWith(invocation);
        expectation3.shouldNotBeInvoked();
        
        dispatcher.dispatch(invocation);
        
        assertTrue("expectation2 should have been invoked", 
                   expectation2.wasInvoked);
    }

    public void testThrowsExpectationErrorIfNoExpectationsMatchAnInvocation() throws Throwable {
        MockExpectation expectation1 = new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT);
        MockExpectation expectation2 = new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT);
        MockExpectation expectation3 = new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT);
        
        InvocationDispatcher dispatcher = new InvocationDispatcher();
        dispatcher.add(expectation1);
        dispatcher.add(expectation2);
        dispatcher.add(expectation3);
        
        expectation1.shouldNotBeInvoked();
        expectation2.shouldNotBeInvoked();
        expectation3.shouldNotBeInvoked();
        
        try {
            dispatcher.dispatch(invocation);
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            // expected
        }
    }

    public void testIsSatisfiedOnlyIfAllExpectationsAreSatisfied() {
        InvocationDispatcher dispatcherAll = new InvocationDispatcher();
        dispatcherAll.add(new MockExpectation(NOT_RELEVANT, true, NOT_RELEVANT));
        dispatcherAll.add(new MockExpectation(NOT_RELEVANT, true, NOT_RELEVANT));
        assertTrue("should be satisfied if all expectations are satisfied",
                   dispatcherAll.isSatisfied());
        
        InvocationDispatcher dispatcher1 = new InvocationDispatcher();
        dispatcher1.add(new MockExpectation(NOT_RELEVANT, true, NOT_RELEVANT));
        dispatcher1.add(new MockExpectation(NOT_RELEVANT, false, NOT_RELEVANT));
        assertFalse("should not be satisfied if first expectation is not satisfied",
                   dispatcher1.isSatisfied());
        
        InvocationDispatcher dispatcher2 = new InvocationDispatcher();
        dispatcher2.add(new MockExpectation(NOT_RELEVANT, false, NOT_RELEVANT));
        dispatcher2.add(new MockExpectation(NOT_RELEVANT, true, NOT_RELEVANT));
        assertFalse("should not be satisfied if second expectation is not satisfied",
                   dispatcher2.isSatisfied());
        
        InvocationDispatcher dispatcherNone = new InvocationDispatcher();
        dispatcherNone.add(new MockExpectation(NOT_RELEVANT, false, NOT_RELEVANT));
        dispatcherNone.add(new MockExpectation(NOT_RELEVANT, true, NOT_RELEVANT));
        assertFalse("should not be satisfied if no expectations are satisfied",
                    dispatcherNone.isSatisfied());
    }
}
