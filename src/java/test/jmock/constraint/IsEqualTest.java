package test.jmock.constraint;

import junit.framework.TestCase;

import org.jmock.Constraint;
import org.jmock.constraint.IsEqual;
import org.jmock.dynamock.Mock;
import org.jmock.expectation.AssertMo;

import test.jmock.dynamic.DummyInterface;


public class IsEqualTest extends TestCase {
    public void testComparesObjectsUsingEqualsMethod() {
        Integer i1 = new Integer(1);
        Integer i2 = new Integer(2);
        Constraint p = new IsEqual(i1);

        assertTrue(p.eval(i1));
        assertTrue(p.eval(new Integer(1)));
        assertTrue(!p.eval(i2));
    }

    public void testCanCompareNullValues() {
        Integer i1 = new Integer(1);
        Constraint p = new IsEqual(i1);
        assertTrue(!p.eval(null));
        Constraint nullEquals = new IsEqual(null);
        assertTrue(nullEquals.eval(null));
        assertTrue(!nullEquals.eval(i1));
    }

    public void testComparesTheElementsOfAnObjectArray() {
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

    public void testReturnsAnObviousDescriptionIfCreatedWithANestedConstraint() {
        assertEquals("Should get an obvious toString to reflect nesting if viewed in a debugger",
            " =  = NestedConstraint", new IsEqual(new IsEqual("NestedConstraint")).toString());
    }

    public void testReturnsMockNameAsDescriptionIfCreatedWithProxyOfMock() {
        // Required for error message reporting
        Mock mockDummyInterface = new Mock(DummyInterface.class, "MockName");
        Constraint p = new IsEqual(mockDummyInterface.proxy());

        AssertMo.assertIncludes("should get resolved toString() with no expectation error", 
            "MockName", p.toString());
    }

    public void testReturnsGoodDescriptionIfCreatedWithNullReference() {
        assertEquals("Should print toString even if argument is null",
            " = null", new IsEqual(null).toString());
    }
    
}
