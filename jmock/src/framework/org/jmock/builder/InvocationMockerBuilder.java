/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.Constraint;
import org.jmock.constraint.IsEqual;
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
        method(new IsEqual(name));
        idTable.registerID( name, this );
        return this;
    }
    
    public StubBuilder match( InvocationMatcher customMatcher ) {
        return addMatcher(customMatcher);
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
        return match(new ArgumentsMatcher(constraints));
	}

	public StubBuilder noParams() {
        return match(NoArgumentsMatcher.INSTANCE);
    }
    
    public StubBuilder anyParams() {
        return match(AnyArgumentsMatcher.INSTANCE);
    }
    
    public ExpectationBuilder stub(Stub stub) {
    	return will(stub);
    }
    
    public ExpectationBuilder will(Stub stubAction) {
        mocker.setStub(stubAction);
        return this;
    }
    
    public ExpectationBuilder isVoid() {
    	return stub(VoidStub.INSTANCE);
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
        return stub(new ReturnStub(returnValue));
    }
    
    public ExpectationBuilder willThrow(Throwable throwable) {
        return stub(new ThrowStub(throwable));
    }
    
    public ExpectationBuilder expect( InvocationMatcher expectation ) {
        return addMatcher( expectation );
    }
    
    public ExpectationBuilder addExpectation( InvocationMatcher expectation ) {
        return expect( expectation );
    }
    
	public ExpectationBuilder expectOnce() {
		return addExpectation( new InvokeOnceMatcher() );
	}
	
	public ExpectationBuilder expectAtLeastOnce() {
		return addExpectation( new InvokeAtLeastOnceMatcher() );
	}
	
    public ExpectationBuilder expectNotCalled() {
        return stub( new TestFailureStub("must not be called") );
    }
    
	public ExpectationBuilder id( String invocationID ) {
        mocker.setName(invocationID);
		idTable.registerUniqueID( invocationID, this );
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
    	
    	priorCallBuilder.expect(priorCallRecorder);
    	mocker.addMatcher(new InvokedAfterMatcher( priorCallRecorder,
    	                                           priorCallDescription));
	}
    
	private InvocationMockerBuilder addMatcher(InvocationMatcher matcher) {
        mocker.addMatcher(matcher);
        return this;
    }
}
