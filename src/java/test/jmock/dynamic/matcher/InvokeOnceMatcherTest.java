/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.matcher;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.matcher.InvokeOnceMatcher;

public class InvokeOnceMatcherTest extends TestCase {
    private Invocation emptyInvocation =
            new Invocation(Void.class, "test", "example", new Class[0], Void.class, new Object[0]);
    private InvokeOnceMatcher matcher = new InvokeOnceMatcher();

    public void testWillMatchIfNotYetInvoked() {
        assertTrue("Should match", matcher.matches(emptyInvocation));
    }

    public void testVerifyFailsIfNotYetInvoked() {
        try {
            matcher.verify();
        } catch (AssertionFailedError expected) {
            return;
        }
        fail("Should have thrown exception");
    }

    public void testWillNotMatchAfterInvocation() {
        matcher.invoked(emptyInvocation);
        assertFalse("Should not match", matcher.matches(emptyInvocation));
    }

    public void testVerifyPassesAfterInvocation() {
        matcher.invoked(emptyInvocation);
        matcher.verify();
    }
    
    public void testWritesDescriptionOfMatch() {
    	String description = matcher.writeTo(new StringBuffer()).toString();
    	
    	assertEquals( "should describe match", "called once", description );
    }
}
