package org.jmock.integration.junit3;

import org.jmock.Mockery;

public class JUnit3Mockery extends Mockery {
    public JUnit3Mockery() {
        setExpectationErrorTranslator(JUnit3ErrorTranslator.INSTANCE);
    }
}
