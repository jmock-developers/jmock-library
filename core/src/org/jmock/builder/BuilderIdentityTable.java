package org.jmock.builder;



public interface BuilderIdentityTable 
{
    MatchBuilder lookupIDForSameMock( String id );
    
    MatchBuilder lookupIDForOtherMock( String id );
	
    void registerMethodName( String id, MatchBuilder invocation );
    
    void registerUniqueID( String id, MatchBuilder invocation );
}
