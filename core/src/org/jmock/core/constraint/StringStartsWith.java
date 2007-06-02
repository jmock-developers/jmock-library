/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.constraint;

/**
 * Tests if the argument is a string that contains a substring.
 */
public class StringStartsWith extends SubstringConstraint {
    public StringStartsWith(String substring) {
        super(substring);
    }

    protected boolean evalSubstringOf(String s) {
        return s.startsWith(substring);
    }

    protected String relationship() {
        return "starting with";
    }
}