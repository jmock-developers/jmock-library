/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core.constraint;

import org.jmock.core.Constraint;

/**
 * Calculates the logical disjunction of two constraints. Evaluation is
 * shortcut, so that the second constraint is not called if the first
 * constraint returns <code>true</code>.
 */
public class Or implements Constraint {
	Constraint left, right;
	
	public Or(Constraint left, Constraint right) {
		this.left = left;
		this.right = right;
	}
	
	public boolean eval(Object o) {
		return left.eval(o) || right.eval(o);
	}
	
    public StringBuffer describeTo(StringBuffer buffer) {
        buffer.append("(");
        left.describeTo(buffer);
        buffer.append(" or ");
        right.describeTo(buffer);
        buffer.append(")");
        return buffer;
    }
}
