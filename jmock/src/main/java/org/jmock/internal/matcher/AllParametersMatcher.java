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
    public boolean matchesSafely(Object[] parameters, Description mismatch) {
        return matchesNumberOfParameters(parameters, mismatch)
            && matchesParameters(parameters, mismatch);
    }

    private boolean matchesNumberOfParameters(Object[] parameters, Description mismatch) {
        if (elementMatchers.length != parameters.length) {
            mismatch.appendText("wrong number of parameters: ")
                    .appendValue(parameters);
            return false;
        }
        return true;
    }

    private boolean matchesParameters(Object[] parameters, Description mismatch) {
        boolean result = true;
        for (int i = 0; i < parameters.length; i++) {
            result &= matchesParameter(parameters[i], elementMatchers[i], mismatch, i);
        }
        return result;
    }

    private boolean matchesParameter(final Object value, final Matcher<Object> matcher, Description mismatch, int index) {
        mismatch.appendText("\n      parameter " + index + " ");
        final boolean parameterMatches = matcher.matches(value);
        if (parameterMatches) {
            mismatch.appendText("matched: ").appendDescriptionOf(matcher);
        } else {
            mismatch.appendText("did not match: ")
                .appendDescriptionOf(matcher)
                .appendText(", because ");
            matcher.describeMismatch(value, mismatch);
        }
        return parameterMatches;
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
