package org.jmock.builder;

import org.jmock.Constraint;


public interface MatchBuilder extends StubBuilder {
	StubBuilder args( Constraint[] argumentConstraints );
	StubBuilder args(Constraint arg1);
	StubBuilder args(Constraint arg1, Constraint arg2);
    StubBuilder args(Constraint arg1, Constraint arg2, Constraint arg3);
    StubBuilder args(Constraint arg1, Constraint arg2, Constraint arg3, Constraint arg4);
    
	StubBuilder noParams();
    StubBuilder anyParams();
}
