package org.jmock.syntax;

import java.lang.reflect.Method;

import org.hamcrest.Matcher;

public interface MethodClause {
    ParametersClause method(Matcher<Method> methodMatcher);
    ParametersClause method(String nameRegex);
}
