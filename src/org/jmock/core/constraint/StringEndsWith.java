/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.constraint;

/**
 * Tests if the argument is a string that contains a substring.
 */
public class StringEndsWith extends SubstringConstraint {
    public StringEndsWith(String substring) {
        super(substring);
    }

    protected boolean evalSubstringOf(String s) {
        return s.endsWith(substring);
    }

    protected String relationship() {
        return "ending with";
    }
}
