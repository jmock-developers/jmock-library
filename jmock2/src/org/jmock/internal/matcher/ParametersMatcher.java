package org.jmock.internal.matcher;

import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsArray;
import org.hamcrest.core.IsEqual;

public class ParametersMatcher extends IsArray<Object> {
    public ParametersMatcher(Object[] expectedValues) {
        super(equalMatchersFor(expectedValues));
    }
    
    @SuppressWarnings("unchecked")
    private static Matcher<Object>[] equalMatchersFor(Object[] expectedValues) {
        Matcher<Object>[] matchers = new Matcher[expectedValues.length];
        for (int i = 0; i < expectedValues.length; i++) {
            matchers[i] = new IsEqual<Object>(expectedValues[i]);
        }
        return matchers;
    }
    
    @SuppressWarnings("unchecked")
    public ParametersMatcher(List<Matcher<?>> parameterMatchers) {
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
