package org.jmock.test.acceptance;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.jmock.InAnyOrder;
import org.jmock.Mockery;

public class ErrorCheckingAcceptanceTests extends TestCase {
    Mockery context = new Mockery();
    final MockedType mock = context.mock(MockedType.class, "mock");
    
    public void testCannotSetAnExpectationOnAnObjectThatIsNotAMock() {
        final ArrayList<String> list = new ArrayList<String>();
        
        try {
            context.expects(new InAnyOrder() {{
                exactly(1).of (list).add("a new element");
            }});
            
            fail("should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException ex) {
            // expected
        }
    }
    
    public void testCannotSetAnExpectationWithoutSpecifyingCardinality() {
        try {
            context.expects(new InAnyOrder() {{
                mock.doSomething();
            }});
            fail("should have thrown IllegalStateException");
        }
        catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testCannotSetAnExpectationWithoutSpecifyingCardinalityAfterPreviousExpectationsWithCardinality() {
        try {
            context.expects(new InAnyOrder() {{
                exactly(1).of (mock).doSomething();
                mock.doSomething();
            }});
            fail("should have thrown IllegalStateException");
        }
        catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testCannotSetAnExpectationWithoutSpecifyingCardinalityAfterAnIncompleteExpectation() {
        try {
            context.expects(new InAnyOrder() {{
                exactly(1);
                mock.doSomething();
            }});
            fail("should have thrown IllegalStateException");
        }
        catch (IllegalStateException ex) {
            // expected
        }
    }
    
    public void testCannotSetAnExpectationWithoutSpecifyingTheMockObject() {
        try {
            context.expects(new InAnyOrder() {{
                exactly(1);
            }});
            fail("should have thrown IllegalStateException");
        }
        catch (IllegalStateException ex) {
            // expected
        }
    }
    
    public void testCannotSetAnExpectationWithoutSpecifyingTheMockObjectBeforeOtherExpectations() {
        try {
            context.expects(new InAnyOrder() {{
                exactly(1);
                exactly(1).of (mock).doSomething();
            }});
            fail("should have thrown IllegalStateException");
        }
        catch (IllegalStateException ex) {
            // expected
        }
    }
    
    public void testCannotSetAnExpectationWithoutSpecifyingTheMockObjectAfterOtherExpectations() {
        try {
            context.expects(new InAnyOrder() {{
                exactly(1).of (mock).doSomething();
                exactly(1);
            }});
            fail("should have thrown IllegalStateException");
        }
        catch (IllegalStateException ex) {
            // expected
        }
    }
    
    public void testCannotSetExpectationWithoutSpecifyingTheMockObjectWhenSettingParameterConstraints() {
        try {
            context.expects(new InAnyOrder() {{
                mock.doSomethingWith(with(equal("1")), with(equal("2")));
            }});
        }
        catch (IllegalStateException e) {
            // exception
        }
    }

    public void testCannotInvokeAMethodOnAMockObjectIfNoExpectationsWereSet() {
        try {
            mock.doSomething();
            fail("should have thrown IllegalStateException");
        }
        catch (IllegalStateException e) {
            // expected
        }
    }
    
    public void testMustSpecifyConstraintsForAllArguments() {
        try {
            context.expects(new InAnyOrder() {{
                exactly(1).of (mock).doSomethingWith("x", with(equal("y")));
            }});
            fail("should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
    }
}
