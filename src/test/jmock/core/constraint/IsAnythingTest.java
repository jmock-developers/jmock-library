/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.constraint;

import org.jmock.core.constraint.IsAnything;


public class IsAnythingTest extends AbstractConstraintsTest
{
    public void testAlwaysEvaluatesToTrue() {
        IsAnything isAnything = new IsAnything();

        assertTrue(isAnything.eval(null));
        assertTrue(isAnything.eval(new Object()));
    }

    public void testHasUsefulDefaultDescription() {
        IsAnything isAnything = new IsAnything();

        assertEquals( "ANYTHING", isAnything.describeTo(new StringBuffer()).toString() );
    }

    public void testCanOverrideDescription() {
        String description = "description";
        IsAnything isAnything = new IsAnything(description);

        assertEquals( description, isAnything.describeTo(new StringBuffer()).toString() );
    }
}
