/*  Copyright (c) 2004 jMock.org
 */
package org.jmock.easy;

import java.lang.reflect.Method;

import org.jmock.builder.InvocationMockerDescriber;
import org.jmock.core.Constraint;
import org.jmock.core.CoreMock;
import org.jmock.core.InvocationMocker;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.matcher.ArgumentsMatcher;
import org.jmock.core.matcher.InvokeOnceMatcher;
import org.jmock.core.matcher.MethodNameMatcher;


public class EasyCoreMock extends CoreMock
{
	private boolean isRecording = true;
	
	public EasyCoreMock(Class mockedType) {
		super(mockedType, CoreMock.mockNameFromClass(mockedType));
	}

	public void replay() {
		isRecording = false;	
	}

	public Object invoke(Object invokedProxy, Method method, Object[] args) throws Throwable
	{
		if (isRecording) {
			addInvokable(createInvocationMocker(method, args));
			return null;
		} 
		return super.invoke(invokedProxy, method, args);
	}

	private InvocationMocker createInvocationMocker(Method method, Object[] args) {
		InvocationMocker mocker = new InvocationMocker(new InvocationMockerDescriber());
		mocker.addMatcher(new InvokeOnceMatcher());
		mocker.addMatcher(new MethodNameMatcher(method.getName()));
		mocker.addMatcher(new ArgumentsMatcher(equalArgs(args)));
		return mocker;
	}

	private Constraint[] equalArgs(Object[] args) {
		Constraint[] result = new Constraint[argumentCount(args)];
		for (int i = 0; i < result.length; i++) {
			result[i] = new IsEqual(args[i]);
		}
		return result;
	}

	private int argumentCount(Object[] args) {
		return args == null ? 0 : args.length;
	}
}
