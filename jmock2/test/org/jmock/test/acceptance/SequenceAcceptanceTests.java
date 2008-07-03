package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.hamcrest.StringDescription;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.api.ExpectationError;
import org.jmock.test.unit.support.AssertThat;

public class SequenceAcceptanceTests extends TestCase {
    Mockery context = new Mockery();
    MockedType mock = context.mock(MockedType.class, "mock");
    
    public void testCanConstrainInvocationsToOccurInOrder() {
        final Sequence s = context.sequence("s");
        
        context.checking(new Expectations() {{
            oneOf (mock).method1(); inSequence(s);
            oneOf (mock).method2(); inSequence(s);
        }});
        
        try {
            mock.method2();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            // expected
        }
    }
    
    public void testAllowsInvocationsInSequence() {
        final Sequence s = context.sequence("s");
        
        context.checking(new Expectations() {{
            oneOf (mock).method1(); inSequence(s);
            oneOf (mock).method2(); inSequence(s);
        }});
        
        mock.method1();
        mock.method2();
    }
    
    public void testCanSkipAllowedInvocationsInSequence() {
        final Sequence s = context.sequence("s");
        
        context.checking(new Expectations() {{
            oneOf (mock).method1(); inSequence(s);
            allowing (mock).method2(); inSequence(s);
            oneOf (mock).method3(); inSequence(s);
        }});
        
        mock.method1();
        mock.method3();
    }
    
    public void testSequencesAreIndependentOfOneAnother() {
        final Sequence s = context.sequence("s");
        final Sequence t = context.sequence("t");
        
        context.checking(new Expectations() {{
            oneOf (mock).method1(); inSequence(s);
            oneOf (mock).method2(); inSequence(s);
            
            oneOf (mock).method3(); inSequence(t);
            oneOf (mock).method4(); inSequence(t);
        }});
        
        mock.method1();
        mock.method3();
        mock.method2();
        mock.method4();
    }
    
    public void testExpectationIncludesSequenceInDescription() {
        final Sequence s = context.sequence("s");
        
        context.checking(new Expectations() {{
            oneOf (mock).method1(); inSequence(s);
        }});
        
        try {
            mock.method2();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            AssertThat.stringIncludes("error message", "in sequence s", StringDescription.toString(e));
        }
    }
    
    public void testAnExpectationCanBeInMoreThanOneSequence() {
        final Sequence s = context.sequence("s");
        final Sequence t = context.sequence("t");
        
        context.checking(new Expectations() {{
            oneOf (mock).method1(); inSequence(s);
            oneOf (mock).method2(); inSequence(t);
            oneOf (mock).method3(); inSequence(s); inSequence(t);
        }});
        
        mock.method1();
    
        try {
            mock.method3();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            AssertThat.stringIncludes("error message", "in sequence s", StringDescription.toString(e));
            AssertThat.stringIncludes("error message", "in sequence t", StringDescription.toString(e));
        }
    }

    // See issue JMOCK-142
    public void testHasShortcutForIncludingExpectationInMultipleSequences() {
        final Sequence s = context.sequence("s");
        final Sequence t = context.sequence("t");
        
        context.checking(new Expectations() {{
            oneOf (mock).method1(); inSequence(s);
            oneOf (mock).method2(); inSequence(t);
            oneOf (mock).method3(); inSequences(s, t);
        }});
        
        mock.method1();
    
        try {
            mock.method3();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            AssertThat.stringIncludes("error message", "in sequence s", StringDescription.toString(e));
            AssertThat.stringIncludes("error message", "in sequence t", StringDescription.toString(e));
        }
    }
}
