package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.api.ExpectationError;
import org.jmock.test.unit.support.AssertThat;

public class StatesAcceptanceTest extends TestCase {
    Mockery context = new Mockery();
    MockedType mock = context.mock(MockedType.class, "mock");
    
    States readiness = context.states("readiness");
    
    public void testCanConstrainExpectationsToOccurWithinAGivenState() {
        context.checking(new Expectations() {{
            allowing (mock).method1(); when(readiness.is("ready"));
            allowing (mock).doSomething(); then(readiness.is("ready"));
        }});
        
        try {
            mock.method1();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError expected) {}
    }
    
    public void testAllowsExpectationsToOccurInCorrectState() {
        context.checking(new Expectations() {{
            allowing (mock).method1(); when(readiness.is("ready"));
            allowing (mock).doSomething(); then(readiness.is("ready"));
        }});
        
        mock.doSomething();
        mock.method1();
    }
    
    public void testCanStartInASpecificState() {
        context.checking(new Expectations() {{
            allowing (mock).method1(); when(readiness.is("ready"));
        }});

        readiness.startsAs("ready");
        mock.method1();
    }

    public void testErrorMessagesIncludeCurrentStates() {
        readiness.startsAs("ethelred");
        
        States fruitiness = context.states("fruitiness");
        fruitiness.startsAs("apple");
        
        context.checking(new Expectations() {{
            allowing (mock).method1(); when(readiness.is("ready"));
        }});
        
        try {
            mock.method1();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            String message = e.toString();
            
            AssertThat.stringIncludes("should describe readiness state", 
                                      "readiness is ethelred", message);
            AssertThat.stringIncludes("should describe fruitiness state", 
                                      "fruitiness is apple", message);
        }
    }
    
        private static class TestException extends RuntimeException {}
    
    public void testSwitchesStateWhenMethodThrowsAnException() {
        context.checking(new Expectations() {{
            oneOf (mock).method1(); will(throwException(new TestException())); 
                then(readiness.is("ready"));
            oneOf (mock).method2(); when(readiness.is("ready"));
        }});
        
        try {
            mock.method1();
        }
        catch (TestException e) {
            
        }
        mock.method2();
    }

}
