/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core.constraint;

import java.util.EventObject;

import org.jmock.core.Constraint;

/**
 * Tests if the value is an event announced by a specific object.
 */
public class IsEventFrom implements Constraint {
	private Class eventClass;
	private Object source;

	/**
	 * Constructs an IsEventFrom predicate that returns true for any object
	 * derived from {@link java.util.EventObject}announced by <var>source
	 * </var>.
	 */
	public IsEventFrom(Object source) {
		this(EventObject.class, source);
	}

	/**
	 * Constructs an IsEventFrom predicate that returns true for any object
	 * derived from <var>event_class</var> announced by <var>source</var>.
	 */
	public IsEventFrom(Class eventClass, Object source) {
		this.eventClass = eventClass;
		this.source = source;
	}

	public boolean eval(Object o) {
		if (o instanceof EventObject) {
			EventObject ev = (EventObject) o;
			return eventClass.isInstance(o) && ev.getSource() == source;
		} else {
			return false;
		}
	}

	public String toString() {
		return "an event of type " + eventClass.getName() + " from " + source;
	}
}
