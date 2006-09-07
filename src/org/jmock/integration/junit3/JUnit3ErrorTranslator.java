package org.jmock.integration.junit3;

import junit.framework.AssertionFailedError;

import org.hamcrest.StringDescription;
import org.jmock.core.ExpectationError;
import org.jmock.core.ExpectationErrorTranslator;

public class JUnit3ErrorTranslator implements ExpectationErrorTranslator {
    public static final JUnit3ErrorTranslator INSTANCE = new JUnit3ErrorTranslator();
    
    public Error translate(ExpectationError e) {
        return new AssertionFailedError(StringDescription.toString(e));
    }
}
