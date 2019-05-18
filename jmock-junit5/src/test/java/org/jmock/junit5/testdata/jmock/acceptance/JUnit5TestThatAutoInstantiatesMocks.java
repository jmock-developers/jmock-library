package org.jmock.junit5.testdata.jmock.acceptance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.jmock.Sequence;
import org.jmock.States;
import org.jmock.auto.Auto;
import org.jmock.auto.Mock;
import org.junit.jupiter.api.Test;

public class JUnit5TestThatAutoInstantiatesMocks extends BaseClassWithMockery {
    @Mock Runnable runnable;
    @Auto States states;
    @Auto Sequence sequence;
    
    @Test
    public void fieldsHaveBeenAutoInstantiated() {
        assertThat(runnable, notNullValue());
        assertThat(states, notNullValue());
        assertThat(sequence, notNullValue());
    }
}
