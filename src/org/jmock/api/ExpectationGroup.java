package org.jmock.api;

/**
 * Dispatches invocations to multiple expectations and defines ordering constraints
 * over the expectations in the group.
 * 
 * @author npryce
 */
public interface ExpectationGroup extends Expectation {
    /**
     * Adds an expectation to the group.
     * 
     * @param expectation
     */
    void add(Expectation expectation);
}
