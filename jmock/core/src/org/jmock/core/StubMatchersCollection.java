package org.jmock.core;

public interface StubMatchersCollection {
    void setName( String name );
	void addMatcher(InvocationMatcher matcher);
	void setStub(Stub stub);
}
