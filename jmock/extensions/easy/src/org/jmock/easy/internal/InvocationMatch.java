/*
 * Created on 11-Jun-2004
 */
package org.jmock.easy.internal;

import java.lang.reflect.Method;

import org.jmock.builder.InvocationMockerDescriber;
import org.jmock.core.Constraint;
import org.jmock.core.DynamicMock;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.InvocationMocker;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.matcher.ArgumentsMatcher;
import org.jmock.core.matcher.InvokeOnceMatcher;
import org.jmock.core.matcher.MethodNameMatcher;
import org.jmock.easy.EasyCoreMock;


public class InvocationMatch {
	private InvocationMatcher methodNameMatcher;
	private InvocationMatcher argsMatcher;
	private InvocationMatcher callCountMatcher;
	
	public void setFromInvocation(EasyCoreMock mock, Method method, Object[] args) {
		callCountMatcher = new InvokeOnceMatcher();
		methodNameMatcher = new MethodNameMatcher(method.getName());
		argsMatcher = new ArgumentsMatcher(InvocationMatch.equalArgs(args));
	}

	public void addInvocationMockerTo(DynamicMock mock) {
		if (isUnset())
			return;
		
		mock.addInvokable(createInvocationMocker());
		flush();
	}
	
	public void flush() {
		methodNameMatcher = null;
		argsMatcher = null;
		callCountMatcher = null;
	}

	public void setCallCount(Range range) {
		callCountMatcher = new InvokeRangeMatcher(range);	
	}

	private boolean isUnset() {
		return methodNameMatcher == null;
	}

	private InvocationMocker createInvocationMocker() {
		InvocationMocker mocker = new InvocationMocker(new InvocationMockerDescriber());
		mocker.addMatcher(callCountMatcher);
		mocker.addMatcher(methodNameMatcher);
		mocker.addMatcher(argsMatcher);
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