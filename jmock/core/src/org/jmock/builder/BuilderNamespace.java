package org.jmock.builder;


public interface BuilderNamespace
{
    MatchBuilder lookupID( String id );

    void registerMethodName( String id, MatchBuilder invocation );

    void registerUniqueID( String id, MatchBuilder invocation );
}
