/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.constraint;

import java.beans.IntrospectionException;

import org.jmock.core.Constraint;
import org.jmock.util.PropertyUtil;

/**
 * Constraint that checks that an object has a JavaBean property
 * with the specified name. If an error occurs while introspecting
 * the object then this is treated as a constraint failure.
 * 
 * @author Iain McGinniss
 * @author Nat Pryce
 * @author Steve Freeman
 * @since 1.1.0
 */
public class HasProperty implements Constraint {

	private String propertyName;
	
	public HasProperty(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public boolean eval(Object obj) {
		try {
			return PropertyUtil.getPropertyDescriptor(propertyName, obj) != null;
		} catch (IntrospectionException e) {
			// introspection failure is treated as a constraint failure
			return false;
		}
	}

	public StringBuffer describeTo(StringBuffer buffer) {
		buffer.append("hasProperty(\"");
		buffer.append(propertyName);
		buffer.append("\")");
		
		return buffer;
	}

}
