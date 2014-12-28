package org.jmock.syntax;

import org.hamcrest.Matcher;

public interface WithClause {
    boolean booleanIs(Matcher<?> matcher);
    byte byteIs(Matcher<?> matcher);
    char charIs(Matcher<?> matcher);
    short shortIs(Matcher<?> matcher);    
    int intIs(Matcher<?> matcher);
    long longIs(Matcher<?> matcher);
    float floatIs(Matcher<?> matcher);
    double doubleIs(Matcher<?> matcher);
    
    <T> T is(Matcher<?> matcher);
}
