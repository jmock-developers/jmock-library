/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.Constraint;
import org.jmock.dynamic.StubMatchersCollection;
import org.jmock.dynamic.matcher.ArgumentsMatcher;
import org.jmock.dynamic.matcher.InvokeOnceMatcher;
import org.jmock.dynamic.stub.ReturnStub;
import org.jmock.dynamic.stub.ThrowStub;
import org.jmock.dynamic.stub.VoidStub;
import org.jmock.dynamock.C;

public class InvocationMockerBuilder 
    implements MatchBuilder, StubBuilder, ExpectationBuilder 
{
    private StubMatchersCollection mocker;

    public InvocationMockerBuilder(StubMatchersCollection mocker) {
        this.mocker = mocker;
    }

	public StubBuilder passed(Object arg1) {
		return passed(new Constraint[]{C.eq(arg1)});
	}

	public StubBuilder passed(Object arg1, Object arg2) {
		return passed(new Constraint[] {C.eq(arg1), C.eq(arg2)} );
	}
	
	public StubBuilder passed(Constraint[] constraints) {
		mocker.addMatcher(new ArgumentsMatcher(constraints));
		return this;
	}

    public StubBuilder noParams() {
    	mocker.addMatcher(C.NO_ARGS);
    	return this;
    }
    
    public ExpectationBuilder isVoid() {
    	mocker.setStub(new VoidStub());
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
