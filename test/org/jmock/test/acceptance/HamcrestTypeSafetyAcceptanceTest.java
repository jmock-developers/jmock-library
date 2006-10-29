package org.jmock.test.acceptance;

import static org.hamcrest.text.StringStartsWith.startsWith;
import static org.hamcrest.number.OrderingComparisons.greaterThan;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.jmock.InAnyOrder;
import org.jmock.Mockery;

public class HamcrestTypeSafetyAcceptanceTest extends TestCase {
    public interface MockedType {
        void m(String s);
        void m(int i);
    }
    
    Mockery context = new Mockery();
    MockedType mock = context.mock(MockedType.class, "mock");
    
    public void testMatchersCanCopeWithDifferentArgumentTypes() {
        context.expects(new InAnyOrder() {{
            exactly(1).of (anything()).method(named("m")).with(startsWith("x"));
            exactly(1).of (anything()).method(named("m")).with(greaterThan(0));
        }});
        
        mock.m(1); // should not throw ClassCastException
    }
    
    Matcher<Method> named(String nameRegex) {
        return new MethodNameMatcher(nameRegex);
    }
    
    private class MethodNameMatcher extends TypeSafeMatcher<Method> {
        Pattern namePattern;
        
        public MethodNameMatcher(String nameRegex) {
            namePattern = Pattern.compile(nameRegex);
        }
        
        public boolean matchesSafely(Method method) {
            return namePattern.matcher(method.getName()).matches();
        }

        public void describeTo(Description description) {
            description.appendText("<"+namePattern+">");
        }
    }
}
