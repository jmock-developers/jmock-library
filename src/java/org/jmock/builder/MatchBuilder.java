package org.jmock.builder;

import org.jmock.Constraint;


public interface MatchBuilder extends StubBuilder {
	StubBuilder passed( Constraint[] argumentConstraints );
	StubBuilder passed(Object arg1);
	StubBuilder passed(Object arg1, Object arg2);
	StubBuilder noParams();
}
