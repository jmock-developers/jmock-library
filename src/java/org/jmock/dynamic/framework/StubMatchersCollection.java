package org.jmock.dynamic.framework;

public interface StubMatchersCollection {
	public abstract void addMatcher(InvocationMatcher matcher);
	public abstract void setStub(Stub stub);
}