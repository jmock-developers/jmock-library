package org.jmock.builder;

import org.jmock.Constraint;


public interface NameMatchBuilder extends ParameterMatchBuilder {
    ParameterMatchBuilder method( String name );
    ParameterMatchBuilder method( Constraint nameConstraint ); 
}
