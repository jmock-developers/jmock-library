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


}
