/*
 * Created on 15-Jun-2004
 */
package org.jmock.core;



public interface InvokablesCollection {
	public interface Iterator {
		boolean hasMore();
		Invokable next();
	}
	Iterator iterator();
	void add(Invokable invokable);
	void clear();
}