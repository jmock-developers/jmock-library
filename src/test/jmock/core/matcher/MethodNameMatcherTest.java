/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.matcher;

import junit.framework.TestCase;
import org.jmock.core.Invocation;
import org.jmock.core.matcher.MethodNameMatcher;
import test.jmock.core.testsupport.MethodFactory;
import test.jmock.core.testsupport.MockConstraint;

public class MethodNameMatcherTest extends TestCase {
    private static final String METHOD_NAME = "methodName";
    private static final String OTHER_NAME = "otherName";
    
    private Invocation exampleInvocation;

    public void setUp() {
        MethodFactory methodFactory = new MethodFactory();
        exampleInvocation = new Invocation("INVOKED-OBJECT",
            methodFactory.newMethod(
                METHOD_NAME, 
                new Class[] {String.class, String.class }, 
                Void.class,
                MethodFactory.NO_EXCEPTIONS), 
            new Object[] { "arg1", "arg2" });
    }

    public void testDelegatesMethodNameMatchToConstraint() {
        MockConstraint mockConstraint = new MockConstraint("name constraint",
                METHOD_NAME, true);
        MethodNameMatcher matcher = new MethodNameMatcher(mockConstraint);

        assertTrue("should match", matcher.matches(exampleInvocation));
    }

    public void testDoesNotMatchIfConstraintFails() {
        MockConstraint mockConstraint = new MockConstraint("name constraint",
                METHOD_NAME, false);
        MethodNameMatcher matcher = new MethodNameMatcher(mockConstraint);

        assertFalse("should not match", matcher.matches(exampleInvocation));
    }

    public void testTestsMethodNameForEqualityToString() {
        MethodNameMatcher matcher;

        matcher = new MethodNameMatcher(METHOD_NAME);
        assertTrue("Should match name", matcher.matches(exampleInvocation));

        matcher = new MethodNameMatcher(OTHER_NAME);
        assertFalse("Should not match other name", matcher
                .matches(exampleInvocation));
    }
}