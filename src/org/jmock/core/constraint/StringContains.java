/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.constraint;

/**
 * Tests if the argument is a string that contains a substring.
 */
public class StringContains extends SubstringConstraint {
    public StringContains(String substring) {
        super(substring);
    }

    protected boolean evalSubstringOf(String s) {
        return s.indexOf(substring) >= 0;
    }

    protected String relationship() {
        return "containing";
    }
}