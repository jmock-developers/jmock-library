package org.jmock.test.unit.internal;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.Collection;

import junit.framework.TestCase;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.internal.AllDeclaredFields;

public class AllDeclaredFieldsTests extends TestCase {
    
    public void testReturnsEmptyListWhenNoFields() {
        assertThat(AllDeclaredFields.in(NoDeclaredFields.class), isEmpty());
    }
    
    @SuppressWarnings("unchecked")
    public void testReturnsFieldsForClassWithNoParent() {
        assertThat(AllDeclaredFields.in(TwoDeclaredFields.class),
                   containsInAnyOrder(aFieldCalled("field1"), aFieldCalled("field2")));
    }
    
    @SuppressWarnings("unchecked")
    public void testReturnsFieldsForClassWithParent() {
        assertThat(AllDeclaredFields.in(WithInheritedFields.class),
                   containsInAnyOrder(aFieldCalled("field1"), aFieldCalled("field2"), 
                                      aFieldCalled("field3"), aFieldCalled("field4")));
    }

    
    private Matcher<Field> aFieldCalled(String name) {
        return new FeatureMatcher<Field, String>(equalTo(name), "field with name", "field") {
            @Override
            protected String featureValueOf(Field actual) { return actual.getName();  }
        };
    }

    private Matcher<Collection<Field>> isEmpty() {
        return empty();
    }
    
    public static class NoDeclaredFields {};
    public static class TwoDeclaredFields {
        public int field1;
        @SuppressWarnings("unused") private String field2;
    }
    public static class WithInheritedFields extends TwoDeclaredFields {
        protected char field3;
        TestCase field4;
    }

}
