/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core.constraint;

import org.jmock.core.Constraint;


/**
 * Tests if the argument is a string that contains a substring.
 */
public class StringContains implements Constraint
{
	private String substring;

	public StringContains( String substring ) {
		this.substring = substring;
	}

	public boolean eval( Object o ) {
		return o instanceof String && ((String)o).indexOf(substring) >= 0;
	}


	public StringBuffer describeTo( StringBuffer buffer ) {
		return buffer.append("a string containing \"")
		        .append(substring)
		        .append("\"");
	}
}
