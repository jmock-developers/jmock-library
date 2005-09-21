package test.jmock.core.constraint;

import java.util.HashMap;
import java.util.Map;

import org.jmock.core.Constraint;
import org.jmock.core.constraint.IsAnything;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.constraint.IsMapContaining;

public class IsMapContainingTest extends AbstractConstraintsTest {
    private static final IsAnything ANYTHING = IsAnything.INSTANCE;
    
    public void testMatchesMapContainingMatchingKeyAndValue() {
        Map map = new HashMap();
        map.put("a", "A");
        map.put("b", "B");
        
        Constraint constraintA = new IsMapContaining(new IsEqual("a"), new IsEqual("A"));
        Constraint constraintB = new IsMapContaining(new IsEqual("b"), new IsEqual("B"));
        Constraint constraintC = new IsMapContaining(new IsEqual("c"), new IsEqual("C"));
        
        assertMatches("constraintA", constraintA, map);
        assertMatches("constraintB", constraintB, map);
        assertDoesNotMatch("constraintC", constraintC, map);
    }
    
    public void testDoesNotMatchAnObjectThatIsNotAMap() {
        assertDoesNotMatch("should not match a string",
            new IsMapContaining(ANYTHING, ANYTHING), "not a map");
    }

    public void testDoesNotMatchNull() {
        assertDoesNotMatch("should not match a string",
            new IsMapContaining(ANYTHING, ANYTHING), null);
    }
    
    public void testHasReadableDescription() {
        Constraint constraint = new IsMapContaining(new IsEqual("a"), new IsEqual("A"));
        
        assertEquals("map containing [eq(\"a\")->eq(\"A\")]",
                	 constraint.describeTo(new StringBuffer()).toString());
    }
}
