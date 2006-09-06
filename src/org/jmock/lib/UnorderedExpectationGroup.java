package org.jmock.lib;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.jmock.core.Expectation;
import org.jmock.core.ExpectationGroup;
import org.jmock.core.Invocation;
import org.jmock.internal.IndentedDescription;


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
	
	public boolean isSatisfied() {
		for (Expectation expectation : expectations) {
		    if (!expectation.isSatisfied()) {
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
