package test.jmock.core.constraint;

import org.jmock.core.constraint.IsCloseTo;


public class IsCloseToTest extends ConstraintsTest {
    
    public void testEvaluatesToTrueIfArgumentIsEqualToADoubleValueWithinSomeError() {
        IsCloseTo p = new IsCloseTo(1.0, 0.5);

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
