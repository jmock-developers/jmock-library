package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;

public class ParameterMatchingAcceptanceTests extends TestCase {
    public interface AnInterface {
        void doSomethingWith(String s);
        void doSomethingWithBoth(String s1, String s2);
        
        void doSomethingWithBoth(boolean i1, boolean i2);
        void doSomethingWithBoth(byte i1, byte i2);
        void doSomethingWithBoth(short i1, short i2);
        void doSomethingWithBoth(char c1, char c2);
        void doSomethingWithBoth(int i1, int i2);
        void doSomethingWithBoth(long i1, long i2);
        void doSomethingWithBoth(float i1, float i2);
        void doSomethingWithBoth(double i1, double i2);
    }
    
    Mockery context = new Mockery();
    AnInterface mock = context.mock(AnInterface.class, "mock");
    
    public void testMatchesParameterValues() {
        context.checking(new Expectations() {{
            exactly(1).of (mock).doSomethingWith(with(equal("hello")));
            exactly(1).of (mock).doSomethingWith(with(equal("goodbye")));
        }});
        
        mock.doSomethingWith("hello");
        mock.doSomethingWith("goodbye");
        
        context.assertIsSatisfied();
    }
    
    public void testDoesNotAllowUnexpectedParameterValues() {
        context.checking(new Expectations() {{
            exactly(1).of (mock).doSomethingWith(with(equal("hello")));
            exactly(1).of (mock).doSomethingWith(with(equal("goodbye")));
        }});
        
        try {
            mock.doSomethingWith("hello");
            mock.doSomethingWith("Goodbye");
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError expected) {}
    }
    
    public void testAllOrNoneOfTheParametersMustBeSpecifiedByMatchers() {
        try {
            context.checking(new Expectations() {{
                exactly(1).of (mock).doSomethingWithBoth(with(equal("a-matcher")), "not-a-matcher");
            }});
        }
        catch (IllegalArgumentException expected) {
        }
    }
    
    // Test to show that issue JMOCK-160 is spurious
    public void testNotAllExpectationsOfSameMockMustUseMatchers() {
        context.checking(new Expectations() {{
            exactly(1).of (mock).doSomethingWithBoth(with(equal("x")), with(equal("y")));
            exactly(1).of (mock).doSomethingWith("z");
        }});
        
        mock.doSomethingWithBoth("x", "y");
        mock.doSomethingWith("z");
        
        context.assertIsSatisfied();
    }
    
    // See issue JMOCK-161
    public void testCanPassLiteralValuesToWithMethodToMeanEqualTo() {
        context.checking(new Expectations() {{
            exactly(2).of (mock).doSomethingWithBoth(with(any(String.class)), with("y"));
        }});
        
        mock.doSomethingWithBoth("x", "y");
        mock.doSomethingWithBoth("z", "y");
        
        context.assertIsSatisfied();
    }
    
    public void testCanPassLiteralPrimitiveValuesToWithMethodToMeanEqualTo() {
        context.checking(new Expectations() {{
            exactly(2).of (mock).doSomethingWithBoth(with.booleanIs(any(boolean.class)), with(true));
            exactly(2).of (mock).doSomethingWithBoth(with.byteIs(any(byte.class)), with((byte)1));
            exactly(2).of (mock).doSomethingWithBoth(with.shortIs(any(short.class)), with((short)2));
            exactly(2).of (mock).doSomethingWithBoth(with.charIs(any(char.class)), with('x'));
            exactly(2).of (mock).doSomethingWithBoth(with.intIs(any(int.class)), with(3));
            exactly(2).of (mock).doSomethingWithBoth(with.longIs(any(long.class)), with(4L));
            exactly(2).of (mock).doSomethingWithBoth(with.floatIs(any(float.class)), with(5.0f));
            exactly(2).of (mock).doSomethingWithBoth(with.doubleIs(any(double.class)), with(6.0));
        }});
        
        mock.doSomethingWithBoth(true, true);
        mock.doSomethingWithBoth(false, true);
        
        mock.doSomethingWithBoth((byte)1, (byte)1);
        mock.doSomethingWithBoth((byte)2, (byte)1);
        
        mock.doSomethingWithBoth((short)1, (short)2);
        mock.doSomethingWithBoth((short)2, (short)2);
        
        mock.doSomethingWithBoth('1', 'x');
        mock.doSomethingWithBoth('2', 'x');
        
        mock.doSomethingWithBoth(1, 3);
        mock.doSomethingWithBoth(2, 3);
        
        mock.doSomethingWithBoth(1L, 4L);
        mock.doSomethingWithBoth(2L, 4L);
        
        mock.doSomethingWithBoth(1.0f, 5.0f);
        mock.doSomethingWithBoth(2.0f, 5.0f);
        
        mock.doSomethingWithBoth(1.0, 6.0);
        mock.doSomethingWithBoth(2.0, 6.0);
        
        context.assertIsSatisfied();
    }
    
    // Checking that you can do with(any(...)) with primitive types, as asked on the mailing list
    public void testSpecifyingAnyValueOfPrimitiveType() {
        context.checking(new Expectations() {{
            allowing (mock).doSomethingWithBoth(with.booleanIs(any(boolean.class)), with.booleanIs(any(boolean.class)));
        }});
        
        mock.doSomethingWithBoth(true, true);
        mock.doSomethingWithBoth(true, false);
        mock.doSomethingWithBoth(false, true);
        mock.doSomethingWithBoth(false, false);
    }
}
