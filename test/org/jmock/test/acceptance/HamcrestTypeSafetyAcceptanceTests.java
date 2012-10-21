package org.jmock.test.acceptance;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.number.OrderingComparison.greaterThan;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.internal.matcher.MethodNameMatcher;

public class HamcrestTypeSafetyAcceptanceTests extends TestCase {
    public interface MockedType {
        void m(String s);
        void m(int i);
    }
    
    Mockery context = new Mockery();
    MockedType mock = context.mock(MockedType.class, "mock");
    
    public void testMatchersCanCopeWithDifferentArgumentTypes() {
        context.checking(new Expectations() {{
            exactly(1).of (anything()).method(withName("m")).with(startsWith("x"));
            exactly(1).of (anything()).method(withName("m")).with(greaterThan(0));
        }});
        
        mock.m(1); // should not throw ClassCastException
    }
    
    Matcher<Method> withName(String nameRegex) {
        return new MethodNameMatcher(nameRegex);
    }
}
