package atest.jmock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.DynamicMockError;

public class CollectionsAcceptanceTest extends MockObjectTestCase {
    private Mock mockT = mock(T.class);
    private T t = (T)mockT.proxy();

    public interface T {
        void withArray(Object[] a);
        void withList(List l);
        void withSet(Set s);
        void withMap(Map m);
        void withObject(Object o);
    }

    public void testCanMatchArrayContainingEqualElement() {
        mockT.expects(once()).method("withArray").with(arrayContaining("a"));

        t.withArray(new String[]{"A","a","Aa"});

        try {
            t.withArray(new String[]{"B","b","Bb"});
            fail("should not have been expected");
        }
        catch (DynamicMockError e) {
            // expected
        }
    }
    
    public void testCanMatchArrayContainingMatchingElement() {
        mockT.expects(atLeastOnce()).method("withArray").with(arrayContaining(eq("a")));

        t.withArray(new String[]{"A","a","Aa"});

        try {
            t.withArray(new String[]{"B","b","Bb"});
            fail("should not have been expected");
        }
        catch (DynamicMockError e) {
            // expected
        }
    }
    
    public void testCanMatchElementOfArray() {
        mockT.expects(atLeastOnce()).method("withObject").with(isIn(new String[]{"A","a","Aa"}));
        
        t.withObject("a");
        
        try {
            t.withObject("not an element");
            fail("should not have been expected");
        }
        catch (DynamicMockError e) {
            // expected
        }
    }
    
    public void testCanMatchListContainingEqualElement() {
        mockT.expects(once()).method("withList").with(collectionContaining("a"));
        
        List list = new ArrayList();
        list.add("A");
        list.add("a");
        list.add("Aa");
        
        t.withList(list);
    }
    
    public void testCanMatchSetContainingEqualElement() {
        mockT.expects(once()).method("withSet").with(collectionContaining("a"));
        
        Set set = new HashSet();
        set.add("A");
        set.add("a");
        set.add("Aa");
        
        t.withSet(set);
    }
    
    public void testCanMatchCollectionContainingMatchingElement() {
        String string = "string";
        String equalString = new String(string);
        
        mockT.expects(atLeastOnce()).method("withList").with(collectionContaining(same(string)));
        
        t.withList(Collections.singletonList(string));
        
        try {
            t.withList(Collections.singletonList(equalString));
            fail("expected DynamicMockError");
        }
        catch (DynamicMockError e) {
            // expected
        }
    }
    
    public void testCanMatchElementOfCollection() {
        mockT.expects(atLeastOnce()).method("withObject").with(isIn(Arrays.asList(new String[]{"A","a","Aa"})));
        
        t.withObject("a");
        
        try {
            t.withObject("not an element");
            fail("should not have been expected");
        }
        catch (DynamicMockError e) {
            // expected
        }
    }
    
    public void testCanMatchMapContainingEntryValues() {
        mockT.expects(atLeastOnce()).method("withMap").with(mapContaining("a","A"));
        
        Map goodMap = new HashMap();
        goodMap.put("a", "A");
        
        Map badMap =  new HashMap();
        badMap.put("a", "B");
        
        t.withMap(goodMap);
        
        try {
            t.withMap(badMap);
            fail("should not have been expected");
        }
        catch (DynamicMockError e) {
            // expected
        }
    }
    
    public void testCanMatchMapContainingEntryMatchingConstraints() {
        mockT.expects(atLeastOnce()).method("withMap").with(mapContaining(eq("a"),eq("A")));
        
        Map goodMap = new HashMap();
        goodMap.put("a", "A");
        
        Map badMap =  new HashMap();
        badMap.put("a", "B");
        
        t.withMap(goodMap);

        try {
            t.withMap(badMap);
            fail("should not have been expected");
        }
        catch (DynamicMockError e) {
            // expected
        }
    }
    
    public void testCanMatchMapContainingEqualKey() {
        mockT.expects(atLeastOnce()).method("withMap").with(mapWithKey("a"));
        
        Map goodMap = new HashMap();
        goodMap.put("a", "A");
        
        Map badMap =  new HashMap();
        badMap.put("b", "B");
        
        t.withMap(goodMap);
        
        try {
            t.withMap(badMap);
            fail("should not have been expected");
        }
        catch (DynamicMockError e) {
            // expected
        }
    }
    
    public void testCanMatchMapContainingMatchingKey() {
        mockT.expects(atLeastOnce()).method("withMap").with(mapWithKey(eq("a")));
        
        Map goodMap = new HashMap();
        goodMap.put("a", "A");
        
        Map badMap =  new HashMap();
        badMap.put("b", "B");
        
        t.withMap(goodMap);

        try {
            t.withMap(badMap);
            fail("should not have been expected");
        }
        catch (DynamicMockError e) {
            // expected
        }
    }
    
    public void testCanMatchMapContainingEqualValue() {
        mockT.expects(atLeastOnce()).method("withMap").with(mapWithValue("A"));
        
        Map goodMap = new HashMap();
        goodMap.put("a", "A");
        
        Map badMap =  new HashMap();
        badMap.put("b", "B");
        
        t.withMap(goodMap);
        
        try {
            t.withMap(badMap);
            fail("should not have been expected");
        }
        catch (DynamicMockError e) {
            // expected
        }
        
    }
    
    public void testCanMatchMapContainingMatchingValue() {
        mockT.expects(atLeastOnce()).method("withMap").with(mapWithValue(eq("A")));
        
        Map goodMap = new HashMap();
        goodMap.put("a", "A");
        
        Map badMap =  new HashMap();
        badMap.put("b", "B");
        
        t.withMap(goodMap);

        try {
            t.withMap(badMap);
            fail("should not have been expected");
        }
        catch (DynamicMockError e) {
            // expected
        }
    }
}
