package org.jmock.builder;



public interface BuilderIdentityTable 
{
    MatchBuilder lookupID( String id );
	void registerID( String id, IdentityBuilder invocation );
    void registerUniqueID( String id, IdentityBuilder invocation );
}
