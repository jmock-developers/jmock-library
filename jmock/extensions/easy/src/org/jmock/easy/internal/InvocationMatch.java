/*
 * Created on 11-Jun-2004
 */
package org.jmock.easy.internal;

import org.jmock.builder.InvocationMockerDescriber;
import org.jmock.core.*;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.matcher.ArgumentsMatcher;
import org.jmock.core.matcher.InvokeOnceMatcher;
import org.jmock.core.matcher.MethodNameMatcher;
import org.jmock.core.matcher.AnyArgumentsMatcher;
import org.jmock.core.stub.DefaultResultStub;
import org.jmock.easy.EasyCoreMock;

import java.lang.reflect.Method;


public class InvocationMatch {
	private InvocationMatcher methodNameMatcher;
	private InvocationMatcher argsMatcher;
	private InvocationMatcher callCountMatcher;
	private Stub         stub;
	
	public void setFromInvocation(EasyCoreMock mock, Method method, Object[] args) {
		callCountMatcher = new InvokeOnceMatcher();
		methodNameMatcher = new MethodNameMatcher(method.getName());
		argsMatcher = new ArgumentsMatcher(InvocationMatch.equalArgs(args));
		stub = new DefaultResultStub();
	}

	public void addInvocationMockerTo(DynamicMock mock) {
		if (isUnset())
			return;
		
		mock.addInvokable(createInvocationMocker());
	}
	
	public void flush() {
		methodNameMatcher = null;
		argsMatcher = null;
		callCountMatcher = null;
		stub = null;
	}

	public void expectCallCount(Range range) {
		callCountMatcher = new InvokeRangeMatcher(range);	
	}

    public void stub() {
        callCountMatcher = null;
        argsMatcher = new AnyArgumentsMatcher();
    }

    public void setStub(Stub stub) {
        this.stub = stub;
    }

	private boolean isUnset() {
		return methodNameMatcher == null;
	}

	private InvocationMocker createInvocationMocker() {
		InvocationMocker mocker = new InvocationMocker(new InvocationMockerDescriber());
        if (callCountMatcher != null)
    		mocker.addMatcher(callCountMatcher);
		mocker.addMatcher(methodNameMatcher);
		mocker.addMatcher(argsMatcher);
		mocker.setStub(stub);
		return mocker;
	}

	static private Constraint[] equalArgs(Object[] args) {
		Constraint[] result = new Constraint[argumentCount(args)];
		for (int i = 0; i < result.length; i++) {
			result[i] = new IsEqual(args[i]);
		}
		return result;
	}

	static private int argumentCount(Object[] args) {
		return args == null ? 0 : args.length;
	}

}