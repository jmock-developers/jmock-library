/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic.matcher;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.matcher.CallOnceMatcher;

public class CallOnceMatcherTest extends TestCase {
    private Invocation emptyInvocation =
            new Invocation(Void.class, "example", new Class[0], Void.class, new Object[0]);
    private CallOnceMatcher matcher = new CallOnceMatcher();

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

}
