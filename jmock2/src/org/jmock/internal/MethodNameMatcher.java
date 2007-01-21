/**
 * 
 */
package org.jmock.internal;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class MethodNameMatcher extends TypeSafeMatcher<Method> {
    Pattern namePattern;
    
    public MethodNameMatcher(String nameRegex) {
        namePattern = Pattern.compile(nameRegex);
    }
    
    @Override
    public boolean matchesSafely(Method method) {
        return namePattern.matcher(method.getName()).matches();
    }
    
    public void describeTo(Description description) {
        description.appendText(namePattern.toString());
    }
}