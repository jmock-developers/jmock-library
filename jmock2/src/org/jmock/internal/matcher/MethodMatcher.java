package org.jmock.internal.matcher;

import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class MethodMatcher extends TypeSafeMatcher<Method> {
    private Method expectedMethod;
    
    public MethodMatcher(Method expectedMethod) {
        super(Method.class);
        this.expectedMethod = expectedMethod;
    }
    
    @Override
    protected boolean matchesSafely(Method m) {
        return expectedMethod.equals(m);
    }
    
    public void describeTo(Description description) {
        description.appendText(expectedMethod.getName());
    }
}
