/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamock;

import org.jmock.C;
import org.jmock.Constraint;
import org.jmock.dynamic.framework.CoreMock;
import org.jmock.dynamic.framework.DynamicMock;
import org.jmock.dynamic.framework.InvocationDispatcher;
import org.jmock.dynamic.framework.InvocationMatcher;
import org.jmock.dynamic.framework.LIFOInvocationDispatcher;
import org.jmock.dynamic.matcher.ArgumentsMatcher;
import org.jmock.expectation.Verifiable;

public class Mock implements Verifiable {
    private InvokableFactory invokableFactory;
    private DynamicMock coreMock;

    public Mock(InvokableFactory invokableFactory, InvocationDispatcher invocationDispatcher, Class mockedClass, String name) {
        coreMock = new CoreMock(mockedClass, name, invocationDispatcher);
        this.invokableFactory = invokableFactory;
    }

    public Mock(Class mockedClass, String nonDefaultName) {
        this(new InvokableFactory(), new LIFOInvocationDispatcher(), mockedClass, nonDefaultName);
    }

    public Mock(Class mockedClass) {
        this(mockedClass, CoreMock.mockNameFromClass(mockedClass));
    }

    public String toString() {
        return coreMock.toString();
    }

    public void expect(String methodName, InvocationMatcher args) {
        coreMock.add(invokableFactory.createVoidExpectation(methodName, args));
    }

    public void expectAndReturn(String methodName, InvocationMatcher args, Object result) {
        coreMock.add(invokableFactory.createReturnExpectation(methodName, args, result));
    }

    public void expectAndThrow(String methodName, InvocationMatcher args, Throwable throwable) {
        coreMock.add(invokableFactory.createThrowableExpectation(methodName, args, throwable));
    }

    public void match(String methodName, InvocationMatcher args) {
        coreMock.add(invokableFactory.createVoidStub(methodName, args));
    }

    public void matchAndReturn(String methodName, InvocationMatcher args, Object result) {
        coreMock.add(invokableFactory.createReturnStub(methodName, args, result));
    }

    public void matchAndThrow(String methodName, InvocationMatcher args, Throwable throwable) {
        coreMock.add(invokableFactory.createThrowableStub(methodName, args, throwable));
    }

    /*
     * --- Sugar methods ---- 
     */

    public void expect(String methodName) {
        expect(methodName, C.NO_ARGS);
    }

    public void expect(String methodName, Object singleEqualArg) {
        expect(methodName, createInvocationMatcher(singleEqualArg));
    }

    public void expectAndReturn(String methodName, Object result) {
        expectAndReturn(methodName, C.NO_ARGS, result);
    }

    public void expectAndReturn(String methodName, boolean result) {
        expectAndReturn(methodName, new Boolean(result));
    }

    public void expectAndReturn(String methodName, int result) {
        expectAndReturn(methodName, new Integer(result));
    }

    public void expectAndReturn(String methodName, Object singleEqualArg, Object result) {
        expectAndReturn(methodName, createInvocationMatcher(singleEqualArg), result);
    }

    public void expectAndReturn(String methodName, Object singleEqualArg, boolean result) {
        expectAndReturn(methodName, singleEqualArg, new Boolean(result));
    }

    public void expectAndReturn(String methodName, Object singleEqualArg, int result) {
        expectAndReturn(methodName, singleEqualArg, new Integer(result));
    }

    public void expectAndReturn(String methodName, InvocationMatcher args, boolean result) {
        expectAndReturn(methodName, args, new Boolean(result));
    }

    public void expectAndReturn(String methodName, InvocationMatcher args, int result) {
        expectAndReturn(methodName, args, new Integer(result));
    }

    public void expectAndThrow(String methodName, Throwable exception) {
        expectAndThrow(methodName, C.NO_ARGS, exception);
    }

