package test.jmock.core.constraint;

import org.jmock.core.Constraint;
import org.jmock.core.constraint.IsEqual;


public class IsEqualTest extends ConstraintsTest {
    public void testComparesObjectsUsingEqualsMethod() {
        Integer i1 = new Integer(1);
        Integer i2 = new Integer(2);
        Constraint c = new IsEqual(i1);

        assertTrue(c.eval(i1));
        assertTrue(c.eval(new Integer(1)));
        assertTrue(!c.eval(i2));
    }

    public void testCanCompareNullValues() {
        Integer i1 = new Integer(1);
        Constraint c = new IsEqual(i1);
        
        assertTrue(!c.eval(null));
        Constraint nullEquals = new IsEqual(null);
        assertTrue(nullEquals.eval(null));
        assertTrue(!nullEquals.eval(i1));
    }
    
    public void testComparesTheElementsOfAnObjectArray() {
        String[] s1 = new String[]{"a", "b"};
        String[] s2 = new String[]{"a", "b"};
        String[] s3 = new String[]{"c", "d"};
        String[] s4 = new String[]{"a", "b", "c", "d"};

        Constraint c = new IsEqual(s1);

        assertTrue("Should equal itself", c.eval(s1));
        assertTrue("Should equal a similar array", c.eval(s2));
        assertTrue("Should not equal a different array", !c.eval(s3));
        assertTrue("Should not equal a different sized array", !c.eval(s4));
    }
    
    public void testComparesTheElementsOfAnArrayOfPrimitiveTypes() {
        int[] i1 = new int[]{1, 2};
        int[] i2 = new int[]{1, 2};
        int[] i3 = new int[]{3, 4};
        int[] i4 = new int[]{1, 2, 3, 4};
        
        Constraint c = new IsEqual(i1);

        assertTrue("Should equal itself", c.eval(i1));
        assertTrue("Should equal a similar array", c.eval(i2));
        assertTrue("Should not equal a different array", !c.eval(i3));
        assertTrue("Should not equal a different sized array", !c.eval(i4));
    }
    
    public void testRecursivelyTestsElementsOfArrays() {
        int[][] i1 = new int[][]{{1,2},{3,4}};
        int[][] i2 = new int[][]{{1,2},{3,4}};
        int[][] i3 = new int[][]{{5,6},{7,8}};
        int[] i4 = new int[]{1, 2, 3, 4};
        int[][] i5 = new int[][]{{1,2,3,4},{3,4}};
        
        Constraint c = new IsEqual(i1);

        assertTrue("Should equal itself", c.eval(i1));
        assertTrue("Should equal a similar array", c.eval(i2));
        assertTrue("Should not equal a different array", !c.eval(i3));
        assertTrue("Should not equal a different sized array", !c.eval(i4));
        assertTrue("Should not equal a different sized subarray", !c.eval(i5));
    }
    
    public void testIncludesTheResultOfCallingToStringOnItsArgumentInTheDescription() {
        final String argumentDescription = "ARGUMENT DESCRIPTION";
        Object argument = new Object() {
        	public String toString() {
        		return argumentDescription;
            }
        };
        Constraint c = new IsEqual(argument);
        
    	assertTrue( "should contain argument's toString in toString result",
            c.describeTo(new StringBuffer()).toString().indexOf(argumentDescription) >= 0 );
    }
    
    public void testReturnsAnObviousDescriptionIfCreatedWithANestedConstraintByMistake() {
        IsEqual innerConstraint = new IsEqual("NestedConstraint");
        assertEquals("should get an obvious description to reflect nesting if viewed in a debugger",
            "eq(<"+innerConstraint.toString()+">)", 
            (new IsEqual(innerConstraint)).describeTo(new StringBuffer()).toString());
    }
    
    public void testReturnsGoodDescriptionIfCreatedWithNullReference() {
        assertEquals("should get a description even if argument is null",
            "eq(null)", 
            new IsEqual(null).describeTo(new StringBuffer()).toString());
    }
}

