/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.matcher;

import junit.framework.TestCase;

import org.jmock.core.Invocation;
import org.jmock.core.matcher.InvokeCountMatcher;
import org.jmock.expectation.AssertMo;

import test.jmock.core.testsupport.MethodFactory;


public class InvokeCountMatcherTest extends TestCase
{
    private static final int COUNT = 3;

    private Invocation emptyInvocation;
    private InvokeCountMatcher matcher;


    public void setUp() {
        MethodFactory methodFactory = new MethodFactory();
        emptyInvocation = new Invocation("INVOKED-OBJECT",
                                         methodFactory.newMethod("example", MethodFactory.NO_ARGUMENTS, Void.class, MethodFactory.NO_EXCEPTIONS),
                                         new Object[0]);
        matcher = new InvokeCountMatcher(COUNT);
    }

    public void testMatchesOnlyIfNotYetInvokedTheExpectedNumberOfTimes() {
        for( int i = 0; i < COUNT; i++ ) {
            assertTrue("Should match", matcher.matches(emptyInvocation));
            matcher.invoked(emptyInvocation);
        }

        assertFalse("Should not match", matcher.matches(emptyInvocation));
    }


    public void testVerifyFailsIfNotInvokedTheExpectedNumberOfTimes() {
        for( int i = 0; i < COUNT; i++ ) {
            AssertMo.assertFails("verify should fail", new Runnable() {
                public void run() {
                    matcher.verify();
                }
            });

            matcher.invoked(emptyInvocation);
        }

        matcher.verify();
    }


    public void testReportsNumberOfExpectedAndActualInvocationsInDescription() {
        for( int i = 0; i < COUNT-1; i++ ) {
            matcher.invoked(emptyInvocation);
        }

        String description = matcher.describeTo(new StringBuffer()).toString();

        assertTrue("should have description", matcher.hasDescription());
        assertTrue("should describe match",
                   description.indexOf("expected "+COUNT+" times") >= 0);
        assertTrue("should report number of actual invocations",
                   description.indexOf("invoked "+(COUNT-1)+" times") >= 0);
    }
}
