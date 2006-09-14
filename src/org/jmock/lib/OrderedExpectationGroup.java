package org.jmock.lib;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.jmock.core.Expectation;
import org.jmock.core.ExpectationGroup;
import org.jmock.core.Invocation;
import org.jmock.internal.IndentedDescription;


/**
 * An {@link org.jmock.core.ExpectationGroup} in which expectations
 * must be invoked in order.
 * 
 * @author npryce
 *
 */
public class OrderedExpectationGroup implements ExpectationGroup {
	private List<Expectation> expectations = new ArrayList<Expectation>();
	private int current = 0;
    
	public void add(Expectation expectation) {
		expectations.add(expectation);
	}
	
    public void describeTo(Description description) {
        description.appendText("in this order:\n");
        IndentedDescription indentedDescription = new IndentedDescription(description);
        for (Expectation expectation : expectations) {
            expectation.describeTo(indentedDescription);
            indentedDescription.appendText("\n");
        }
    }
    
	public boolean matches(Invocation invocation) {
        return nextIndexMatching(invocation) != -1;
	}
	
    private int nextIndexMatching(Invocation invocation) {
        int i = current;
        
        while(i < expectations.size()) {
            Expectation e = expectations.get(i);
            if (e.matches(invocation)) {
                return i;
            }
            else if (!e.isSatisfied()) {
                return -1;
            }
            i++;
        }
        return -1;
    }

    public boolean isActive() {
        for (int i = current; i < expectations.size(); i++) {
            if (expectations.get(i).isActive()) return true;
        }
        return false;
    }
    
	public boolean isSatisfied() {
        for (int i = current; i < expectations.size(); i++) {
            if (!expectations.get(i).isSatisfied()) return false;
        }
        return true;
	}
	
	public Object invoke(Invocation invocation) throws Throwable {
        current = nextIndexMatching(invocation);
        if (current == -1) {
            throw new IllegalStateException("expectation does not match invocation " + invocation);
        }
        
        return expectations.get(current).invoke(invocation);
	}
}
