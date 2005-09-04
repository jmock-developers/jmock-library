package atest.jmock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.DynamicMockError;

public class CollectionsAcceptanceTest extends MockObjectTestCase {
    public interface T {
        void withList(List l);
        void withSet(Set s);
    }
    
    public void testCanMatchListContainingEqualElement() {
        Mock mockT = mock(T.class);
        
        mockT.expects(once()).method("withList").with(collectionContaining("a"));
        
        T t = (T)mockT.proxy();
        List list = new ArrayList();
        list.add("A");
        list.add("a");
        list.add("Aa");
        
        t.withList(list);
    }

    public void testCanMatchSetContainingEqualElement() {
        Mock mockT = mock(T.class);
        
        mockT.expects(once()).method("withSet").with(collectionContaining("a"));
        
        T t = (T)mockT.proxy();
        Set set = new HashSet();
        set.add("A");
        set.add("a");
        set.add("Aa");
        
        t.withSet(set);
    }
    
    public void testCanMatchCollectionContainingMatchingElement() {
        Mock mockT = mock(T.class);
        String string = "string";
        String equalString = new String(string);
        
        mockT.expects(atLeastOnce()).method("withList").with(collectionContaining(same(string)));
        
        T t = (T)mockT.proxy();
        
        t.withList(Collections.singletonList(string));
        
        try {
            t.withList(Collections.singletonList(equalString));
            fail("expected DynamicMockError");
        }
        catch (DynamicMockError e) {
            // expected
        }
    }
}
