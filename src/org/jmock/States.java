package org.jmock;

import org.hamcrest.SelfDescribing;
import org.jmock.syntax.StatesClause;

/**
 * A state machine that is used to constrain the order of invocations.  
 * 
 * An invocation can be constrained to occur when a state is, or is not, active. 
 * 
 * @author nat
 */
public interface States extends SelfDescribing, StatesClause {
    /**
     * Put the state machine into state <var>initialState</var>.
     *  
     * @param initialState
     *     The initial state of the state machine.
     * @return
     *     Itself.
     */
    States startsAs(String initialState);
    
    /**
     * Put the state machine into state <var>nextState</var>.
     *  
     * @param nextState
     *     The next state of the state machine.
     */
    void become(String nextState);
}
