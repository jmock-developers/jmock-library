package org.jmock.junit5.testdata.jmock.acceptance;

import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class JUnit5TestThatCreatesTwoMockeries {
    @RegisterExtension
    JUnit5Mockery contextA = new JUnit5Mockery();
    @RegisterExtension
    JUnit5Mockery contextB = new JUnit5Mockery();
    
    @Test
    public void happy() {
        // a-ok!
    }

}
