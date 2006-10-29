package org.jmock.test.acceptance;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.jmock.InAnyOrder;
import org.jmock.Mockery;
import org.jmock.core.ExpectationError;

public class FlexibleExpectationsAcceptanceTests extends TestCase {
    Mockery context = new Mockery();
    
    MockedType mock1 = context.mock(MockedType.class, "mock1");
    MockedType mock2 = context.mock(MockedType.class, "mock2");
    
    public void testCanSpecifyFlexibleMethodMatchers() {
        context.expects(new InAnyOrder() {{
            allowing (anything()).method(named("doSomething.*"));
        }});
        
        mock1.doSomething();
        mock1.doSomething();
        mock2.doSomethingWith("x", "y");
        
        try {
            mock1.method1();
            fail("method1 should not have been expected");
        }
        catch (ExpectationError e) {
            // expected
        }
    }

    public void testCanSpecifyFlexibleArgumentMatchers() {
        context.expects(new InAnyOrder() {{
            allowing (anything()).method(named("doSomethingWith")).with(equal("x"), equal("y"));
            allowing (anything()).method(named("doSomethingWith")).with(equal("X"), equal("Y"));
        }});
        
        mock1.doSomethingWith("x", "y");
        mock1.doSomethingWith("X", "Y");
        mock2.doSomethingWith("x", "y");
        mock2.doSomethingWith("X", "Y");
        
        try {
            mock1.doSomething();
            fail("doSomething should not have been expected");
        }
        catch (ExpectationError e) {
            // expected
        }
    }
    
    public void testCanSpecifyNoArguments() {
        context.expects(new InAnyOrder() {{
            allowing (anything()).method(named("do.*")).withNoArguments();
            allowing (anything()).method(named("do.*")).with(equal("X"), equal("Y"));
        }});
        
        mock1.doSomething();
        mock1.doSomethingWith("X", "Y");
        
        try {
            mock1.doSomethingWith("x", "y");
            fail("doSomething should not have been expected");
        }
        catch (ExpectationError e) {
            // expected
        }
    }
    
    public void testCanReturnDefaultValueFromFlexibleExpectation() {
        context.expects(new InAnyOrder() {{
            expects(new InAnyOrder() {{
                allowing (anything()).method(named(".*"));
            }});
        }});
        
        mock1.returnInt(); // should not fail
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
