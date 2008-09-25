package org.jmock.internal.matcher;

import static java.util.Arrays.asList;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.core.IsEqual;
import org.jmock.internal.ParametersMatcher;

public class AllParametersMatcher extends TypeSafeDiagnosingMatcher<Object[]>  implements ParametersMatcher {
    private final Matcher<Object>[] elementMatchers;

    public AllParametersMatcher(Object[] expectedValues) {
        this.elementMatchers =  equalMatchersFor(expectedValues);
    }
    
    @SuppressWarnings("unchecked")
    public AllParametersMatcher(List<Matcher<?>> parameterMatchers) {
        this.elementMatchers = parameterMatchers.toArray(new Matcher[0]);
    }

    public boolean isCompatibleWith(Object[] parameters) {
        return elementMatchers.length == parameters.length;
    }

    @Override
    public boolean matchesSafely(Object[] parameters, Description description) {
        boolean result = true;
        for (int i = 0; i < parameters.length; i++) {
            final Matcher<Object> matcher = elementMatchers[i];
            final Object value = parameters[i];
            
            description.appendText("\n      parameter " + i + " ");
            if (! matcher.matches(value)) {
                description.appendText("did not match: ")
                    .appendDescriptionOf(matcher)
                    .appendText(", because ");
                matcher.describeMismatch(value, description);
                result = false;
            } else {
                description.appendText("matched: ").appendDescriptionOf(matcher);
            }
            
        }
        
        return result;
    }

    public void describeTo(Description description) {
        description.appendList("(", ", ",")", asList(elementMatchers));
    }
    
    @SuppressWarnings("unchecked")
    private static Matcher<Object>[] equalMatchersFor(Object[] expectedValues) {
        Matcher<Object>[] matchers = new Matcher[expectedValues.length];
        for (int i = 0; i < expectedValues.length; i++) {
            matchers[i] = new IsEqual<Object>(expectedValues[i]);
        }
        return matchers;
    }

}
