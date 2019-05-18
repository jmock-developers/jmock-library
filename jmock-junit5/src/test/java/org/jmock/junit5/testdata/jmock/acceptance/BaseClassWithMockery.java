package org.jmock.junit5.testdata.jmock.acceptance;

import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.extension.RegisterExtension;

public class BaseClassWithMockery {
    @RegisterExtension
    JUnit5Mockery context = new JUnit5Mockery();
}
