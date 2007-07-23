/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.test.unit.lib.action;

import junit.framework.TestCase;

import org.hamcrest.StringDescription;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;


public class CustomActionTests extends TestCase {
    static class ConcreteSideEffect extends CustomAction {
        public ConcreteSideEffect( String description ) {
            super(description);
        }

        public Object invoke( Invocation invocation ) throws Throwable {
            return null;
        }
    }

    public void testAppendsGivenDescription() {
        String description = "DESCRIPTION";
        CustomAction sideEffect = new ConcreteSideEffect(description);
        
        StringDescription buf = new StringDescription();
        sideEffect.describeTo(buf);
        assertEquals("should be description", description, buf.toString());
    }
}
