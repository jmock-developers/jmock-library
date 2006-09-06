package org.jmock.internal;

import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class MethodMatcher implements Matcher<Method> {
    private Method expectedMethod;
    
    public MethodMatcher(Method expectedMethod) {
        this.expectedMethod = expectedMethod;
    }
    
    public boolean match(Method m) {
        return m == expectedMethod;
    }
    
    public void describeTo(Description description) {
        description.appendText(expectedMethod.getName());
    }
}
