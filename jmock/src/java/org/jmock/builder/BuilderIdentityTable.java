package org.jmock.builder;



public interface BuilderIdentityTable 
{
	ExpectationBuilder lookupID( String id );
	void registerID( String id, ExpectationBuilder invocation );
}
