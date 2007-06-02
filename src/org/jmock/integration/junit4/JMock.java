package org.jmock.integration.junit4;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.TestClassRunner;

public class JMock extends TestClassRunner {
    public JMock(Class<?> testClass) throws InitializationError {
        super(testClass, new JMockTestClassMethodsRunner(testClass));
    }
}
