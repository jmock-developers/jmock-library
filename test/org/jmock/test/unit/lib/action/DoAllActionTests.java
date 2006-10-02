package org.jmock.test.unit.lib.action;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.jmock.core.Action;
import org.jmock.core.Invocation;
import org.jmock.lib.action.DoAllAction;
import org.jmock.test.unit.support.AssertThat;
import org.jmock.test.unit.support.GetDescription;
import org.jmock.test.unit.support.MethodFactory;
import org.jmock.test.unit.support.MockAction;

public class DoAllActionTests extends TestCase {
    private Object invokedObject = "INVOKED_OBJECT";
    private MethodFactory methodFactory = new MethodFactory();
    private Method invokedMethod = methodFactory.newMethodReturning(String.class);
    private Invocation invocation = new Invocation(invokedObject, invokedMethod);

    private MockAction[] actions = new MockAction[4];
	private DoAllAction doAllAction;
    
    @SuppressWarnings("cast") // Eclipse gives warning if there is a cast and if there is not!
    public void setUp() {
        for (int i = 0; i < actions.length; i++) {
            actions[i] = new MockAction();
            actions[i].descriptionText = "actions["+i+"]";
            actions[i].result = actions[i].descriptionText+".result";
            actions[i].expectedInvocation = invocation;
            if (i > 0) actions[i].previous = actions[i-1];
        }
        
        doAllAction = new DoAllAction((Action[])actions);
    }
    
    public void testPerformsAllActionsInOrder() throws Throwable {
        doAllAction.invoke(invocation);
        
        for (MockAction action : actions) {
        	assertTrue(action.descriptionText + " should have been invoked",
        			   action.wasInvoked);
        }
    }
    
    public void testReturnsResultOfLastAction() throws Throwable {
        Object expectedResult = actions[actions.length-1].result;
        Object actualResult = doAllAction.invoke(invocation);
        
        assertEquals("result", expectedResult, actualResult);
    }
    
    public void testDescribesAllActionsInDescription() {
        String description = GetDescription.of(doAllAction);
        
        AssertThat.stringIncludes("description should contain list of actions",
        	"actions[0], actions[1], actions[2], actions[3]", description);
    }
}
