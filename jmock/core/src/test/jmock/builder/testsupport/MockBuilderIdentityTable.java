package test.jmock.builder.testsupport;

import junit.framework.AssertionFailedError;

import org.jmock.builder.BuilderIdentityTable;
import org.jmock.builder.MatchBuilder;
import org.jmock.core.Verifiable;
import org.jmock.expectation.ExpectationValue;
import org.jmock.util.Verifier;


public class MockBuilderIdentityTable
	implements BuilderIdentityTable, Verifiable
{
	public ExpectationValue lookupIDForSameMock = new ExpectationValue("lookupID id");
	public MatchBuilder lookupIDForSameMockResult;
	public AssertionFailedError lookupIDForSameMockError;
	
	public MatchBuilder lookupIDForSameMock(String id) {
		lookupIDForSameMock.setActual(id);
		if( lookupIDForSameMockError != null ) {
			throw lookupIDForSameMockError;
		} else {
			return lookupIDForSameMockResult;
		}
	}
	
    public ExpectationValue lookupIDForOtherMock = new ExpectationValue("lookupID id");
    public MatchBuilder lookupIDForOtherMockResult;
    public AssertionFailedError lookupIDForOtherMockError;
    
    public MatchBuilder lookupIDForOtherMock(String id) {
        lookupIDForOtherMock.setActual(id);
        if( lookupIDForOtherMockError != null ) {
            throw lookupIDForOtherMockError;
        } else {
            return lookupIDForOtherMockResult;
        }
    }
    
    public ExpectationValue registerUniqueID = 
        new ExpectationValue("registerUniqueID id");
    public ExpectationValue registerUniqueIDBuilder = 
        new ExpectationValue("registerUniqueID builder");
    
    public void registerUniqueID(String id, MatchBuilder builder) {
        registerUniqueID.setActual(id);
        registerUniqueIDBuilder.setActual(builder);
    }
    
	public ExpectationValue registerMethodName = 
        new ExpectationValue("registerMethodName name");
	public ExpectationValue registerMethodNameBuilder = 
		new ExpectationValue("registerMethodName builder");
	
	public void registerMethodName(String name, MatchBuilder builder) {
		registerMethodName.setActual(name);
		registerMethodNameBuilder.setActual(builder);
	}
	
	public void verify() {
		Verifier.verifyObject(this);
	}
}
