/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.builder.testsupport;

import org.jmock.builder.BuilderNamespace;
import org.jmock.builder.IdentityBuilder;
import org.jmock.builder.MatchBuilder;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.Stub;
import org.jmock.core.Verifiable;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.expectation.ExpectationValue;
import org.jmock.util.Verifier;


public class MockMatchBuilder
        implements MatchBuilder, Verifiable
{
    ExpectationValue addedExpectationType = new ExpectationValue("added expectation type");

    public IdentityBuilder isVoid() {
        throw new NoSuchMethodError("isVoid not implemented");
    }

    public MatchBuilder match( InvocationMatcher customMatcher ) {
        throw new NoSuchMethodError("match not implemented");
    }

    public IdentityBuilder stub( Stub customStub ) {
        throw new NoSuchMethodError("stub not implemented");
    }

    public IdentityBuilder will( Stub stubAction ) {
        throw new NoSuchMethodError("will not implemented");
    }

    public IdentityBuilder willReturn( boolean result ) {
        throw new NoSuchMethodError("willReturn not implemented");
    }

    public IdentityBuilder willReturn( byte result ) {
        throw new NoSuchMethodError("willReturn not implemented");
    }

    public IdentityBuilder willReturn( char result ) {
        throw new NoSuchMethodError("willReturn not implemented");
    }

    public IdentityBuilder willReturn( double result ) {
        throw new NoSuchMethodError("willReturn not implemented");
    }

    public IdentityBuilder willReturn( float result ) {
        throw new NoSuchMethodError("willReturn not implemented");
    }

    public IdentityBuilder willReturn( int result ) {
        throw new NoSuchMethodError("willReturn not implemented");
    }

    public IdentityBuilder willReturn( long result ) {
        throw new NoSuchMethodError("willReturn not implemented");
    }

    public IdentityBuilder willReturn( Object result ) {
        throw new NoSuchMethodError("willReturn not implemented");
    }

    public IdentityBuilder willReturn( short result ) {
        throw new NoSuchMethodError("willReturn not implemented");
    }

    public IdentityBuilder willThrow( Throwable throwable ) {
        throw new NoSuchMethodError("willThrow not implemented");
    }

    public IdentityBuilder expect( InvocationMatcher expectation ) {
        addedExpectationType.setActual(expectation.getClass());
        return this;
    }

    public IdentityBuilder addExpectation( InvocationMatcher expectation ) {
        addedExpectationType.setActual(expectation.getClass());
        return this;
    }


    ExpectationCounter expectOnceCalls = new ExpectationCounter("expectOnce #calls");

    public IdentityBuilder expectOnce() {
        expectOnceCalls.inc();
        return this;
    }

    ExpectationCounter expectAtLeastOnceCalls = new ExpectationCounter("expectAtLeastOnce #calls");

    public IdentityBuilder expectAtLeastOnce() {
        expectAtLeastOnceCalls.inc();
        return this;
    }

    ExpectationValue expectAfterPreviousCall = new ExpectationValue("expectAfter previousCall");

    public IdentityBuilder expectAfter( IdentityBuilder previousCall ) {
        expectAfterPreviousCall.setExpected(previousCall);
        return this;
    }


    public ExpectationValue afterMock = new ExpectationValue("after mock");
    public ExpectationValue afterPreviousCallID = new ExpectationValue("after previousCallID");

    public MatchBuilder after( String previousCallID ) {
        afterPreviousCallID.setActual(previousCallID);
        return this;
    }

    public MatchBuilder after( BuilderNamespace otherMock, String previousCallID ) {
        afterMock.setActual(otherMock);
        return after(previousCallID);
    }

    public ExpectationValue id = new ExpectationValue("id");

    public void id( String newID ) {
        id.setActual(newID);
    }

    public ExpectationCounter expectNotCalledCalls =
            new ExpectationCounter("expectNotCalled #calls");

    public IdentityBuilder expectNotCalled() {
        expectNotCalledCalls.inc();
        return this;
    }

    public void verify() {
        Verifier.verifyObject(this);
    }


}
