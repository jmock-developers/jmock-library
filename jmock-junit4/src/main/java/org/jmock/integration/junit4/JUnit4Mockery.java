package org.jmock.integration.junit4;

import org.jmock.Mockery;
import org.jmock.lib.AssertionErrorTranslator;

/**
 * A {@link Mockery} that reports expectation errors as JUnit 4 test failures.
 * @author nat
 * @deprecated migrate to JUnitRuleMockery or JUnit5Mockery  
 */
public class JUnit4Mockery extends Mockery {
    public JUnit4Mockery() {
        setExpectationErrorTranslator(AssertionErrorTranslator.INSTANCE);
    }
}
