package org.jmock.builder;

import org.jmock.Constraint;


public interface MatchBuilder extends StubBuilder {
	StubBuilder whenPassed( Constraint[] argumentConstraints );
	StubBuilder whenPassed(Object arg1);
}
