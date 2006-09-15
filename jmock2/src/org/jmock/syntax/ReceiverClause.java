package org.jmock.syntax;

public interface ReceiverClause {
    <T> T of(T mockObject);
    
    //TODO: MethodClause of(Matcher<?> receiverMatcher);
}
