/*
 * Created on 15-Jun-2004
 */
package org.jmock.core;


public interface InvokablesIterator {
	boolean hasMore();
	Invokable next();
}