/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.constraint;

import org.jmock.Constraint;

/**
 * Calculates the logical conjunction of two constraints. Evaluation is
 * shortcut, so that the second constraint is not called if the first
 * constraint returns <code>false</code>.
 */
public class And implements Constraint {
	Constraint left, right;

	public And(Constraint left, Constraint right) {
		this.left = left;
		this.right = right;
	}

	public boolean eval(Object o) {
		return left.eval(o) && right.eval(o);
	}

	public String toString() {
		return "(" + left + " and " + right + ")";
	}
}
