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
    public boolean matchesSafely(Method m) {
        return expectedMethod.equals(m);
    }
    
    @Override
    protected void describeMismatchSafely(Method m, Description mismatchDescription) {
        mismatchDescription.appendText("was ").appendText(m.getName());
    }
    
    public void describeTo(Description description) {
        description.appendText(expectedMethod.getName());
    }

}
