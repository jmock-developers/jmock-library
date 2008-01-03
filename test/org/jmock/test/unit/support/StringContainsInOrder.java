package org.jmock.test.unit.support;

import java.util.Arrays;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


public class StringContainsInOrder extends TypeSafeMatcher<String> {
    private final Iterable<String> substrings;

    public StringContainsInOrder(Iterable<String> substrings) {
        this.substrings = substrings;
    }

    @Override
    public boolean matchesSafely(String s) {
        int fromIndex = 0;

        for (String substring: substrings) {
            fromIndex = s.indexOf(substring, fromIndex);
            if (fromIndex == -1) {
                return false;
            }
        }

        return true;
    }

    public void describeTo(Description description) {
        description.appendText("a string containing ")
            .appendValueList("", ", ", "", substrings).appendText(" in order");
    }

    @Factory
    public static Matcher<String> containsInOrder(String... substrings) {
        return new StringContainsInOrder(Arrays.asList(substrings));
    }

    @Factory
    public static Matcher<String> containsInOrder(Iterable<String> substrings) {
        return new StringContainsInOrder(substrings);
    }

}
