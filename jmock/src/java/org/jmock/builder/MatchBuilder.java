package org.jmock.builder;

import org.jmock.Constraint;


public interface MatchBuilder extends StubBuilder {
	StubBuilder passed( Constraint[] argumentConstraints );
    //TODO: add passed methods that take one or more Constraint arguments
	StubBuilder passed(Object arg1);
	StubBuilder passed(Object arg1, Object arg2);
    
	StubBuilder noParams();
    
    //TODO: add an anyParams method
}
