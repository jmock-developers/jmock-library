/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import junit.framework.AssertionFailedError;
import org.jmock.core.Constraint;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.Stub;
import org.jmock.core.StubMatchersCollection;
import org.jmock.core.matcher.*;
import org.jmock.core.stub.VoidStub;


public class InvocationMockerBuilder
        implements NameMatchBuilder
{
	private StubMatchersCollection mocker;
	private BuilderNamespace idTable;

	public InvocationMockerBuilder( StubMatchersCollection mocker,
	                                BuilderNamespace idTable ) {
		this.mocker = mocker;
		this.idTable = idTable;
	}

	public ArgumentsMatchBuilder method( Constraint nameConstraint ) {
		return addMatcher(new MethodNameMatcher(nameConstraint));
	}

	public ArgumentsMatchBuilder method( String name ) {
		addMatcher(new MethodNameMatcher(name));
		idTable.registerMethodName(name, this);
		return this;
	}

	public MatchBuilder match( InvocationMatcher customMatcher ) {
		return addMatcher(customMatcher);
	}

	public MatchBuilder with( Constraint arg1 ) {
		return with(new Constraint[]{arg1});
	}

	public MatchBuilder with( Constraint arg1, Constraint arg2 ) {
		return with(new Constraint[]{arg1, arg2});
	}

	public MatchBuilder with( Constraint arg1, Constraint arg2, Constraint arg3 ) {
		return with(new Constraint[]{arg1, arg2, arg3});
	}

	public MatchBuilder with( Constraint arg1, Constraint arg2, Constraint arg3, Constraint arg4 ) {
		return with(new Constraint[]{arg1, arg2, arg3, arg4});
	}

	public MatchBuilder with( Constraint[] constraints ) {
		return addMatcher(new ArgumentsMatcher(constraints));
	}

	public MatchBuilder withNoArguments() {
		return addMatcher(NoArgumentsMatcher.INSTANCE);
	}

	public MatchBuilder withAnyArguments() {
		return addMatcher(AnyArgumentsMatcher.INSTANCE);
	}

	public IdentityBuilder will( Stub stubAction ) {
		setStub(stubAction);
		return this;
	}

	public IdentityBuilder isVoid() {
		setStub(VoidStub.INSTANCE);
		return this;
	}

	private void setStub( Stub stubAction ) {
		mocker.setStub(stubAction);
	}

	public IdentityBuilder expect( InvocationMatcher expectation ) {
		return addMatcher(expectation);
	}

	public void id( String invocationID ) {
		mocker.setName(invocationID);
		idTable.registerUniqueID(invocationID, this);
	}

	public MatchBuilder after( String priorCallID ) {
		setupOrderingMatchers(idTable, priorCallID, priorCallID);
		return this;
	}

	public MatchBuilder after( BuilderNamespace otherMock, String priorCallID ) {
		setupOrderingMatchers(otherMock, priorCallID, priorCallID + " on " + otherMock);
		return this;
	}

	private void setupOrderingMatchers( BuilderNamespace idTable,
	                                    String priorCallID,
	                                    String priorCallDescription ) {
		MatchBuilder priorCallBuilder = idTable.lookupID(priorCallID);

		if (priorCallBuilder == this) {
			throw new AssertionFailedError("confusing identifier of prior invocation \"" + priorCallID + "\"; " +
			                               "give it an explicit call identifier");
		}

		InvokedRecorder priorCallRecorder = new InvokedRecorder();

		priorCallBuilder.match(priorCallRecorder);

		mocker.addMatcher(new InvokedAfterMatcher(priorCallRecorder,
		                                          priorCallDescription));
	}

	private InvocationMockerBuilder addMatcher( InvocationMatcher matcher ) {
		mocker.addMatcher(matcher);
		return this;
	}
}
