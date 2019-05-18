package org.jmock.junit5.testdata.jmock.acceptance;

import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(JUnit5Mockery.class)
public class JUnit5TestThatCreatesNoMockery {
    @Test
    public void happy() {
        // a-ok!
    }
}
