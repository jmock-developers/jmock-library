/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.constraint;

import org.jmock.core.constraint.StringEndsWith;


public class StringEndsWithTest extends AbstractConstraintsTest
{
    static final String EXCERPT = "EXCERPT";
    
    StringEndsWith stringEndsWith;

    public void setUp() {
        stringEndsWith = new StringEndsWith(EXCERPT);
    }
    
    public void testEvaluatesToTrueIfArgumentContainsSpecifiedSubstring() {
        assertFalse("should be false if excerpt at beginning",
                   stringEndsWith.eval(EXCERPT + "END"));
        assertTrue("should be true if excerpt at end",
                    stringEndsWith.eval("START" + EXCERPT));
        assertFalse("should be false if excerpt in middle",
                    stringEndsWith.eval("START" + EXCERPT + "END"));
        assertTrue("should be true if excerpt is at end and repeated",
                   stringEndsWith.eval(EXCERPT + EXCERPT));
        
        assertFalse("should be false if excerpt is not in string",
                    stringEndsWith.eval("Something else"));
        assertFalse("should be false if part of excerpt is at end of string",
                    stringEndsWith.eval(EXCERPT.substring(0,EXCERPT.length()-2)));
    }
    
    public void testEvaluatesToTrueIfArgumentIsEqualToSubstring() {
        assertTrue("should be true if excerpt is entire string",
                   stringEndsWith.eval(EXCERPT));
    }
}
