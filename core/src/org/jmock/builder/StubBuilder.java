/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.builder;

import org.jmock.core.Stub;

/**
 * @since 1.0
 */
public interface StubBuilder extends IdentityBuilder
{
    IdentityBuilder will( Stub stubAction );

    IdentityBuilder isVoid();
}
