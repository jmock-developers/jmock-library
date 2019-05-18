package org.jmock.junit5.testdata.jmock.acceptance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.States;
import org.jmock.auto.Auto;
import org.jmock.auto.Mock;
import org.jmock.junit5.JUnit5Mockery;
import org.jmock.junit5.extensions.ExpectationExtension;
import org.jmock.junit5.extensions.ExpectationThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

public class JUnit5WithRulesExamples {
    public static class SatisfiesExpectations {
        @RegisterExtension
        public final JUnit5Mockery context = new JUnit5Mockery();
        private final Runnable runnable = context.mock(Runnable.class);

        @Test
        public void doesSatisfyExpectations() {
            context.checking(new Expectations() {
                {
                    oneOf(runnable).run();
                }
            });

            runnable.run();
        }
    }

    public static class DoesNotSatisfyExpectations {
        @RegisterExtension
        public final JUnit5Mockery context = new JUnit5Mockery();
        private Runnable runnable = context.mock(Runnable.class);

        @Test
        public void doesNotSatisfyExpectations() {
            context.checking(new Expectations() {
                {
                    oneOf(runnable).run();
                }
            });

            // Return without satisfying the expectation for runnable.run()
        }
    }

    public static class DerivedAndDoesNotSatisfyExpectations extends BaseClassWithJMockContext {
        private Runnable runnable = context.mock(Runnable.class);

        @Test
        public void doesNotSatisfyExpectations() {
            context.checking(new Expectations() {
                {
                    oneOf(runnable).run();
                }
            });

            // Return without satisfying the expectation for runnable.run()
        }
    }

    @ExtendWith(ExpectationExtension.class)
    public static class ThrowsExpectedException {
        @RegisterExtension
        public final JUnit5Mockery context = new JUnit5Mockery();
        private WithException withException = context.mock(WithException.class);

        @Test
        @ExpectationThrows(expected = CheckedException.class)
        public void doesNotSatisfyExpectationsWhenExpectedExceptionIsThrown() throws CheckedException {
            context.checking(new Expectations() {
                {
                    oneOf(withException).anotherMethod();
                    oneOf(withException).throwingMethod();
                    will(throwException(new CheckedException()));
                }
            });

            withException.throwingMethod();
        }

        @SuppressWarnings("serial")
        public static class CheckedException extends Exception {
        }

        public interface WithException {
            void throwingMethod() throws CheckedException;

            void anotherMethod();
        }
    }

    public static class CreatesTwoMockeries extends BaseClassWithJMockContext {
        @RegisterExtension
        public final JUnit5Mockery context = new JUnit5Mockery();

        @Test
        public void doesNothing() {
            // no op
        }
    }

    public static class AutoInstantiatesMocks extends BaseClassWithJMockContext {
        @Mock
        Runnable runnable;
        @Auto
        States states;
        @Auto
        Sequence sequence;

        @Test
        public void fieldsHaveBeenAutoInstantiated() {
            assertThat("runnable", runnable, notNullValue());
            assertThat("states", states, notNullValue());
            assertThat("sequence", sequence, notNullValue());
        }
    }

    public static class BaseClassWithJMockContext {
        @RegisterExtension
        public final JUnit5Mockery context = new JUnit5Mockery();
    }

}
