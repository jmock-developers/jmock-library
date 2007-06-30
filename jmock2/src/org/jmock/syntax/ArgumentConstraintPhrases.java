package org.jmock.syntax;

import org.hamcrest.Matcher;

public interface ArgumentConstraintPhrases {
    <T> T with(Matcher<T> matcher);
    boolean with(Matcher<Boolean> matcher);
    byte with(Matcher<Byte> matcher);
    short with(Matcher<Short> matcher);
    int with(Matcher<Integer> matcher);
    long with(Matcher<Long> matcher);
    float with(Matcher<Float> matcher);
    double with(Matcher<Double> matcher);
}
