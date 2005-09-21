package test.jmock.core.constraint;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.jmock.core.Constraint;
import org.jmock.core.constraint.IsCollectionContaining;
import org.jmock.core.constraint.IsEqual;

public class IsCollectionContainingTest extends IsInTest {
    Constraint constraint = new IsCollectionContaining(new IsEqual("a"));
    
    public void testMatchesACollectionThatContainsAnElementMatchingTheGivenConstraint() {
        assertMatches("should match list that contains 'a'", 
            constraint, collectionOf(new String[]{"a", "b", "c"}));
    }
    
    public void testDoesNotMatchCollectionThatDoesntContainAnElementMatchingTheGivenConstraint() {
        assertDoesNotMatch("should not match list that doesn't contain 'a'", 
            constraint, collectionOf(new String[]{"b", "c"}));
        assertDoesNotMatch("should not match empty list", 
            constraint, emptyCollection());
    }
    
    public void testDoesNotMatchNull() {
        assertDoesNotMatch("should not match null", constraint, null);
    }
    
    public void testDoesNotMatchObjectThatIsNotCollections() {
        assertDoesNotMatch("should not match empty list", constraint, "not a collection");
    }
    
    public void testHasAReadableDescription() {
        Constraint constraint = new IsCollectionContaining(new IsEqual("a"));
        
        assertEquals("a collection containing eq(\"a\")", 
                	 constraint.describeTo(new StringBuffer()).toString());
    }
    
    private Collection collectionOf(String[] abc) {
        return Arrays.asList(abc);
    }
    
    private Collection emptyCollection() {
        return Collections.EMPTY_LIST;
    }
}
