/*  Copyright (c) 2000-2004 jMock.org
 */
package uk.jamesdal.perfmock.test.unit.lib.action;

import junit.framework.TestCase;

import org.hamcrest.StringDescription;
import uk.jamesdal.perfmock.api.Invocation;
import uk.jamesdal.perfmock.lib.action.CustomAction;


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
