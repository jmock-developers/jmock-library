/*  Copyright (c) 2000-2004 jMock.org
 */
package uk.jamesdal.perfmock.test.unit.lib.action;

import junit.framework.TestCase;

import org.hamcrest.StringDescription;
import uk.jamesdal.perfmock.api.Invocation;
import uk.jamesdal.perfmock.lib.action.VoidAction;
import uk.jamesdal.perfmock.test.unit.support.AssertThat;
import uk.jamesdal.perfmock.test.unit.support.MethodFactory;


public class VoidActionTests extends TestCase {
    Invocation invocation;
    VoidAction voidAction;

    @Override
    public void setUp() {
        MethodFactory methodFactory = new MethodFactory();
        invocation = new Invocation("INVOKED-OBJECT", methodFactory.newMethodReturning(void.class), new Object[0]);
        voidAction = new VoidAction();
    }

    public void testReturnsNullWhenInvoked() throws Throwable {
        assertNull("Should return null",
                   new VoidAction().invoke(invocation));
    }

    public void testIncludesVoidInDescription() {
        AssertThat.stringIncludes("contains 'void' in description",
            "void", StringDescription.toString(voidAction));
    }
}
