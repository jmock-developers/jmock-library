package test.jmock.core.constraint;

import org.jmock.core.constraint.StringContains;


public class StringContainsTest extends ConstraintsTest {
    static final String EXCERPT = "EXCERPT";
    
    StringContains stringContains;
    
    public void setUp() {
        stringContains = new StringContains(EXCERPT);
    }
    
    public void testEvaluatesToTrueIfArgumentContainsSpecifiedSubstring() {
        assertTrue( "should be true if excerpt at beginning", 
                    stringContains.eval(EXCERPT+"END") );
        assertTrue( "should be true if excerpt at end", 
                    stringContains.eval("START"+EXCERPT) );
        assertTrue( "should be true if excerpt in middle", 
                    stringContains.eval("START"+EXCERPT+"END") );
        assertTrue( "should be true if excerpt is repeated", 
                    stringContains.eval(EXCERPT+EXCERPT) );
        
        assertFalse( "should not be true if excerpt is not in string", 
                     stringContains.eval("Something else") );
        assertFalse( "should not be true if part of excerpt is in string",
                     stringContains.eval( EXCERPT.substring(1)) );
    }
    
    public void testEvaluatesToTrueIfArgumentIsEqualToSubstring() {
        assertTrue( "should be true if excerpt is entire string", 
                    stringContains.eval(EXCERPT) );
    }
}
