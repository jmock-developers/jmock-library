/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.Constraint;
import org.jmock.dynamic.InvocationMatcher;
import org.jmock.dynamic.Stub;
import org.jmock.dynamic.StubMatchersCollection;
import org.jmock.dynamic.matcher.AnyArgumentsMatcher;
import org.jmock.dynamic.matcher.ArgumentsMatcher;
import org.jmock.dynamic.matcher.InvokeOnceMatcher;
import org.jmock.dynamic.matcher.InvokedAfterMatcher;
import org.jmock.dynamic.matcher.InvokedRecorder;
import org.jmock.dynamic.matcher.NoArgumentsMatcher;
import org.jmock.dynamic.stub.ReturnStub;
import org.jmock.dynamic.stub.ThrowStub;
import org.jmock.dynamic.stub.VoidStub;

public class InvocationMockerBuilder 
    implements MatchBuilder, StubBuilder, ExpectationBuilder
{
    private StubMatchersCollection mocker;
    private BuilderIdentityTable idTable;
    
    public InvocationMockerBuilder( StubMatchersCollection mocker, 
									BuilderIdentityTable idTable ) 
    {
        this.mocker = mocker;
        this.idTable = idTable;
    }
    
	public StubBuilder with(Constraint arg1) {
		return with(new Constraint[]{arg1});
	}

	public StubBuilder with(Constraint arg1, Constraint arg2) {
		return with(new Constraint[] {arg1, arg2} );
	}
	
    public StubBuilder with(Constraint arg1, Constraint arg2, Constraint arg3) {
        return with(new Constraint[] {arg1, arg2, arg3} );
    }
    
    public StubBuilder with(Constraint arg1, Constraint arg2, Constraint arg3, Constraint arg4) {
        return with(new Constraint[] {arg1, arg2, arg3, arg4} );
    }
    
    public StubBuilder with(Constraint[] constraints) {
        return addMatcher(new ArgumentsMatcher(constraints));
	}

	public StubBuilder noParams() {
        return addMatcher(NoArgumentsMatcher.INSTANCE);
    }
    
    public StubBuilder anyParams() {
        return addMatcher(AnyArgumentsMatcher.INSTANCE);
    }
    
    public ExpectationBuilder isVoid() {
    	return setStub(VoidStub.INSTANCE);
    }
    
    public ExpectationBuilder willReturn(boolean returnValue) {
        return willReturn(new Boolean(returnValue));
    }

    public ExpectationBuilder willReturn(byte returnValue) {
        return willReturn(new Byte(returnValue));
    }

    public ExpectationBuilder willReturn(char returnValue) {
        return willReturn(new Character(returnValue));
    }

    public ExpectationBuilder willReturn(short returnValue) {
        return willReturn(new Short(returnValue));
    }

    public ExpectationBuilder willReturn(int returnValue) {
        return willReturn(new Integer(returnValue));
    }

    public ExpectationBuilder willReturn(long returnValue) {
        return willReturn(new Long(returnValue));
    }

    public ExpectationBuilder willReturn(float returnValue) {
        return willReturn(new Float(returnValue));
    }
    
    public ExpectationBuilder willReturn(double returnValue) {
        return willReturn(new Double(returnValue));
    }
    
    public ExpectationBuilder willReturn(Object returnValue) {
        return setStub(new ReturnStub(returnValue));
    }
    
    public ExpectationBuilder willThrow(Throwable throwable) {
        return setStub(new ThrowStub(throwable));
    }
    
    public ExpectationBuilder addExpectation( InvocationMatcher expectation ) {
        return addMatcher( expectation );
    }
    
	public ExpectationBuilder expectOnce() {
		return addExpectation( new InvokeOnceMatcher() );
	}
	
	public ExpectationBuilder id( String invocationID ) {
		idTable.registerID( invocationID, this );
		return this;
	}
	
    public ExpectationBuilder after( String priorCallID ) {
    	setupOrderingMatchers( idTable, priorCallID, priorCallID );
        return this;
    }
    
    public ExpectationBuilder after( BuilderIdentityTable otherMock, String priorCallID ) {
    	setupOrderingMatchers( otherMock, priorCallID, priorCallID + " on " + otherMock );
    	return this;
    }
    
    private void setupOrderingMatchers( BuilderIdentityTable priorMockObject, 
										String priorCallID, 
										String priorCallDescription ) 
    {
		ExpectationBuilder priorCallBuilder = priorMockObject.lookupID(priorCallID);
    	InvokedRecorder priorCallRecorder = new InvokedRecorder();
    	
    	priorCallBuilder.addExpectation(priorCallRecorder);
    	mocker.addMatcher(new InvokedAfterMatcher( priorCallRecorder,
    	                                           priorCallDescription));
	}
    
	private InvocationMockerBuilder addMatcher(InvocationMatcher matcher) {
        mocker.addMatcher(matcher);
        return this;
    }
    
    private InvocationMockerBuilder setStub(Stub stub) {
        mocker.setStub(stub);
        return this;
    }
}
