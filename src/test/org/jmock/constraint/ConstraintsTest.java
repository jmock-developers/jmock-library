/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.constraint;

import junit.framework.TestCase;
import org.jmock.Constraint;
import org.jmock.Mock;
import org.jmock.dynamic.DummyInterface;
import org.jmock.expectation.AssertMo;

import java.util.EventObject;

public class ConstraintsTest extends TestCase {
    class True implements Constraint {
        public boolean eval(Object o) {
            return true;
        }
    }

    class False implements Constraint {
        public boolean eval(Object o) {
            return false;
        }
    }

    /**
     * Creates a new instance of Test_Predicates
     */
    public ConstraintsTest(String test) {
        super(test);
    }

    public void testIsNull() {
        Constraint p = new IsNull();

        assertTrue(p.eval(null));
        assertTrue(!p.eval(new Object()));
    }

    public void testIsSame() {
        Object o1 = new Object();
        Object o2 = new Object();
        Constraint p = new IsSame(o1);

        assertTrue(p.eval(o1));
        assertTrue(!p.eval(o2));
    }

    public void testIsEqual() {
        Integer i1 = new Integer(1);
        Integer i2 = new Integer(2);
        Constraint p = new IsEqual(i1);

        assertTrue(p.eval(i1));
        assertTrue(p.eval(new Integer(1)));
        assertTrue(!p.eval(i2));
    }

    public void testIsEqualNull() {
        Integer i1 = new Integer(1);
        Constraint p = new IsEqual(i1);
        assertTrue(!p.eval(null));
        Constraint nullEquals = new IsEqual(null);
        assertTrue(nullEquals.eval(null));
        assertTrue(!nullEquals.eval(i1));
    }

    public void testIsEqualObjectArray() {
        String[] s1 = new String[]{"a", "b"};
        String[] s2 = new String[]{"a", "b"};
        String[] s3 = new String[]{"c", "d"};
        String[] s4 = new String[]{"a", "b", "c", "d"};

        Constraint p = new IsEqual(s1);

        assertTrue("Should equal itself", p.eval(s1));
        assertTrue("Should equal a similar array", p.eval(s2));
        assertTrue("Should not equal a different array", !p.eval(s3));
        assertTrue("Should not equal a different sized array", !p.eval(s4));
    }

    public void testIsEqualToStringForNestedConstraint() {
        assertEquals("Should get an obvious toString to reflect nesting if viewed in a debugger",
                " =  = NestedConstraint", new IsEqual(new IsEqual("NestedConstraint")).toString());
    }

    public void testIsEqualToStringOnProxyArgument() {
        // Required for error message reporting
        Mock mockDummyInterface = new Mock(DummyInterface.class, "MockName");
        Constraint p = new IsEqual(mockDummyInterface.proxy());

        AssertMo.assertIncludes("Should get resolved toString() with no expectation error", "MockName", p.toString());
    }

    public void testIsEqualNullToString() {
        assertEquals("Should print toString even if argument is null",
                " = null", new IsEqual(null).toString());
    }

    public void testIsEqualEquals() throws Exception {
        assertEquals("Should be equal", new IsEqual("a"), new IsEqual("a"));
        assertFalse("Should not be equal - same type different values", new IsEqual("a").equals(new IsEqual("b")));
        assertFalse("Should not be equal - different type", new IsEqual("a").equals("b"));
    }

    public void testIsGreaterThan() {
        Constraint p = new IsGreaterThan(new Integer(1));

        assertTrue(!p.eval(new Integer(0)));
        assertTrue(!p.eval(new Integer(1)));
        assertTrue(p.eval(new Integer(2)));
    }

    public void testIsLessThan() {
        Constraint p = new IsLessThan(new Integer(1));

        assertTrue(p.eval(new Integer(0)));
        assertTrue(!p.eval(new Integer(1)));
        assertTrue(!p.eval(new Integer(2)));
    }

    public void testIsAnything() {
        Constraint p = new IsAnything();
        assertTrue(p.eval(null));
        assertTrue(p.eval(new Object()));
    }

    public void testIsInstanceOf() {
        Constraint p = new IsInstanceOf(Number.class);
        assertTrue(p.eval(new Integer(1)));
        assertTrue(p.eval(new Double(1.0)));
        assertTrue(!p.eval("a string"));
        assertTrue(!p.eval(null));
    }

    public void testIsNot() {
        Constraint p = new IsNot(new True());
        assertTrue(!p.eval(null));
        assertTrue(!p.eval(new Object()));
    }

    public void testAnd() {
        Object o = new Object();
        assertTrue(new And(new True(), new True()).eval(o));
        assertTrue(!new And(new False(), new True()).eval(o));
        assertTrue(!new And(new True(), new False()).eval(o));
        assertTrue(!new And(new False(), new False()).eval(o));
    }

    public void testOr() {
        Object o = new Object();
        assertTrue(new Or(new True(), new True()).eval(o));
        assertTrue(new Or(new False(), new True()).eval(o));
        assertTrue(new Or(new True(), new False()).eval(o));
        assertTrue(!new Or(new False(), new False()).eval(o));
    }

    public void testIsEventFrom() {
        Object o = new Object();
        EventObject ev = new EventObject(o);
        EventObject ev2 = new EventObject(new Object());

        Constraint p = new IsEventFrom(o);

        assertTrue(p.eval(ev));
        assertTrue("p should eval to false for an event not from o",
                !p.eval(ev2));
        assertTrue("p should eval to false for objects that are not events",
                !p.eval(o));
    }

    private static class DerivedEvent extends EventObject {
        public DerivedEvent(Object source) {
            super(source);
        }
    }

    public void testIsEventSubtypeFrom() {
        Object o = new Object();
        DerivedEvent good_ev = new DerivedEvent(o);
        DerivedEvent wrong_source = new DerivedEvent(new Object());
        EventObject wrong_type = new EventObject(o);
        EventObject wrong_source_and_type = new EventObject(new Object());

        Constraint p = new IsEventFrom(DerivedEvent.class, o);

        assertTrue(p.eval(good_ev));
        assertTrue("p should eval to false for an event not from o",
                !p.eval(wrong_source));
        assertTrue("p should eval to false for an event of the wrong type",
                !p.eval(wrong_type));
        assertTrue("p should eval to false for an event of the wrong type " +
                "and from the wrong source",
                !p.eval(wrong_source_and_type));
    }

    public void testIsCloseTo() {
        Constraint p = new IsCloseTo(1.0, 0.5);

        assertTrue(p.eval(new Double(1.0)));
        assertTrue(p.eval(new Double(0.5)));
        assertTrue(p.eval(new Double(1.5)));

        assertTrue(p.eval(new Float(1.0)));
        assertTrue(p.eval(new Integer(1)));

        assertTrue("number too large", !p.eval(new Double(2.0)));
        assertTrue("number too small", !p.eval(new Double(0.0)));

        try {
            p.eval("wrong type");
            fail("ClassCastException expected for wrong type of argument");
        } catch (ClassCastException ex) {
            // expected
        }
    }
}
