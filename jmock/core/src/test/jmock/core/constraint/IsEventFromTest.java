/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.constraint;

import java.util.EventObject;
import org.jmock.core.constraint.IsEventFrom;


public class IsEventFromTest extends ConstraintsTest
{
    public void testEvaluatesToTrueIfArgumentIsAnEventObjectFiredByASpecifiedSource() {
        Object o = new Object();
        EventObject ev = new EventObject(o);
        EventObject ev2 = new EventObject(new Object());

        IsEventFrom p = new IsEventFrom(o);

        assertTrue(p.eval(ev));
        assertTrue("p should eval to false for an event not from o",
                   !p.eval(ev2));
        assertTrue("p should eval to false for objects that are not events",
                   !p.eval(o));
    }

    private static class DerivedEvent extends EventObject
    {
        public DerivedEvent( Object source ) {
            super(source);
        }
    }

    public void testCanTestForSpecificEventClasses() {
        Object o = new Object();
        DerivedEvent good_ev = new DerivedEvent(o);
        DerivedEvent wrong_source = new DerivedEvent(new Object());
        EventObject wrong_type = new EventObject(o);
        EventObject wrong_source_and_type = new EventObject(new Object());

        IsEventFrom p = new IsEventFrom(DerivedEvent.class, o);

        assertTrue(p.eval(good_ev));
        assertTrue("p should eval to false for an event not from o",
                   !p.eval(wrong_source));
        assertTrue("p should eval to false for an event of the wrong type",
                   !p.eval(wrong_type));
        assertTrue("p should eval to false for an event of the wrong type " +
                   "and from the wrong source",
                   !p.eval(wrong_source_and_type));
    }
}
