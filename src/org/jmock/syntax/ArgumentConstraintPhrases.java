package org.jmock.syntax;

import org.hamcrest.Matcher;

public interface ArgumentConstraintPhrases {
    <T> T with(Matcher<T> matcher);
}
