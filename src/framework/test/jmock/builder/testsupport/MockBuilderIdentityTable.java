package test.jmock.builder.testsupport;

import junit.framework.AssertionFailedError;

import org.jmock.Verifiable;
import org.jmock.builder.BuilderIdentityTable;
import org.jmock.builder.ExpectationBuilder;
import org.jmock.expectation.ExpectationValue;
import org.jmock.util.Verifier;


public class MockBuilderIdentityTable
	implements BuilderIdentityTable, Verifiable
{
	public ExpectationValue lookupID = new ExpectationValue("lookupID id");
	public ExpectationBuilder lookupIDResult;
	public AssertionFailedError lookupIDError;
	
	public ExpectationBuilder lookupID(String id) {
		lookupID.setActual(id);
		if( lookupIDError != null ) {
			throw lookupIDError;
		} else {
			return lookupIDResult;
		}
	}
	
    public ExpectationValue registerUniqueID = new ExpectationValue("registerUniqueID id");
    public ExpectationValue registerUniqueIDBuilder = 
        new ExpectationValue("registerUniqueID builder");
    
    public void registerUniqueID(String id, ExpectationBuilder builder) {
        registerUniqueID.setActual(id);
        registerUniqueIDBuilder.setActual(builder);
    }
    
	public ExpectationValue registerID = new ExpectationValue("registerID id");
	public ExpectationValue registerIDBuilder = 
		new ExpectationValue("registerID builder");
	
	public void registerID(String id, ExpectationBuilder invocation) {
		registerID.setActual(id);
		registerIDBuilder.setActual(invocation);
	}
	
	public void verify() {
		Verifier.verifyObject(this);
	}
}
