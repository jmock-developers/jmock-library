/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.C;
import org.jmock.Constraint;
import org.jmock.dynamic.StubMatchersCollection;
import org.jmock.dynamic.matcher.ArgumentsMatcher;
import org.jmock.dynamic.matcher.CallOnceMatcher;
import org.jmock.dynamic.stub.ReturnStub;
import org.jmock.dynamic.stub.ThrowStub;
import org.jmock.dynamic.stub.VoidStub;

public class InvocationMockerBuilder implements MatchBuilder, ExpectationBuilder {

    private StubMatchersCollection mocker;

    public InvocationMockerBuilder(StubMatchersCollection mocker) {
        this.mocker = mocker;
    }

	public StubBuilder whenPassed(Object arg1) {
		return whenPassed(new Constraint[]{C.eq(arg1)});
	}

	public StubBuilder whenPassed(Object arg1, Object arg2) {
		return whenPassed(new Constraint[] {C.eq(arg1), C.eq(arg2)} );
	}
	
	public StubBuilder whenPassed(Constraint[] constraints) {
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

    public ExpectationBuilder willReturn(Object returnValue) {
        mocker.setStub(new ReturnStub(returnValue));
        return this;
    }

    public ExpectationBuilder willThrow(Throwable throwable) {
        mocker.setStub(new ThrowStub(throwable));
        return this;
    }
    
	public ExpectationBuilder expectOnce() {
		mocker.addMatcher( new CallOnceMatcher() );
		return this;
	}

}
