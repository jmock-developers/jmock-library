/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamock;

import org.jmock.C;
import org.jmock.Constraint;
import org.jmock.dynamic.framework.BuildableInvokable;
import org.jmock.dynamic.framework.CoreMock;
import org.jmock.dynamic.framework.DynamicMock;
import org.jmock.dynamic.framework.InvocationDispatcher;
import org.jmock.dynamic.framework.InvocationMatcher;
import org.jmock.dynamic.framework.Invokable;
import org.jmock.dynamic.framework.LIFOInvocationDispatcher;
import org.jmock.dynamic.framework.Stub;
import org.jmock.dynamic.matcher.ArgumentsMatcher;
import org.jmock.expectation.Verifiable;



public class Mock 
	implements Verifiable 
{
    private DynamicMock coreMock;
    private BuildableInvokableFactory factory;
    
    
    public Mock( Class mockedClass ) {
    	this( mockedClass, CoreMock.mockNameFromClass(mockedClass) );
    }

    public Mock( Class mockedClass, String nonDefaultName ) {
        this( new InvokableFactory(), new LIFOInvocationDispatcher(), 
    		  mockedClass, nonDefaultName );
    }
    
	public Mock( DynamicMock coreMock, BuildableInvokableFactory factory ) {
		this.factory = factory;
		this.coreMock = coreMock;
	}
	
	/** @deprecated */
	public Mock( BuildableInvokableFactory factory, 
				 InvocationDispatcher invocationDispatcher, 
				 Class mockedClass, 
				 String name) 
	{
		this( new CoreMock( mockedClass, name, invocationDispatcher ), factory );
	}
	
	public String toString() {
        return coreMock.toString();
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

	
	/**
	 * Adds an invokable object to the mock object. 
	 * This is the fundamental hook for adding application-specific mock behaviour.
	 * All the sugar methods cause an eventual call to this method.
	 * 
	 * @param invokable The invokable object to add to the mock
	 */
	public void add(Invokable invokable) {
		coreMock.add(invokable);
	}

	public void stub(String methodName, InvocationMatcher argumentsMatcher, Stub stub ) {
		createStub( methodName, argumentsMatcher, stub );
	}
	
	public void stubVoid( String methodName, InvocationMatcher argumentsMatcher ) {
		stub( methodName, argumentsMatcher, factory.createVoidStub() );
	}

	public void stubAndReturn( String methodName, InvocationMatcher argumentsMatcher, 
							   Object result) 
	{
		stub( methodName, argumentsMatcher, factory.createReturnStub(result) );
	}

	public void stubAndThrow( String methodName, InvocationMatcher argumentsMatcher, 
							  Throwable throwable) 
	{
		stub( methodName, argumentsMatcher, factory.createThrowStub(throwable) );
	}
	
	public void stub( String methodName, Stub stub ) {
		stub( methodName, C.NO_ARGS, stub );
	}

	public void stubVoid( String methodName ) {
		stubVoid( methodName, C.NO_ARGS );
	}

	public void stubAndReturn( String methodName, Object result) {
		stubAndReturn( methodName, C.NO_ARGS, result );
	}

	public void stubAndThrow( String methodName, Throwable throwable ) {
		stubAndThrow( methodName, C.NO_ARGS, throwable );
	}
	
	
	
	public void expect( String methodName, InvocationMatcher argumentsMatcher, Stub stub ) {
		BuildableInvokable buildableInvokable = createStub( methodName, argumentsMatcher, stub );
		
		buildableInvokable.addMatcher( factory.createCallOnceMatcher() );
	}
	
	public void expectVoid( String methodName, InvocationMatcher argumentsMatcher ) {
		expect( methodName, argumentsMatcher, factory.createVoidStub() );
	}
	
	public void expectAndReturn( String methodName, InvocationMatcher argumentsMatcher, 
								 Object result ) 
	{
		expect( methodName, argumentsMatcher, factory.createReturnStub(result) );
	}
	
	public void expectAndThrow( String methodName, InvocationMatcher argumentsMatcher, 
								Throwable throwable ) 
	{
		expect( methodName, argumentsMatcher, factory.createThrowStub(throwable) );
	}

	/*------------------------------------------------------------------------------------------
	 *  THE FOLLOWING METHODS ARE BEING REFACTORED BIT BY BIT.
	 */
	
    /*
     * --- Sugar methods ---- 
     */

    public void expect(String methodName) {
        expectVoid(methodName, C.NO_ARGS);
    }

    public void expect(String methodName, Object singleEqualArg) {
        expectVoid(methodName, createInvocationMatcher(singleEqualArg));
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
    
    public void match(String methodName, Object singleEqualArg) {
        stubVoid(methodName, createInvocationMatcher(singleEqualArg));
    }

    public void match(String methodName, int singleEqualArg) {
        match(methodName, new Integer(singleEqualArg));
    }

    public void match(String methodName, boolean singleEqualArg) {
        match(methodName, new Boolean(singleEqualArg));
    }

    public void matchAndReturn(String methodName, boolean result) {
        stubAndReturn(methodName, new Boolean(result));
    }

    public void matchAndReturn(String methodName, int result) {
        stubAndReturn(methodName, new Integer(result));
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

    private InvocationMatcher createInvocationMatcher(Constraint[] constraints) {
    	return new ArgumentsMatcher(constraints);
    }
    
    private InvocationMatcher createInvocationMatcher(Constraint constraint) {
    	return createInvocationMatcher(new Constraint[]{constraint});
    }
    
    private InvocationMatcher createInvocationMatcher(Object argumentValue) {
    	return createInvocationMatcher(C.eq(argumentValue));
    }

    private BuildableInvokable createStub(String methodName, InvocationMatcher argumentsMatcher, Stub stub) {
    	BuildableInvokable buildableInvokable = factory.createBuildableInvokable();
    	
    	buildableInvokable.addMatcher( factory.createMethodNameMatcher(methodName) );
    	buildableInvokable.addMatcher( argumentsMatcher );
    	buildableInvokable.setStub(stub);
    	
    	add(buildableInvokable);
    	
    	return buildableInvokable;
    }
}
