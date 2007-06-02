/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.builder;

import org.jmock.core.Constraint;


public interface NameMatchBuilder extends ArgumentsMatchBuilder
{
    ArgumentsMatchBuilder method( String name );

    ArgumentsMatchBuilder method( Constraint nameConstraint );
}
