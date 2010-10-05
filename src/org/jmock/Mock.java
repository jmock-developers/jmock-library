/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock;

import java.util.HashMap;

import junit.framework.AssertionFailedError;

import org.jmock.builder.BuilderNamespace;
import org.jmock.builder.IdentityBuilder;
import org.jmock.builder.InvocationMockerBuilder;
import org.jmock.builder.InvocationMockerDescriber;
import org.jmock.builder.MatchBuilder;
import org.jmock.builder.NameMatchBuilder;
import org.jmock.core.CoreMock;
import org.jmock.core.DynamicMock;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.InvocationMocker;
import org.jmock.core.Invokable;
import org.jmock.core.Stub;

/**
 * @since 1.0
 */
public class Mock
        implements DynamicMock, BuilderNamespace
{
    DynamicMock coreMock;
    HashMap idTable = new HashMap();

    public Mock( Class mockedType ) {
        this(mockedType, CoreMock.mockNameFromClass(mockedType));
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
        coreMock.setDefaultStub(newDefaultStub);
    }

    public void reset() {
        idTable.clear();
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
