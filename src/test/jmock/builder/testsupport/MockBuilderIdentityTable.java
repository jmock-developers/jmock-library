/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.builder.testsupport;

import junit.framework.AssertionFailedError;
import org.jmock.builder.BuilderNamespace;
import org.jmock.builder.MatchBuilder;
import org.jmock.core.Verifiable;
import org.jmock.expectation.ExpectationValue;
import org.jmock.util.Verifier;


public class MockBuilderIdentityTable
        implements BuilderNamespace, Verifiable
{
    public ExpectationValue lookupID = new ExpectationValue("lookupID id");
    public MatchBuilder lookupIDResult;
    public AssertionFailedError lookupIDError;

    public MatchBuilder lookupID( String id ) {
        lookupID.setActual(id);
        if (lookupIDError != null) {
            throw lookupIDError;
        } 
        return lookupIDResult;
    }

    public ExpectationValue registerUniqueID =
            new ExpectationValue("registerUniqueID id");
    public ExpectationValue registerUniqueIDBuilder =
            new ExpectationValue("registerUniqueID builder");

    public void registerUniqueID( String id, MatchBuilder builder ) {
        registerUniqueID.setActual(id);
        registerUniqueIDBuilder.setActual(builder);
    }

    public ExpectationValue registerMethodName =
            new ExpectationValue("registerMethodName name");
    public ExpectationValue registerMethodNameBuilder =
            new ExpectationValue("registerMethodName builder");

    public void registerMethodName( String name, MatchBuilder builder ) {
        registerMethodName.setActual(name);
        registerMethodNameBuilder.setActual(builder);
    }

    public void verify() {
        Verifier.verifyObject(this);
    }
}
