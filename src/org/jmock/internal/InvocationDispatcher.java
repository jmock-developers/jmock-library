package org.jmock.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Mismatchable;
import org.hamcrest.SelfDescribing;
import org.jmock.api.Expectation;
import org.jmock.api.ExpectationError;
import org.jmock.api.Invocation;

public class InvocationDispatcher implements ExpectationCollector, Mismatchable {
	private List<Expectation> expectations = new ArrayList<Expectation>();
	private List<StateMachine> stateMachines = new ArrayList<StateMachine>();
    
    public StateMachine newStateMachine(String name) {
        StateMachine stateMachine = new StateMachine(name);
        stateMachines.add(stateMachine);
        return stateMachine;
    }
    
	public void add(Expectation expectation) {
		expectations.add(expectation);
	}
	
    public void describeTo(Description description) {
        if (expectations.isEmpty()) {
            description.appendText("no expectations specified: did you...\n"+
                                   " - forget to start an expectation with a cardinality clause?\n" +
                                   " - call a mocked method to specify the parameter of an expectation?");
        }
        else {
            description.appendList("expectations:\n  ", "\n  ", "", expectations);
            if (!stateMachines.isEmpty()) {
                description.appendList("\nstates:\n  ", "\n  ", "", stateMachines);
            }
        }
    }
    
    public void describeMismatch(Object invocation, Description description) {
        if (expectations.isEmpty()) {
            description.appendText("no expectations specified: did you...\n"+
                                   " - forget to start an expectation with a cardinality clause?\n" +
                                   " - call a mocked method to specify the parameter of an expectation?");
        }
        else {
            description.appendList("expectations:\n  ", "\n  ", "", iterable(invocation, expectations));
            if (!stateMachines.isEmpty()) {
                description.appendList("\nstates:\n  ", "\n  ", "", stateMachines);
            }
        }
        
    }

    private Iterable<SelfDescribing> iterable(final Object invocation, List<Expectation> expectations) {
        final Iterator<Expectation> iterator = expectations.iterator();
        return new Iterable<SelfDescribing>() {
            public Iterator<SelfDescribing> iterator() {
                return new Iterator<SelfDescribing>() {
                    public boolean hasNext() { return iterator.hasNext(); }
                    public SelfDescribing next(){
                        return new SelfDescribing() {
                            public void describeTo(Description description) {
                                iterator.next().describeMismatch(invocation, description);
                            }                            
                        };
                    }
                    public void remove() { iterator.remove(); }
                };
            }
            
        };
    }


    public boolean isSatisfied() {
		for (Expectation expectation : expectations) {
		    if (! expectation.isSatisfied()) {
                return false;
            }
        }
        return true;
	}
	
	public Object dispatch(Invocation invocation) throws Throwable {
		for (Expectation expectation : expectations) {
		    if (expectation.matches(invocation)) {
		        return expectation.invoke(invocation);
            }
        }
        
        throw new ExpectationError("unexpected invocation", invocation);
	}

}
