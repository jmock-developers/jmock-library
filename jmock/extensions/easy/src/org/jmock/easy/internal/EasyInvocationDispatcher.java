/*
 * Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.easy.internal;

import org.jmock.core.InvocationMocker;
import org.jmock.core.OrderedInvocationDispatcher;

public class EasyInvocationDispatcher extends OrderedInvocationDispatcher.FIFO {

	public void add(InvocationMocker mocker) {
		invokables.add(mocker);
    }
    public void addDefault(InvocationMocker mocker) {
        invokables.add(mocker);
	}
}