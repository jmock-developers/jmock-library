/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock;

import java.util.HashMap;

import junit.framework.AssertionFailedError;

import org.jmock.builder.*;
import org.jmock.core.*;
import org.jmock.core.matcher.MethodNameMatcher;

public class Mock
	implements BuilderIdentityTable, Verifiable 
{
    DynamicMock coreMock;
    HashMap idTable = new HashMap();
    String pendingMethodName = null;
    IdentityBuilder pendingBuilder = null;
    
    
    public Mock(Class mockedType) {
        this( mockedType, CoreMock.mockNameFromClass(mockedType) );
    }
    
    public Mock( Class mockedType, String name ) {
        this(new CoreMock(mockedType, name));
    }
    
    public Mock( DynamicMock coreMock ) {
        this.coreMock = coreMock;
    }
    
    public Class getMockedType() {
        return coreMock.getMockedType();
    }

    public Object proxy() {
        return coreMock.proxy();
    }

    public String toString() {
        return coreMock.toString();
    }
    
    public void verify() {
        coreMock.verify();
    }
    
    public NameMatchBuilder stub() {
        flushPendingMethodName();
        
        InvocationMocker mocker = new InvocationMocker( new InvocationMockerDescriber() );
        coreMock.add(mocker);
        
        return new InvocationMockerBuilder(mocker,this);
    }
    
    public NameMatchBuilder expect( InvocationMatcher expectation ) {
        flushPendingMethodName();
        
        InvocationMocker mocker = new InvocationMocker( new InvocationMockerDescriber() );
        
        mocker.addMatcher(expectation);
        coreMock.add(mocker);
        
        return new InvocationMockerBuilder(mocker,this);
    }
    
    /**
     * @deprecated use expect(...).method(methodName)... or stub().method(methodName)...
     * Will be removed in version 1.0.
     */
    public ParameterMatchBuilder method(String methodName) {
    	InvocationMocker mocker = new InvocationMocker();
    	mocker.addMatcher( new MethodNameMatcher(methodName));
    	coreMock.add(mocker);
    	
    	InvocationMockerBuilder builder = new InvocationMockerBuilder(mocker,this);
        idTable.put( methodName, builder );
    	
		return builder;
    }
    
    public void setDefaultStub( Stub newDefaultStub ) {
        coreMock.setDefaultStub(newDefaultStub);
    }
    
    public MatchBuilder lookupIDForSameMock(String id) {
        return lookupID(id);
    }
    
    public MatchBuilder lookupIDForOtherMock(String id) {
        flushPendingMethodName();
        return lookupID(id);
	}
	
    private MatchBuilder lookupID(String id) throws AssertionFailedError {
        if( ! idTable.containsKey(id) ) {
            throw new AssertionFailedError("no expected invocation named '"+id+"'");
        }
        
        return (MatchBuilder)idTable.get(id);
    }
    
    public void registerUniqueID( String id, MatchBuilder builder ) {
        if( idTable.containsKey(id) ) {
            throw new AssertionFailedError(
                "duplicate invocation named \"" + id + "\"" );
        }
        
        storeID( id, builder );
    }
    
    public void registerMethodName( String id, MatchBuilder builder ) {
        pendingMethodName = id;
        pendingBuilder = builder;
    }
    
	private void storeID( String id, IdentityBuilder builder ) {
        idTable.put( id, builder );
	}
    
    private void flushPendingMethodName() {
        if( pendingMethodName != null ) {
            storeID( pendingMethodName, pendingBuilder );
            clearPendingMethodName();
        }
    }
    
    private void clearPendingMethodName() {
        pendingMethodName = null;
        pendingBuilder = null;
    }
}
