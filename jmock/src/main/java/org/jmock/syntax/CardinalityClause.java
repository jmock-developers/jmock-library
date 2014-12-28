package org.jmock.syntax;

import org.hamcrest.Matcher;


public interface CardinalityClause {
    ReceiverClause exactly(int count);
    ReceiverClause atLeast(int count);
    ReceiverClause between(int minCount, int maxCount);
    ReceiverClause atMost(int count);
    
    <T> T oneOf(T mockObject);
    
    <T> T one(T mockObject);

    <T> T allowing(T mockObject);
    MethodClause allowing(Matcher<?> mockObjectMatcher);
    
    <T> T ignoring(T mockObject);
    MethodClause ignoring(Matcher<?> mockObjectMatcher);
    
    <T> T never(T mockObject);
}
