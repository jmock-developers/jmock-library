/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock;

import java.util.HashMap;
import junit.framework.AssertionFailedError;
import org.jmock.builder.*;
import org.jmock.core.*;


public class Mock
        implements DynamicMock, BuilderNamespace, Verifiable
{
    String name;
    InvocationDispatcher dispatcher;
    DynamicMock coreMock;
    HashMap idTable = new HashMap();

    public Mock( Class mockedType ) {
        this(mockedType, CoreMock.mockNameFromClass(mockedType));
    }

    public Mock( Class mockedType, String name ) {
        this( mockedType, name, 
              new OrderedInvocationDispatcher(new OrderedInvocationDispatcher.LIFOInvokablesCollection()) );
    }
    
    public Mock( Class mockedType, String name, InvocationDispatcher dispatcher ) {
    	this( new CoreMock(mockedType, name, dispatcher), name, dispatcher );
    }

    public Mock( DynamicMock coreMock, String name, InvocationDispatcher dispatcher ) {
        this.coreMock = coreMock;
        this.name = name;
        this.dispatcher = dispatcher;
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
        try {
            dispatcher.verify();
        }
        catch (AssertionFailedError ex) {
            throw new AssertionFailedError( "mock object " + name + ": " + ex.getMessage());
        }
    }

    public void addInvokable( Invokable invokable ) {
        dispatcher.add(invokable);
    }

    public NameMatchBuilder stubs() {
        return addNewInvocationMocker();
    }

    public NameMatchBuilder expects( InvocationMatcher expectation ) {
        NameMatchBuilder builder = addNewInvocationMocker();
        builder.match(expectation);
        return builder;
    }

    private NameMatchBuilder addNewInvocationMocker() {
        InvocationMocker mocker = new InvocationMocker(new InvocationMockerDescriber());
        addInvokable(mocker);

        return new InvocationMockerBuilder( mocker, this, getMockedType() );
    }

    public void setDefaultStub( Stub newDefaultStub ) {
        dispatcher.setDefaultStub(newDefaultStub);
    }

    public void reset() {
        coreMock.reset();
    }

    public MatchBuilder lookupID( String id ) {
        if (!idTable.containsKey(id)) {
            throw new AssertionFailedError("no expected invocation named '" + id + "'");
        }

        return (MatchBuilder)idTable.get(id);
    }

    public void registerUniqueID( String id, MatchBuilder builder ) {
        if (idTable.containsKey(id)) {
            throw new AssertionFailedError("duplicate invocation named \"" + id + "\"");
        }

        storeID(id, builder);
    }

    public void registerMethodName( String id, MatchBuilder builder ) {
        storeID(id, builder);
    }

    private void storeID( String id, IdentityBuilder builder ) {
        idTable.put(id, builder);
    }
}
