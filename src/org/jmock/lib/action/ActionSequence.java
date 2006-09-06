/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.lib.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.hamcrest.Description;
import org.jmock.core.Action;
import org.jmock.core.Invocation;


public class ActionSequence implements Action {
    List<Action> actions;
    Iterator<Action> iterator;
    
    public ActionSequence(Action... actions) {
        this.actions = new ArrayList<Action>(Arrays.asList(actions));
        this.iterator = this.actions.iterator();
    }
    
    public Object invoke( Invocation invocation ) throws Throwable {
        if (iterator.hasNext()) {
            return iterator.next().invoke(invocation);
        } 
    	throw new AssertionFailedError("no more actions available");
    }
    
    public void describeTo(Description description) {
        for (int i = 0; i < actions.size(); i++) {
            if (i > 0) description.appendText(", and then ");
            actions.get(i).describeTo(description);
        }
    }
}
