/*
 * Created on 01-Dec-2003
 * 
 */
package org.jmock.constraint;

import org.jmock.Constraint;

public class StringContains 
	implements Constraint 
{
	private String excerpt;
	
	public StringContains(String excerpt) {
		this.excerpt = excerpt;
	}

	public boolean eval( Object o ) {
		return o instanceof String && ((String)o).indexOf(excerpt) >= 0;
	}
	
	public String toString() {
		return "a string containing \"" + excerpt + "\"";
	}
}
