/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock;

import java.util.HashMap;

import junit.framework.AssertionFailedError;

import org.jmock.builder.*;
import org.jmock.core.*;


public class Mock
	implements DynamicMock, BuilderNamespace, Verifiable 
{
    DynamicMock coreMock;
    HashMap idTable = new HashMap();
    
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
    
    public void addInvokable( Invokable invokable ) {
        coreMock.addInvokable(invokable);
    }

    public NameMatchBuilder stub() {
        InvocationMocker mocker = new InvocationMocker( new InvocationMockerDescriber() );
        addInvokable(mocker);
        
        return new InvocationMockerBuilder(mocker,this);
    }
    
    public NameMatchBuilder expect( InvocationMatcher expectation ) {
        InvocationMocker mocker = new InvocationMocker( new InvocationMockerDescriber() );
        
        mocker.addMatcher(expectation);
        addInvokable(mocker);
        
        return new InvocationMockerBuilder(mocker,this);
    }
    
    public void setDefaultStub( Stub newDefaultStub ) {
        coreMock.setDefaultStub(newDefaultStub);
    }
    
    public void reset() {
        coreMock.reset();
    }
    
    public MatchBuilder lookupID(String id) {
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
        storeID( id, builder );
    }
    
	private void storeID( String id, IdentityBuilder builder ) {
        idTable.put( id, builder );
	}
}
