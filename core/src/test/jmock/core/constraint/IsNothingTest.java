/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.constraint;

import org.jmock.core.constraint.IsNothing;


public class IsNothingTest extends AbstractConstraintsTest
{
    public void testAlwaysEvaluatesToFalse() {
        IsNothing isNothing = new IsNothing();

        assertFalse(isNothing.eval(null));
        assertFalse(isNothing.eval(new Object()));
    }

    public void testHasUsefulDefaultDescription() {
        IsNothing isNothing = new IsNothing();

        assertEquals( "NOTHING", isNothing.describeTo(new StringBuffer()).toString() );
    }

    public void testCanOverrideDescription() {
        String description = "description";
        IsNothing isNothing = new IsNothing(description);

        assertEquals( description, isNothing.describeTo(new StringBuffer()).toString() );
    }
}
