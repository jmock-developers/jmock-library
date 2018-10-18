package org.jmock.junit5.testdata;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jmock.Mockery;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(JUnit5Mockery.class)
public class JUnit5TestWithNonPublicBeforeMethod {
    @SuppressWarnings("unused")
    private Mockery context = new Mockery();

    public boolean beforeWasCalled = false;

    @BeforeEach
    void before() {
        beforeWasCalled = true;
    }

    @org.junit.jupiter.api.Test
    public void beforeShouldBeCalled() {
        assertTrue(beforeWasCalled, "before was called");
    }
}
