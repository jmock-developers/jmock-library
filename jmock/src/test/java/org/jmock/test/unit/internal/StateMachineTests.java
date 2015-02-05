package org.jmock.test.unit.internal;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.hamcrest.StringDescription;
import org.jmock.States;
import org.jmock.internal.StateMachine;

public class StateMachineTests extends TestCase {
    States stateMachine = new StateMachine("stateMachineName");
    
    public void testIsInitiallyInNoState() {
        for (String state : anyState) {
            assertFalse("should not report being in state " + state, 
                        stateMachine.is(state).isActive());
            assertTrue("should report not being in state " + state, 
                       stateMachine.isNot(state).isActive());
        }
    }
    
    public void testCanEnterAState() {
        String state = "A";
        Set<String> otherStates = except(anyState, state);
        
        stateMachine.is(state).activate();
        
        assertTrue("should report being in state " + state, 
                   stateMachine.is(state).isActive());
        assertFalse("should not report not being in state " + state, 
                    stateMachine.isNot(state).isActive());
        
        for (String otherState : otherStates) {
            assertFalse("should not report being in state " + otherState, 
                        stateMachine.is(otherState).isActive());
            assertTrue("should report not being in state " + otherState, 
                       stateMachine.isNot(otherState).isActive());
        }
    }

    public void testCanChangeState() {
        String state = "B";
        Set<String> otherStates = except(anyState, state);
        
        stateMachine.is("A").activate();
        stateMachine.is(state).activate();
        
        assertTrue("should report being in state " + state, 
                   stateMachine.is(state).isActive());
        assertFalse("should not report not being in state " + state, 
                    stateMachine.isNot(state).isActive());
        
        for (String otherState : otherStates) {
            assertFalse("should not report being in state " + otherState, 
                        stateMachine.is(otherState).isActive());
            assertTrue("should report not being in state " + otherState, 
                       stateMachine.isNot(otherState).isActive());
        }
    }

    public void testCanBePutIntoAnInitialState() {
        String initialState = "A";
        Set<String> otherStates = except(anyState, initialState);
        
        stateMachine.startsAs(initialState);
        
        assertTrue("should report being in state " + initialState, 
                   stateMachine.is(initialState).isActive());
        assertFalse("should not report not being in state " + initialState, 
                    stateMachine.isNot(initialState).isActive());
        
        for (String otherState : otherStates) {
            assertFalse("should not report being in state " + otherState, 
                        stateMachine.is(otherState).isActive());
            assertTrue("should report not being in state " + otherState, 
                       stateMachine.isNot(otherState).isActive());
        }
    }
    
    public void testCanBePutIntoANewState() {
        String nextState = "B";
        
        Set<String> otherStates = except(anyState, nextState);
        stateMachine.startsAs("A");
        
        stateMachine.become(nextState);
        
        assertTrue("should report being in state " + nextState, 
                   stateMachine.is(nextState).isActive());
        assertFalse("should not report not being in state " + nextState, 
                    stateMachine.isNot(nextState).isActive());
        
        for (String otherState : otherStates) {
            assertFalse("should not report being in state " + otherState, 
                        stateMachine.is(otherState).isActive());
            assertTrue("should report not being in state " + otherState, 
                       stateMachine.isNot(otherState).isActive());
        }
    }
    
    public void testDescribesItselfAsNameAndCurrentState() {
        assertEquals("description with no current state",
                     "stateMachineName has no current state", StringDescription.toString(stateMachine));
        
        stateMachine.is("stateName").activate();
        
        assertEquals("description with a current state",
                     "stateMachineName is stateName", StringDescription.toString(stateMachine));
        assertEquals("description with a current state from toString",
                     "stateMachineName is stateName", stateMachine.toString());
    }
    
    public void testHasSelfDescribingStates() {
        assertEquals("stateMachineName is A", StringDescription.toString(stateMachine.is("A")));
        assertEquals("stateMachineName is not A", StringDescription.toString(stateMachine.isNot("A")));
    }
    
    private <T> Set<T> except(Set<T> s, T e) {
        Set<T> result = new HashSet<T>(s);
        result.remove(e);
        return result;
    }
    
    private final Set<String> anyState = new HashSet<String>() {{
        add("A");
        add("B");
        add("C");
        add("D");
    }};
}
