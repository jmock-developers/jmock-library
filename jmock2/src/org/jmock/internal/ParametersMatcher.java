package org.jmock.internal;

import org.hamcrest.Matcher;

public interface ParametersMatcher extends Matcher<Object[]> {
    /**
     * Is this matcher likely to be relevant to the given parameters?
     * @param parameters The parameters to be matched
     * @return true iff the parameters may be relevant.
     */
    boolean isCompatibleWith(Object[] parameters); 
}
