/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import java.util.HashMap;

import junit.framework.AssertionFailedError;

import org.jmock.Verifiable;
import org.jmock.dynamic.CoreMock;
import org.jmock.dynamic.DynamicMock;
import org.jmock.dynamic.InvocationMocker;
import org.jmock.dynamic.LIFOInvocationDispatcher;
import org.jmock.dynamic.matcher.MethodNameMatcher;

public class Mock
	implements BuilderIdentityTable, Verifiable 
{
    DynamicMock coreMock;
    HashMap idTable = new HashMap();
    
    
    public Mock(Class mockedType) {
        this( mockedType, CoreMock.mockNameFromClass(mockedType) );
    }
    
    public Mock( Class mockedType, String name ) {
        this(new CoreMock(mockedType, name, new LIFOInvocationDispatcher()));
    }
    
    public Mock(DynamicMock coreMock) {
        this.coreMock = coreMock;
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

    public MatchBuilder method(String methodName) {
    	InvocationMocker mocker = new InvocationMocker();
    	mocker.addMatcher( new MethodNameMatcher(methodName));
    	coreMock.add(mocker);
    	
    	InvocationMockerBuilder builder = new InvocationMockerBuilder(mocker,this);
        idTable.put( methodName, builder );
    	
		return builder;
    }

    //TODO setDefaultResult(Class, value);
    //TODO setDefaultResult(int value); ...
    
    
	public ExpectationBuilder lookupID(String id) {
		if( ! idTable.containsKey(id) ) {
            throw new AssertionFailedError("no expected invocation named '"+id+"'");
		} 
		return (ExpectationBuilder)idTable.get(id);
	}
	
	public void registerID( String id, ExpectationBuilder builder ) {
		if( idTable.containsKey(id) ) {
			throw new AssertionFailedError("duplicate invocation named '"+id+"'");
        }
        idTable.put( id, builder );
	}
}
