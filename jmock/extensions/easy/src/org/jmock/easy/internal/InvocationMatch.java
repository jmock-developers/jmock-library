/*
 * Created on 11-Jun-2004
 */
package org.jmock.easy.internal;

import java.lang.reflect.Method;

import junit.framework.AssertionFailedError;

import org.jmock.builder.InvocationMockerDescriber;
import org.jmock.core.Constraint;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.InvocationMocker;
import org.jmock.core.Stub;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.matcher.ArgumentTypesMatcher;
import org.jmock.core.matcher.ArgumentsMatcher;
import org.jmock.core.matcher.InvokeOnceMatcher;
import org.jmock.core.matcher.MethodNameMatcher;
import org.jmock.core.stub.DefaultResultStub;


public class InvocationMatch {
    private Class returnType;
	private InvocationMatcher methodNameMatcher;
	private Object[] methodArguments;
    private Class[] parameterTypes;
	private InvocationMatcher callCountMatcher;
	private Stub         stub;
	
	public void setFromInvocation(Method method, Object[] args) {
        parameterTypes = method.getParameterTypes();
		callCountMatcher = new InvokeOnceMatcher();
		methodNameMatcher = new MethodNameMatcher(method.getName());
		methodArguments = args;
		returnType = method.getReturnType();
        stub = new DefaultResultStub();
	}

	public void addInvocationMockerTo(EasyInvocationDispatcher dispatcher) {
		if (isUnset()) return;
		
        dispatcher.add(createInvocationMocker());
	}
	
	public void flush() {
        parameterTypes = null;
        methodArguments = null;
		methodNameMatcher = null;
		callCountMatcher = null;
		stub = null;
	}

	public void expectCountedCall(Range range, Stub aStub) {
        setCallMatchAndStub(new InvokeRangeMatcher(range), aStub);
	}

    public void setDefault(Stub aStub) {
        setCallMatchAndStub(null, aStub);
    }

    public Object createNumberObjectForReturnValue(long value) {
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
        if (isExpectation())
        		mocker.addMatcher(callCountMatcher);
		mocker.addMatcher(methodNameMatcher);
		mocker.addMatcher(createArgumentMatcher());
		mocker.setStub(stub);
		return mocker;
	}

    private InvocationMatcher createArgumentMatcher() {
        return isExpectation()
            		? (InvocationMatcher)new ArgumentsMatcher(equalArgs(methodArguments))
                    : (InvocationMatcher)new ArgumentTypesMatcher(parameterTypes);
    }
	private boolean isUnset() {
		return methodNameMatcher == null;
	}

    private boolean isExpectation() {
		return callCountMatcher != null;
	}

	static private Constraint[] equalArgs(Object[] args) {
		Constraint[] result = new Constraint[arrayCount(args)];
		for (int i = 0; i < result.length; i++) {
			result[i] = new IsEqual(args[i]);
		}
		return result;
	}

	static private int arrayCount(Object[] args) {
		return args == null ? 0 : args.length;
	}
}