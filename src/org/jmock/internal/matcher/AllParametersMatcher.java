package org.jmock.internal.matcher;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsArray;
import org.hamcrest.core.IsEqual;
import org.jmock.internal.ParametersMatcher;

public class AllParametersMatcher extends IsArray<Object> implements ParametersMatcher {
    public AllParametersMatcher(Object[] expectedValues) {
        super(equalMatchersFor(expectedValues));
    }


    public boolean isCompatibleWith(Object[] parameters) {
        return elementMatchers.length == parameters.length;
    }

    @SuppressWarnings("unchecked")
    private static Matcher<Object>[] equalMatchersFor(Object[] expectedValues) {
        Matcher<Object>[] matchers = new Matcher[expectedValues.length];
        for (int i = 0; i < expectedValues.length; i++) {
            matchers[i] = new IsEqual<Object>(expectedValues[i]);
        }
        return matchers;
    }
    
    @Override
    protected boolean matchesValuesSafely(Object[] values, Matcher<Object>[] matchers, Description description) {
        boolean result = true;
        description.appendText("      received values: (");
        for (int i = 0; i < values.length; i++) {
            final Matcher<Object> matcher = matchers[i];
            
            if (! matcher.matches(values[i])) {
              description.appendText("mismatch<");
              result = false;
            } else {
              description.appendText("match<");
            }
            matcher.describeMismatch(values[i], description);
            description.appendText(">");
            if (i < values.length - 1) {
                description.appendText(", ");
            } else {
                description.appendText(")");
            }
        }
        
        return result;
      }

    @SuppressWarnings("unchecked")
    public AllParametersMatcher(List<Matcher<?>> parameterMatchers) {
        super(parameterMatchers.toArray(new Matcher[0]));
    }

    @Override
    protected String descriptionStart() {
        return "(";
    }
    
    @Override
    protected String descriptionEnd() {
        return ")";
    }
}
