/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.core.Stub;


public interface StubBuilder extends IdentityBuilder
{
	IdentityBuilder will( Stub stubAction );

	IdentityBuilder isVoid();
}
