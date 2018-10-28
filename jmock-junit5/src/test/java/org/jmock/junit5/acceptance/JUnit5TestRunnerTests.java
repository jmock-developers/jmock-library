package org.jmock.junit5.acceptance;

import org.jmock.junit5.testdata.DerivedJUnit5TestThatDoesNotSatisfyExpectations;
import org.jmock.junit5.testdata.JUnit5TestThatAutoInstantiatesMocks;
import org.jmock.junit5.testdata.JUnit5TestThatCreatesNoMockery;
import org.jmock.junit5.testdata.JUnit5TestThatCreatesTwoMockeries;
import org.jmock.junit5.testdata.JUnit5TestThatDoesNotCreateAMockery;
import org.jmock.junit5.testdata.JUnit5TestThatDoesNotSatisfyExpectations;
import org.jmock.junit5.testdata.JUnit5TestThatDoesSatisfyExpectations;
import org.jmock.junit5.testdata.JUnit5TestThatThrowsExpectedException;
import org.junit.jupiter.api.Test;

public class JUnit5TestRunnerTests {

    FailureRecordingTestExecutionListener listener = new FailureRecordingTestExecutionListener();

    @Test
    public void testTheJUnit5TestRunnerReportsPassingTestsAsSuccessful() {
        listener.runTestIn(JUnit5TestThatDoesSatisfyExpectations.class);
        listener.assertTestSucceeded();
    }

    @Test
    public void testTheJUnit5TestRunnerAutomaticallyAssertsThatAllExpectationsHaveBeenSatisfied() {
        listener.runTestIn(JUnit5TestThatDoesNotSatisfyExpectations.class);
        listener.assertTestFailedWith(AssertionError.class);
    }

    @Test
    public void testTheJUnit5TestRunnerLooksForTheMockeryInBaseClasses() {
        listener.runTestIn(DerivedJUnit5TestThatDoesNotSatisfyExpectations.class);
        listener.assertTestFailedWith(AssertionError.class);
    }

    @Test
    public void testTheJUnit5TestRunnerReportsAHelpfulErrorIfTheMockeryIsNull() {
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
    public void testTheJUnit5TestRunnerReportsIfNoMockeryIsFound() {
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
    public void testAutoInstantiatesMocks() {
        listener.runTestIn(JUnit5TestThatAutoInstantiatesMocks.class);
        listener.assertTestSucceeded();
    }
}
