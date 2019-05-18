package org.jmock.junit5.testdata.jmock.acceptance;

import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

@ExtendWith(JUnit5Mockery.class)
public class JUnit5TestThatDoesNotCreateAMockery {
    @RegisterExtension
    JUnit5Mockery context = null;
    
    @Test
    public void happy() {
        // a-ok!
    }
}