    public void expectAndThrow(String methodName, Object singleEqualArg, Throwable exception) {
        expectAndThrow(methodName, createInvocationMatcher(singleEqualArg), exception);
    }

    public void match(String methodName) {
        match(methodName, C.NO_ARGS);
    }

    public void match(String methodName, Object singleEqualArg) {
        match(methodName, createInvocationMatcher(singleEqualArg));
    }

    public void match(String methodName, int singleEqualArg) {
        match(methodName, new Integer(singleEqualArg));
    }

    public void match(String methodName, boolean singleEqualArg) {
        match(methodName, new Boolean(singleEqualArg));
    }

    public void matchAndReturn(String methodName, Object result) {
        matchAndReturn(methodName, C.NO_ARGS, result);
    }

    public void matchAndReturn(String methodName, boolean result) {
        matchAndReturn(methodName, new Boolean(result));
    }

    public void matchAndReturn(String methodName, int result) {
        matchAndReturn(methodName, new Integer(result));
    }

    public void matchAndReturn(String methodName, Object singleEqualArg, Object result) {
        matchAndReturn(methodName, createInvocationMatcher(singleEqualArg), result);
    }

    public void matchAndReturn(String methodName, boolean singleEqualArg, Object result) {
        matchAndReturn(methodName, new Boolean(singleEqualArg), result);
    }

    public void matchAndReturn(String methodName, int singleEqualArg, Object result) {
        matchAndReturn(methodName, new Integer(singleEqualArg), result);
    }

    public void matchAndReturn(String methodName, Object singleEqualArg, boolean result) {
        matchAndReturn(methodName, singleEqualArg, new Boolean(result));
    }

    public void matchAndReturn(String methodName, Object singleEqualArg, int result) {
        matchAndReturn(methodName, singleEqualArg, new Integer(result));
    }

    public void matchAndReturn(String methodName, InvocationMatcher args, boolean result) {
        matchAndReturn(methodName, args, new Boolean(result));
    }

    public void matchAndReturn(String methodName, InvocationMatcher args, int result) {
        matchAndReturn(methodName, args, new Integer(result));
    }

    public void matchAndThrow(String methodName, Throwable throwable) {
        matchAndThrow(methodName, C.NO_ARGS, throwable);
    }

    public void matchAndThrow(String methodName, Object singleEqualArg, Throwable throwable) {
        matchAndThrow(methodName, createInvocationMatcher(singleEqualArg), throwable);
    }

    public void matchAndThrow(String methodName, boolean singleEqualArg, Throwable throwable) {
        matchAndThrow(methodName, new Boolean(singleEqualArg), throwable);
    }

    public void matchAndThrow(String methodName, int singleEqualArg, Throwable throwable) {
        matchAndThrow(methodName, new Integer(singleEqualArg), throwable);
    }

    /**
     * @deprecated @see expect
     */
    public void expectVoid(String methodName, InvocationMatcher args) {
        this.expect(methodName, args);
    }

    /**
     * @deprecated @see expect
     */
    public void expectVoid(String methodName, Object equalArg) {
        this.expect(methodName, equalArg);
    }

    /**
     * @deprecated @see expect
     */
    public void expectVoid(String methodName) {
        this.expect(methodName);
    }

    /**
     * @deprecated Not required, as if methodName is called, you will get an exception
     */
    public void expectNotCalled(String methodName) {
    }

    public Object proxy() {
        return coreMock.proxy();
    }

    public void reset() {
        coreMock.reset();
    }

    public void verify() {
        coreMock.verify();
    }

    private InvocationMatcher createInvocationMatcher(Constraint[] constraints) {
    	return new ArgumentsMatcher(constraints);
    }
    
    private InvocationMatcher createInvocationMatcher(Constraint constraint) {
    	return createInvocationMatcher(new Constraint[]{constraint});
    }
    
    private InvocationMatcher createInvocationMatcher(Object argumentValue) {
    	return createInvocationMatcher(C.eq(argumentValue));
    }
}
