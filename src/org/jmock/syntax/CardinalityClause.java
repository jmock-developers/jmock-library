package org.jmock.syntax;

import org.hamcrest.Matcher;
import org.jmock.internal.ExpectationGroupBuilder;


public interface CardinalityClause {
    ReceiverClause exactly(int count);
    ReceiverClause atLeast(int count);
    ReceiverClause between(int minCount, int maxCount);
    ReceiverClause atMost(int count);
    
    <T> T one(T mockObject);
    
    <T> T allowing(T mockObject);
    MethodClause allowing(Matcher<Object> mockObjectMatcher);
    
    <T> void ignoring(T mockObject);
    
    <T> T never(T mockObject);
    
    void expects(ExpectationGroupBuilder subgroupBuilder);
}
