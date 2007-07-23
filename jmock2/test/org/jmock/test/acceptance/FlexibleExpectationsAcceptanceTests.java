package org.jmock.test.acceptance;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;
import org.jmock.internal.matcher.MethodNameMatcher;

public class FlexibleExpectationsAcceptanceTests extends TestCase {
    Mockery context = new Mockery();
    
    MockedType mock1 = context.mock(MockedType.class, "mock1");
    MockedType mock2 = context.mock(MockedType.class, "mock2");
    
    public void testCanSpecifyFlexibleMethodMatchers() {
        context.checking(new Expectations() {{
            allowing (anything()).method(withName("doSomething.*"));
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
    
    public void testCanSpecifyMethodNameRegexDirectly() {
        context.checking(new Expectations() {{
            allowing (anything()).method("doSomething.*");
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
        context.checking(new Expectations() {{
            allowing (anything()).method(withName("doSomethingWith")).with(equal("x"), equal("y"));
            allowing (anything()).method(withName("doSomethingWith")).with(equal("X"), equal("Y"));
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
        context.checking(new Expectations() {{
            allowing (anything()).method(withName("do.*")).withNoArguments();
            allowing (anything()).method(withName("do.*")).with(equal("X"), equal("Y"));
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
        context.checking(new Expectations() {{
            allowing (anything()).method(withName(".*"));
        }});
        
        mock1.returnInt(); // should not fail
    }
    
    Matcher<Method> withName(String nameRegex) {
        return new MethodNameMatcher(nameRegex);
    }
}
