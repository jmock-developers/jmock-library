/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamock;

import org.jmock.Verifiable;
import org.jmock.dynamic.*;


public class Mock 
	implements Verifiable 
{
    private DynamicMock coreMock;
    private BuildableInvokableFactory factory;
    
    
    public Mock( Class mockedClass ) {
    	this( mockedClass, CoreMock.mockNameFromClass(mockedClass) );
    }
    
    public Mock( Class mockedClass, String nonDefaultName ) {
        this( new CoreMock( mockedClass, nonDefaultName ), 
    		  new InvokableFactory() );
    }
    
	public Mock( DynamicMock coreMock, BuildableInvokableFactory factory ) {
		this.factory = factory;
		this.coreMock = coreMock;
	}
	
	public String toString() {
        return coreMock.toString();
    }
    
	public Object proxy() {
		return coreMock.proxy();
	}
	
    public Class getMockedType() {
        return coreMock.getMockedType();
    }
    
	public void reset() {
		coreMock.reset();
	}
	
	public void verify() {
		coreMock.verify();
	}
	
    public void setDefaultStub( Stub newDefaultStub ) {
        coreMock.setDefaultStub(newDefaultStub);
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
    
    public void expectNotCalled( String methodName, InvocationMatcher argumentsMatcher ) {
        createStub( methodName, argumentsMatcher, 
                    factory.createTestFailureStub("must not be called") );
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
