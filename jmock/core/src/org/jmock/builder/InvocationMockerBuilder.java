/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.builder;

import java.lang.reflect.Method;
import junit.framework.AssertionFailedError;
import org.jmock.core.Constraint;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.Stub;
import org.jmock.core.StubMatchersCollection;
import org.jmock.core.matcher.*;
import org.jmock.core.stub.VoidStub;

/**
 * @since 1.0
 */
public class InvocationMockerBuilder
        implements NameMatchBuilder
{
    private StubMatchersCollection mocker;
    private BuilderNamespace builderNamespace;
    private Class mockedType;

    public InvocationMockerBuilder( StubMatchersCollection mocker,
                                    BuilderNamespace idTable,
                                    Class mockedType )
    {
        this.mocker = mocker;
        this.builderNamespace = idTable;
        this.mockedType = mockedType;
    }

    public ArgumentsMatchBuilder method( Constraint nameConstraint ) {
        return addMatcher(new MethodNameMatcher(nameConstraint));
    }

    public ArgumentsMatchBuilder method( String name ) {
        checkLegalMethodName(name);
        checkExistingMethodName(name);

        addMatcher(new MethodNameMatcher(name));
        builderNamespace.registerMethodName(name, this);

        return this;
    }

    private void checkLegalMethodName( String name ) {
        if( !isLegalMethodName(name) ) {
            throw new IllegalArgumentException("illegal method name: " + name + " is not a legal Java identifier");
        }
    }

    private boolean isLegalMethodName( String name ) {
        if( !Character.isJavaIdentifierStart(name.charAt(0)) ) return false;
        for( int i = 1; i < name.length(); i++ ) {
            if( !Character.isJavaIdentifierPart(name.charAt(i))) return false;
        }
        return true;
    }

    private void checkExistingMethodName( String name ) {
        if( !typeDefinesMethodNamed(mockedType,name) && !typeDefinesMethodNamed(Object.class,name) ) {
            throw new AssertionFailedError("no method named " + name + " is defined in type "+mockedType);
        }
    }

    private boolean typeDefinesMethodNamed( Class type, String name ) {
        Method[] methods = type.getMethods();
        
        for( int i = 0; i < methods.length; i++ ) {
            if( methods[i].getName().equals(name) ) return true;
        }
        return false;
    }

    public MatchBuilder match( InvocationMatcher customMatcher ) {
        return addMatcher(customMatcher);
    }

    public MatchBuilder with( Constraint arg1 ) {
        return with(new Constraint[]{arg1});
    }

    public MatchBuilder with( Constraint arg1, Constraint arg2 ) {
        return with(new Constraint[]{arg1, arg2});
    }

    public MatchBuilder with( Constraint arg1, Constraint arg2, Constraint arg3 ) {
        return with(new Constraint[]{arg1, arg2, arg3});
    }

    public MatchBuilder with( Constraint arg1, Constraint arg2, Constraint arg3, Constraint arg4 ) {
        return with(new Constraint[]{arg1, arg2, arg3, arg4});
    }

    public MatchBuilder with( Constraint[] constraints ) {
        return addMatcher(new ArgumentsMatcher(constraints));
    }

    public MatchBuilder withNoArguments() {
        return addMatcher(NoArgumentsMatcher.INSTANCE);
    }

    public MatchBuilder withAnyArguments() {
        return addMatcher(AnyArgumentsMatcher.INSTANCE);
    }

    public IdentityBuilder will( Stub stubAction ) {
        setStub(stubAction);
        return this;
    }

    public IdentityBuilder isVoid() {
        setStub(VoidStub.INSTANCE);
        return this;
    }

    private void setStub( Stub stubAction ) {
        mocker.setStub(stubAction);
    }

    public IdentityBuilder expect( InvocationMatcher expectation ) {
        return addMatcher(expectation);
    }

    public void id( String invocationID ) {
        mocker.setName(invocationID);
        builderNamespace.registerUniqueID(invocationID, this);
    }

    public MatchBuilder after( String priorCallID ) {
        setupOrderingMatchers(builderNamespace, priorCallID, priorCallID);
        return this;
    }

    public MatchBuilder after( BuilderNamespace otherMock, String priorCallID ) {
        setupOrderingMatchers(otherMock, priorCallID, priorCallID + " on " + otherMock);
        return this;
    }

    private void setupOrderingMatchers( BuilderNamespace idTable,
                                        String priorCallID,
                                        String priorCallDescription ) {
        MatchBuilder priorCallBuilder = idTable.lookupID(priorCallID);

        if (priorCallBuilder == this) {
            throw new AssertionFailedError("confusing identifier of prior invocation \"" + priorCallID + "\"; " +
                                           "give it an explicit call identifier");
        }

        InvokedRecorder priorCallRecorder = new InvokedRecorder();

        priorCallBuilder.match(priorCallRecorder);

        mocker.addMatcher(new InvokedAfterMatcher(priorCallRecorder,
                                                  priorCallDescription));
    }

    private InvocationMockerBuilder addMatcher( InvocationMatcher matcher ) {
        mocker.addMatcher(matcher);
        return this;
    }
}
