package org.jmock.builder;

import org.jmock.Constraint;


public interface ParameterMatchBuilder extends StubBuilder {
	StubBuilder with( Constraint[] argumentConstraints );
	StubBuilder with(Constraint arg1);
	StubBuilder with(Constraint arg1, Constraint arg2);
    StubBuilder with(Constraint arg1, Constraint arg2, Constraint arg3);
    StubBuilder with(Constraint arg1, Constraint arg2, Constraint arg3, Constraint arg4);
    
	StubBuilder noParams();
    StubBuilder anyParams();
}
