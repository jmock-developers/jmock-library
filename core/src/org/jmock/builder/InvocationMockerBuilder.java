/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.core.Constraint;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.Stub;
import org.jmock.core.StubMatchersCollection;
import org.jmock.core.matcher.*;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.TestFailureStub;
import org.jmock.core.stub.ThrowStub;
import org.jmock.core.stub.VoidStub;


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
        idTable.registerMethodName( name, this );
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
    	setupOrderingMatchers( idTable.lookupIDForSameMock(priorCallID), 
                               priorCallID, priorCallID );
        return this;
    }
    
    public MatchBuilder after( BuilderIdentityTable otherMock, String priorCallID ) {
    	setupOrderingMatchers( otherMock.lookupIDForOtherMock(priorCallID), 
                               priorCallID, priorCallID + " on " + otherMock );
    	return this;
    }
    
    private void setupOrderingMatchers( MatchBuilder priorCallBuilder, 
										String priorCallID, 
										String priorCallDescription ) 
    {
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
