package org.jmock.junit5.testdata;

import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(JUnit5Mockery.class)
public class BaseClassWithMockery {
    protected JUnit5Mockery context = new JUnit5Mockery();
}
