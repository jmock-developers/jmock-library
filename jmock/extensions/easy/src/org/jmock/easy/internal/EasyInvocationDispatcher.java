/*
 * Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.easy.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.jmock.core.InvocationMocker;
import org.jmock.core.Invokable;
import org.jmock.core.InvokablesIterator;
import org.jmock.core.OrderedInvocationDispatcher;

public class EasyInvocationDispatcher extends OrderedInvocationDispatcher {
	private List expectations = new ArrayList();
    private List defaults = new ArrayList();
    
	public InvokablesIterator iterator() {
        final ArrayList all = new ArrayList(expectations);
        all.addAll(defaults);
        
		return new InvokablesIterator() {
			ListIterator i = all.listIterator();

			public boolean hasMore() {
				return i.hasNext();
			}
			public Invokable next() {
				return (Invokable) i.next();
			}
		};
	}
	public void add(InvocationMocker mocker) {
		expectations.add(mocker);
	}
	public void addDefault(InvocationMocker mocker) {
		defaults.add(mocker);
	}
}