/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.builder;

import org.jmock.core.Constraint;

/**
 * @since 1.0
 */
public interface ArgumentsMatchBuilder extends MatchBuilder
{
    MatchBuilder with( Constraint[] argumentConstraints );

    MatchBuilder with( Constraint arg1 );

    MatchBuilder with( Constraint arg1, Constraint arg2 );

    MatchBuilder with( Constraint arg1, Constraint arg2, Constraint arg3 );

    MatchBuilder with( Constraint arg1, Constraint arg2, Constraint arg3, Constraint arg4 );

    MatchBuilder withNoArguments();

    MatchBuilder withAnyArguments();
}
