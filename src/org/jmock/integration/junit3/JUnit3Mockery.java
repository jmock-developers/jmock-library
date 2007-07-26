package org.jmock.integration.junit3;

import org.jmock.Mockery;

/**
 * A {@link Mockery} that reports expectation errors as JUnit 3 test failures.
 *  
 * @author nat
 */
public class JUnit3Mockery extends Mockery {
    public JUnit3Mockery() {
        setExpectationErrorTranslator(JUnit3ErrorTranslator.INSTANCE);
    }
}
