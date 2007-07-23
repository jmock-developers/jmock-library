package org.jmock.syntax;

import org.hamcrest.Matcher;

public interface ReceiverClause {
    <T> T of(T mockObject);
    
    MethodClause of(Matcher<?> objectMatcher);
}
