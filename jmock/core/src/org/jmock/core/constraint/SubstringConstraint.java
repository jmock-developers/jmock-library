package org.jmock.core.constraint;

import org.jmock.core.Constraint;
import org.jmock.core.Formatting;

public abstract class SubstringConstraint implements Constraint {
    protected final String substring;

    protected SubstringConstraint(final String substring) {
        this.substring = substring;
    }

    public boolean eval(Object o) {
        return o instanceof String && evalSubstringOf((String) o);
    }

    public StringBuffer describeTo(StringBuffer buffer) {
        return buffer.append("a string ")
                     .append(relationship())
                     .append(" ")
                     .append(Formatting.toReadableString(substring));
    }

    protected abstract boolean evalSubstringOf(String string);

    protected abstract String relationship();
}