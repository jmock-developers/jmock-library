package org.jmock.test.unit.internal;

import junit.framework.TestCase;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.internal.AllDeclaredFields;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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

    private Matcher<Collection<? extends Field>> isEmpty() {
        return empty();
    }
    
    public static class NoDeclaredFields {}

  @SuppressWarnings("UnusedDeclaration")
  public static class TwoDeclaredFields {
        public int field1;
        @SuppressWarnings("unused") private String field2;
    }
    @SuppressWarnings("UnusedDeclaration")
    public static class WithInheritedFields extends TwoDeclaredFields {
        protected char field3;
        TestCase field4;
    }

}
