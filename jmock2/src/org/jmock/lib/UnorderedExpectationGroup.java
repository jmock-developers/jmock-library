package org.jmock.lib;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.jmock.api.Expectation;
import org.jmock.api.ExpectationGroup;
import org.jmock.api.Invocation;
import org.jmock.internal.IndentedDescription;

/**
 * An {@link org.jmock.api.ExpectationGroup} in which expectations
 * can be invoked in any order.
 * 
 * @author npryce
 * @author smgf
 */
public class UnorderedExpectationGroup implements ExpectationGroup {
	private List<Expectation> expectations = new ArrayList<Expectation>();
	
	public void add(Expectation expectation) {
		expectations.add(expectation);
	}
	
    public void describeTo(Description description) {
        description.appendText("in any order:\n");
        IndentedDescription indentedDescription = new IndentedDescription(description);
        for (Expectation expectation : expectations) {
            expectation.describeTo(indentedDescription);
            indentedDescription.appendText("\n");
        }
    }
    
	public boolean matches(Invocation invocation) {
		for (Expectation expectation : expectations) {
			if (expectation.matches(invocation)) {
				return true;
			}
		}
		return false;
	}
	
    public boolean allowsMoreInvocations() {
        for (Expectation expectation : expectations) {
            if (expectation.allowsMoreInvocations()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isSatisfied() {
		for (Expectation expectation : expectations) {
		    if (! expectation.isSatisfied()) {
                return false;
            }
        }
        return true;
	}
	
	public Object invoke(Invocation invocation) throws Throwable {
		for (Expectation expectation : expectations) {
		    if (expectation.matches(invocation)) {
		        return expectation.invoke(invocation);
            }
        }
        
        throw new IllegalStateException("expectation does not match invocation " + invocation);
	}
}
