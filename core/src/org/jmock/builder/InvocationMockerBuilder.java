/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.Constraint;
import org.jmock.dynamic.InvocationMatcher;
import org.jmock.dynamic.Stub;
import org.jmock.dynamic.StubMatchersCollection;
import org.jmock.dynamic.matcher.*;
import org.jmock.dynamic.stub.ReturnStub;
import org.jmock.dynamic.stub.TestFailureStub;
import org.jmock.dynamic.stub.ThrowStub;
import org.jmock.dynamic.stub.VoidStub;


public class InvocationMockerBuilder 
    implements NameMatchBuilder
{
    private StubMatchersCollection mocker;
    private BuilderIdentityTable idTable;
    
    public InvocationMockerBuilder( StubMatchersCollection mocker, 
									BuilderIdentityTable idTable ) 
    {
        this.mocker = mocker;
        this.idTable = idTable;
    }
    
    public ParameterMatchBuilder method(Constraint nameConstraint) {
        return addMatcher( new MethodNameMatcher(nameConstraint) );
    }
    
    public ParameterMatchBuilder method(String name) {
        addMatcher( new MethodNameMatcher(name) );
        idTable.registerID( name, this );
        return this;
    }
    
    public MatchBuilder match( InvocationMatcher customMatcher ) {
        return addMatcher(customMatcher);
    }
    
	public MatchBuilder with(Constraint arg1) {
		return with(new Constraint[]{arg1});
	}

	public MatchBuilder with(Constraint arg1, Constraint arg2) {
		return with(new Constraint[] {arg1, arg2} );
	}
	
    public MatchBuilder with(Constraint arg1, Constraint arg2, Constraint arg3) {
        return with(new Constraint[] {arg1, arg2, arg3} );
    }
    
    public MatchBuilder with(Constraint arg1, Constraint arg2, Constraint arg3, Constraint arg4) {
        return with(new Constraint[] {arg1, arg2, arg3, arg4} );
    }
    
    public MatchBuilder with(Constraint[] constraints) {
        return addMatcher(new ArgumentsMatcher(constraints));
	}

	public MatchBuilder noParams() {
        return addMatcher(NoArgumentsMatcher.INSTANCE);
    }
    
    public MatchBuilder anyParams() {
        return addMatcher(AnyArgumentsMatcher.INSTANCE);
    }
    
    public IdentityBuilder stub(Stub stub) {
    	return will(stub);
    }
    
    public IdentityBuilder will(Stub stubAction) {
        mocker.setStub(stubAction);
        return this;
    }
    
    public IdentityBuilder isVoid() {
    	return stub(VoidStub.INSTANCE);
    }
    
    public IdentityBuilder willReturn(boolean returnValue) {
        return willReturn(new Boolean(returnValue));
    }

    public IdentityBuilder willReturn(byte returnValue) {
        return willReturn(new Byte(returnValue));
    }

    public IdentityBuilder willReturn(char returnValue) {
        return willReturn(new Character(returnValue));
    }

    public IdentityBuilder willReturn(short returnValue) {
        return willReturn(new Short(returnValue));
    }

    public IdentityBuilder willReturn(int returnValue) {
        return willReturn(new Integer(returnValue));
    }

    public IdentityBuilder willReturn(long returnValue) {
        return willReturn(new Long(returnValue));
    }

    public IdentityBuilder willReturn(float returnValue) {
        return willReturn(new Float(returnValue));
    }
    
    public IdentityBuilder willReturn(double returnValue) {
        return willReturn(new Double(returnValue));
    }
    
    public IdentityBuilder willReturn(Object returnValue) {
        return stub(new ReturnStub(returnValue));
    }
    
    public IdentityBuilder willThrow(Throwable throwable) {
        return stub(new ThrowStub(throwable));
    }
    
    public IdentityBuilder expect( InvocationMatcher expectation ) {
        return addMatcher( expectation );
    }
    
    public IdentityBuilder addExpectation( InvocationMatcher expectation ) {
        return expect( expectation );
    }
    
	public IdentityBuilder expectOnce() {
		return addExpectation( new InvokeOnceMatcher() );
	}
	
	public IdentityBuilder expectAtLeastOnce() {
		return addExpectation( new InvokeAtLeastOnceMatcher() );
	}
	
    public IdentityBuilder expectNotCalled() {
        return stub( new TestFailureStub("must not be called") );
    }
    
	public void id( String invocationID ) {
        mocker.setName(invocationID);
		idTable.registerUniqueID( invocationID, this );
	}
	
    public MatchBuilder after( String priorCallID ) {
    	setupOrderingMatchers( idTable, priorCallID, priorCallID );
        return this;
    }
    
    public MatchBuilder after( BuilderIdentityTable otherMock, String priorCallID ) {
    	setupOrderingMatchers( otherMock, priorCallID, priorCallID + " on " + otherMock );
    	return this;
    }
    
    private void setupOrderingMatchers( BuilderIdentityTable priorMockObject, 
										String priorCallID, 
										String priorCallDescription ) 
    {
		MatchBuilder priorCallBuilder = priorMockObject.lookupID(priorCallID);
    	InvokedRecorder priorCallRecorder = new InvokedRecorder();
    	
    	priorCallBuilder.match(priorCallRecorder);
    	mocker.addMatcher(new InvokedAfterMatcher( priorCallRecorder,
    	                                           priorCallDescription));
	}
    
	private InvocationMockerBuilder addMatcher(InvocationMatcher matcher) {
        mocker.addMatcher(matcher);
        return this;
    }
}
