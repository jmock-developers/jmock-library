package org.jmock.syntax;

import org.hamcrest.Matcher;

public interface ParametersClause extends MethodClause {
    void with(Matcher<?>... parameterMatchers);
    void withNoArguments();
}
