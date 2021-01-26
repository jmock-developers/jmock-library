package uk.jamesdal.perfmock.lib.action;

import java.util.Arrays;

import org.hamcrest.Description;
import uk.jamesdal.perfmock.api.Action;
import uk.jamesdal.perfmock.api.Invocation;

/**
 * Performs multiple actions every time it is invoked.
 * 
 * @author nat
 *
 */
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
        description.appendList("do all of ", ", ", "", Arrays.asList(actions));
    }
}
