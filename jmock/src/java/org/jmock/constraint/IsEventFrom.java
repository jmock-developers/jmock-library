/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.constraint;

import org.jmock.Constraint;

import java.util.EventObject;

/**
 * Tests if the value is an event announced by a specific object.
 */
public class IsEventFrom
        implements Constraint {
    private Class _event_class;
    private Object _source;

    /**
     * Constructs an IsEventFrom predicate that returns true for any object
     * derived from {@link java.util.EventObject} announced by
     * <var>source</var>.
     */
    public IsEventFrom(Object source) {
        this(EventObject.class, source);
    }

    /**
     * Constructs an IsEventFrom predicate that returns true for any object
     * derived from <var>event_class</var> announced by
     * <var>source</var>.
     */
    public IsEventFrom(Class event_class, Object source) {
        _event_class = event_class;
        _source = source;
    }

    public boolean eval(Object o) {
        if (o instanceof EventObject) {
            EventObject ev = (EventObject) o;
            return _event_class.isInstance(o) && ev.getSource() == _source;

        } else {
            return false;
        }
    }

    public String toString() {
        return "an event of type " + _event_class.getName() +
                " from " + _source;
    }
}
