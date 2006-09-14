package org.jmock.lib.action;

import org.hamcrest.Description;
import org.jmock.core.Action;
import org.jmock.core.Invocation;

public class DoAllAction implements Action {
    private final Action[] actions;
    
    public DoAllAction(Action... actions) {
        this.actions = actions.clone();
    }
    
    public Object invoke(Invocation invocation) throws Throwable {
        Object result = null;
        
        for (int i = 0; i < actions.length; i++) {
            result = actions[i].invoke(invocation);
        }
        
        return result;
    }
    
    public void describeTo(Description description) {
        description.appendText("do all of ");
        for (int i = 0; i < actions.length; i++) {
            if (i > 0) description.appendText(", ");
            actions[i].describeTo(description);
        }
    }
}
