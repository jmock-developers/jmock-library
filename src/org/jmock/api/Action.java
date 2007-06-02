/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.api;

import org.hamcrest.SelfDescribing;



/**
 * An object that fakes the behaviour of an {@link org.jmock.internal.InvocationExpectation}.
 */
public interface Action extends SelfDescribing, Invokable {
}
