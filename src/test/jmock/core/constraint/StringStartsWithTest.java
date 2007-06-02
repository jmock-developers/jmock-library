/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.constraint;

import org.jmock.core.constraint.StringStartsWith;


public class StringStartsWithTest extends AbstractConstraintsTest
{
    static final String EXCERPT = "EXCERPT";
    
    StringStartsWith stringStartsWith;

    public void setUp() {
        stringStartsWith = new StringStartsWith(EXCERPT);
    }
    
    public void testEvaluatesToTrueIfArgumentContainsSpecifiedSubstring() {
        assertTrue("should be true if excerpt at beginning",
                   stringStartsWith.eval(EXCERPT + "END"));
        assertFalse("should be false if excerpt at end",
                    stringStartsWith.eval("START" + EXCERPT));
        assertFalse("should be false if excerpt in middle",
                    stringStartsWith.eval("START" + EXCERPT + "END"));
        assertTrue("should be true if excerpt is at beginning and repeated",
                   stringStartsWith.eval(EXCERPT + EXCERPT));
        
        assertFalse("should be false if excerpt is not in string",
                    stringStartsWith.eval("Something else"));
        assertFalse("should be false if part of excerpt is at start of string",
                    stringStartsWith.eval(EXCERPT.substring(1)));
    }
    
    public void testEvaluatesToTrueIfArgumentIsEqualToSubstring() {
        assertTrue("should be true if excerpt is entire string",
                   stringStartsWith.eval(EXCERPT));
    }
}
