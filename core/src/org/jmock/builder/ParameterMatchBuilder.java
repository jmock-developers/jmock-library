package org.jmock.builder;

import org.jmock.core.Constraint;


public interface ParameterMatchBuilder extends MatchBuilder {
	MatchBuilder with( Constraint[] argumentConstraints );
	MatchBuilder with(Constraint arg1);
	MatchBuilder with(Constraint arg1, Constraint arg2);
    MatchBuilder with(Constraint arg1, Constraint arg2, Constraint arg3);
    MatchBuilder with(Constraint arg1, Constraint arg2, Constraint arg3, Constraint arg4);
    
	MatchBuilder noParams();
    MatchBuilder anyParams();
}
