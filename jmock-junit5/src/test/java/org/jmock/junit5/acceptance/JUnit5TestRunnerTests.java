package org.jmock.junit5.acceptance;

import org.jmock.junit5.JUnit5Mockery;
import org.jmock.junit5.testdata.DerivedJUnit4TestThatDoesNotSatisfyExpectations;
import org.jmock.junit5.testdata.JUnit5TestThatAutoInstantiatesMocks;
import org.jmock.junit5.testdata.JUnit5TestThatCreatesNoMockery;
import org.jmock.junit5.testdata.JUnit5TestThatCreatesTwoMockeries;
import org.jmock.junit5.testdata.JUnit5TestThatDoesNotCreateAMockery;
import org.jmock.junit5.testdata.JUnit5TestThatDoesNotSatisfyExpectations;
import org.jmock.junit5.testdata.JUnit5TestThatDoesSatisfyExpectations;
import org.jmock.junit5.testdata.JUnit5TestThatThrowsExpectedException;
import org.jmock.junit5.testdata.JUnit5TestWithNonPublicBeforeMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(JUnit5Mockery.class)
public class JUnit5TestRunnerTests {
    FailureRecordingTestExecutionListener listener = new FailureRecordingTestExecutionListener();
    
    @Test
    public void testTheJUnit4TestRunnerReportsPassingTestsAsSuccessful() {
        listener.runTestIn(JUnit5TestThatDoesSatisfyExpectations.class);
        listener.assertTestSucceeded();
    }
    
    @Test
    public void testTheJUnit4TestRunnerAutomaticallyAssertsThatAllExpectationsHaveBeenSatisfied() {
        listener.runTestIn(JUnit5TestThatDoesNotSatisfyExpectations.class);
        listener.assertTestFailedWith(AssertionError.class);
    }
    
    @Test
    public void testTheJUnit4TestRunnerLooksForTheMockeryInBaseClasses() {
        listener.runTestIn(DerivedJUnit4TestThatDoesNotSatisfyExpectations.class);
        listener.assertTestFailedWith(AssertionError.class);
    }
    
    @Test
    public void testTheJUnit4TestRunnerReportsAHelpfulErrorIfTheMockeryIsNull() {
        listener.runTestIn(JUnit5TestThatDoesNotCreateAMockery.class);
        listener.assertTestFailedWith(IllegalStateException.class);
    }
    
    // See issue JMOCK-156
    @Test
    public void testReportsMocksAreNotSatisfiedWhenExpectedExceptionIsThrown() {
        listener.runTestIn(JUnit5TestThatThrowsExpectedException.class);
        listener.assertTestFailedWith(AssertionError.class);
    }

    // See issue JMOCK-219
    @Test
    public void testTheJUnit4TestRunnerReportsIfNoMockeryIsFound() {
        listener.runTestIn(JUnit5TestThatCreatesNoMockery.class);
        listener.assertTestFailedWithInitializationError();
    }

    // See issue JMOCK-219
    @Test
    public void testTheJUnit4TestRunnerReportsIfMoreThanOneMockeryIsFound() {
        listener.runTestIn(JUnit5TestThatCreatesTwoMockeries.class);
        listener.assertTestFailedWithInitializationError();
    }
    
    @Test
    public void testDetectsNonPublicBeforeMethodsCorrectly() {
        listener.runTestIn(JUnit5TestWithNonPublicBeforeMethod.class);
        listener.assertTestFailedWith(Throwable.class);
        Assertions.assertEquals("should have detected non-public before method",
                "Method before() should be public",
                       listener.testExecutionResult.toString());
    }
    
    @Test
    public void testAutoInstantiatesMocks() {
        listener.runTestIn(JUnit5TestThatAutoInstantiatesMocks.class);
        listener.assertTestSucceeded();
    }
}
