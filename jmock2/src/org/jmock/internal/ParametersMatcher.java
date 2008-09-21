package org.jmock.internal;

import org.hamcrest.Matcher;

public interface ParametersMatcher extends Matcher<Object[]> {
    /*
     * return true iff this parameters matcher <em>could</em> be appropriate for the given parameter list
     */
    boolean isCompatibleWith(Object[] parameters);
}
