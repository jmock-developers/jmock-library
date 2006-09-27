package org.jmock.syntax;

import org.hamcrest.Matcher;


public interface CardinalitySyntax {
    ReceiverClause exactly(int count);
    ReceiverClause atLeast(int count);
    ReceiverClause between(int minCount, int maxCount);
    ReceiverClause atMost(int count);
    
    <T> T allowing(T mockObject);
    MethodClause allowing(Matcher<Object> mockObjectMatcher);
    
    <T> void ignoring(T mockObject);
    
    <T> T never(T mockObject);
}
