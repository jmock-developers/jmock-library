/*
 * Created on 11-Jun-2004
 */
package org.jmock.easy.internal;


import java.lang.reflect.Method;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.jmock.builder.InvocationMockerDescriber;
import org.jmock.core.*;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.matcher.ArgumentTypesMatcher;
import org.jmock.core.matcher.ArgumentsMatcher;
import org.jmock.core.matcher.InvokeOnceMatcher;
import org.jmock.core.matcher.MethodNameMatcher;
import org.jmock.core.stub.DefaultResultStub;


public class InvocationMatch {
    private Invocation invocation;
	private InvocationMatcher methodNameMatcher;
	private InvocationMatcher callCountMatcher;
	private Stub         stub;
	
	public void setFromInvocation(Invocation invocation) {
        this.invocation = invocation;
        Method method = invocation.invokedMethod;
		callCountMatcher = new InvokeOnceMatcher();
		methodNameMatcher = new MethodNameMatcher(method.getName());
        stub = new DefaultResultStub();
	}

	public void addInvocationMockerTo(EasyInvocationDispatcher dispatcher) {
		if (isUnset()) return;
		
        if (isExpectation())
        	dispatcher.add(createInvocationMocker());
        else
            dispatcher.addDefault(createInvocationMocker());
	}
	
	public void flush() {
        invocation = null;
		methodNameMatcher = null;
		callCountMatcher = null;
		stub = null;
	}

	public void expectCountedCall(Range range, Stub aStub) {
        setCallMatchAndStub(new InvokeRangeMatcher(range), aStub);
	}

    public void setDefaultForMethod(Stub aStub) {
        setCallMatchAndStub(null, aStub);
    }

    public Object createNumberObjectForReturnValue(long value) {
        Class returnType = invocation.invokedMethod.getReturnType();
        if (returnType.equals(Byte.TYPE)) return new Byte((byte) value);
        if (returnType.equals(Short.TYPE)) return new Short((short) value);
        if (returnType.equals(Character.TYPE)) return new Character((char) value);
        if (returnType.equals(Integer.TYPE)) return new Integer((int) value);
        if (returnType.equals(Long.TYPE)) return new Long(value);
        
        throw new AssertionFailedError("incompatible return value type: " + returnType);
    }

    private void setCallMatchAndStub(InvocationMatcher callCountMatcher, Stub stub) {
        this.callCountMatcher = callCountMatcher;
        this.stub = stub;
    }
    
	private InvocationMocker createInvocationMocker() {
		InvocationMocker mocker = new InvocationMocker(new InvocationMockerDescriber());
        mocker.addMatcher(methodNameMatcher);
        if (isExpectation()) mocker.addMatcher(callCountMatcher);
        
		mocker.addMatcher(createArgumentMatcher());
		mocker.setStub(stub);
		return mocker;
	}

    private InvocationMatcher createArgumentMatcher() {
        return isExpectation() ? equalArgumentsMatcher() : argumentTypesMatcher();
    }
	private boolean isUnset() {
		return methodNameMatcher == null;
	}

    private boolean isExpectation() {
		return callCountMatcher != null;
	}

    private InvocationMatcher equalArgumentsMatcher() {
        List args = invocation.parameterValues;
		Constraint[] constraints = new Constraint[args.size()];
		for (int i = 0; i < constraints.length; i++) {
			constraints[i] = new IsEqual(args.get(i));
		}
		return new ArgumentsMatcher(constraints);
	}

    private InvocationMatcher argumentTypesMatcher() {
        return new ArgumentTypesMatcher(invocation.invokedMethod.getParameterTypes());
    }


}