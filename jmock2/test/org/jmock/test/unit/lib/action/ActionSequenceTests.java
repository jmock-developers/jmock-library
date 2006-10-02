/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.test.unit.lib.action;

import java.lang.reflect.Method;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.core.Action;
import org.jmock.core.Invocation;
import org.jmock.lib.action.ActionSequence;
import org.jmock.test.unit.support.AssertThat;
import org.jmock.test.unit.support.GetDescription;
import org.jmock.test.unit.support.MethodFactory;
import org.jmock.test.unit.support.MockAction;


public class ActionSequenceTests extends TestCase {
    private Object invokedObject = "INVOKED_OBJECT";
    private MethodFactory methodFactory = new MethodFactory();
    private Method invokedMethod = methodFactory.newMethodReturning(String.class);
    private Invocation invocation = new Invocation(invokedObject, invokedMethod);
    
    
    @SuppressWarnings("cast") // Eclipse gives warning if there is a cast and if there is not!
    public void testInvokesActionsInOrder() throws Throwable {
    	final int sequenceLength = 4;
    	
        MockAction[] actions = new MockAction[sequenceLength];        
        for (int i = 0; i < sequenceLength; i++) {
            actions[i] = new MockAction();
            actions[i].result = "RESULT-" + i;
            if (i > 0) actions[i].previous = actions[i-1];
        }
        
        Invocation[] invocations = new Invocation[actions.length];
        for (int i = 0; i < sequenceLength; i++) {
        	invocations[i] = new Invocation(invokedObject, invokedMethod);
        }
        
        ActionSequence sequence = new ActionSequence((Action[])actions);
        
        for (int current = 0; current < actions.length; current++) {
            reset(actions);
            actions[current].expectInvoke = true;
            actions[current].expectedInvocation = invocation;
            
            Object result = sequence.invoke(invocation);
            
			assertSame("should be result of actions[" + current + "]",
                         actions[current].result, result);
        }
    }
    
    @SuppressWarnings("cast") // Eclipse gives warning if there is a cast and if there is not!
    public void testThrowsAssertionFailedErrorIfInvokedMoreTimesThanThereAreActionsInTheSequence() throws Throwable {
        MockAction[] actions = new MockAction[]{new MockAction(), new MockAction()};
        ActionSequence sequence = new ActionSequence((Action[])actions);

        for (int i = 0; i < actions.length; i++) sequence.invoke(invocation);

        try {
            sequence.invoke(invocation);
        }
        catch (AssertionFailedError ex) {
            AssertThat.stringIncludes("should describe error",
                "no more actions", ex.getMessage());
        }
    }
    
    @SuppressWarnings("cast") // Eclipse gives warning if there is a cast and if there is not!
    public void testDescribesItselfAsSequenceOfActions() throws Throwable {
        MockAction[] actions = new MockAction[]{new MockAction(), new MockAction()};
        ActionSequence sequence = new ActionSequence((Action[])actions);
        
        String sequenceDescription = GetDescription.of(sequence);

        for (int i = 0; i < actions.length; i++) {
            AssertThat.stringIncludes("should include action " + i,
                actions[i].descriptionText, sequenceDescription);

            if (i > 0) {
                int h = i - 1;
                
                assertTrue("description of action " + h + " should be before that of action " + i,
                           sequenceDescription.indexOf(actions[h].descriptionText) <
                           sequenceDescription.indexOf(actions[i].descriptionText));
            }
        }
    }

    private void reset( MockAction[] actions ) {
        for (int i = 0; i < actions.length; i++) {
            actions[i].expectInvoke = false;
        }
    }
}
