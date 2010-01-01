package org.jmock.test.acceptance.junit4.testdata;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.States;
import org.jmock.auto.Auto;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JMockContext;
import org.junit.Rule;
import org.junit.Test;

public class JUnit4WithRulesExamples {
    public static class SatisfiesExpectations {
        @Rule public final JMockContext context = new JMockContext();
        private final Runnable runnable = context.mock(Runnable.class);
        
        @Test
        public void doesSatisfyExpectations() {
            context.checking(new Expectations() {{
                oneOf (runnable).run();
            }});
            
            runnable.run();
        }
    }
    
    public static class DoesNotSatisfyExpectations {
        @Rule public final JMockContext context = new JMockContext();
        private Runnable runnable = context.mock(Runnable.class);
        
        @Test
        public void doesNotSatisfyExpectations() {
            context.checking(new Expectations() {{
                oneOf (runnable).run();
            }});
            
            // Return without satisfying the expectation for runnable.run()
        }
    }

    
    public static class DerivedAndDoesNotSatisfyExpectations extends BaseClassWithJMockContext {
        private Runnable runnable = context.mock(Runnable.class);
        
        @Test
        public void doesNotSatisfyExpectations() {
            context.checking(new Expectations() {{
                oneOf (runnable).run();
            }});
            
            // Return without satisfying the expectation for runnable.run()
        }
    }

    public static class ThrowsExpectedException {
        @Rule public final JMockContext context = new JMockContext();
        private WithException withException = context.mock(WithException.class);
        
        @Test(expected=CheckedException.class)
        public void doesNotSatisfyExpectationsWhenExpectedExceptionIsThrown() throws CheckedException {
            context.checking(new Expectations() {{
                oneOf (withException).anotherMethod();
                oneOf (withException).throwingMethod(); will(throwException(new CheckedException()));
            }});
            
            withException.throwingMethod();
        }
        
        public static class  CheckedException extends Exception {
        }
        
        public interface WithException {
            void throwingMethod() throws CheckedException;
            void anotherMethod();
        }
    }

    public static class CreatesTwoMockeries extends BaseClassWithJMockContext {
        @Rule public final JMockContext otherContext = new JMockContext();

        @Test
        public void doesNothing() {
            // no op
        }
    }

    public static class AutoInstantiatesMocks extends BaseClassWithJMockContext {
        @Mock Runnable runnable;
        @Auto States states;
        @Auto Sequence sequence;
        
        @Test
        public void fieldsHaveBeenAutoInstantiated() {
            assertThat("runnable", runnable, notNullValue());
            assertThat("states", states, notNullValue());
            assertThat("sequence", sequence, notNullValue());
        }
    }

    
    public static class BaseClassWithJMockContext {
        @Rule public final JMockContext context = new JMockContext();
    }

}
