/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.Constraint;
import org.jmock.dynamic.StubMatchersCollection;
import org.jmock.dynamic.matcher.ArgumentsMatcher;
import org.jmock.dynamic.matcher.InvokeOnceMatcher;
import org.jmock.dynamic.matcher.NoArgumentsMatcher;
import org.jmock.dynamic.stub.ReturnStub;
import org.jmock.dynamic.stub.ThrowStub;
import org.jmock.dynamic.stub.VoidStub;

public class InvocationMockerBuilder 
    implements MatchBuilder, StubBuilder, ExpectationBuilder 
{
    private StubMatchersCollection mocker;

    public InvocationMockerBuilder(StubMatchersCollection mocker) {
        this.mocker = mocker;
    }

	public StubBuilder args(Constraint arg1) {
		return args(new Constraint[]{arg1});
	}

	public StubBuilder args(Constraint arg1, Constraint arg2) {
		return args(new Constraint[] {arg1, arg2} );
	}
	
    public StubBuilder args(Constraint arg1, Constraint arg2, Constraint arg3) {
        return args(new Constraint[] {arg1, arg2, arg3} );
    }
    
    public StubBuilder args(Constraint arg1, Constraint arg2, Constraint arg3, Constraint arg4) {
        return args(new Constraint[] {arg1, arg2, arg3, arg4} );
    }
    
    public StubBuilder args(Constraint[] constraints) {
		mocker.addMatcher(new ArgumentsMatcher(constraints));
		return this;
	}

    public StubBuilder noParams() {
    	mocker.addMatcher(NoArgumentsMatcher.INSTANCE);
    	return this;
    }
    
    public ExpectationBuilder isVoid() {
    	mocker.setStub(VoidStub.INSTANCE);
    	return this;
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
        mocker.setStub(new ReturnStub(returnValue));
        return this;
    }

    public ExpectationBuilder willThrow(Throwable throwable) {
        mocker.setStub(new ThrowStub(throwable));
        return this;
    }
    
	public ExpectationBuilder expectOnce() {
		mocker.addMatcher( new InvokeOnceMatcher() );
		return this;
	}

}
