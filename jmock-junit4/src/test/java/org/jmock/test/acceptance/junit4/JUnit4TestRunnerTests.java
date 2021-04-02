package org.jmock.test.acceptance.junit4;

import junit.framework.TestCase;
import testdata.jmock.acceptance.junit4.*;

public class JUnit4TestRunnerTests extends TestCase {
    FailureRecordingRunListener listener = new FailureRecordingRunListener();
    
    public void testTheJUnit4TestRunnerReportsPassingTestsAsSuccessful() {
        listener.runTestIn(JUnit4TestThatDoesSatisfyExpectations.class);
        listener.assertTestSucceeded();
    }
    
    public void testTheJUnit4TestRunnerAutomaticallyAssertsThatAllExpectationsHaveBeenSatisfied() {
        listener.runTestIn(JUnit4TestThatDoesNotSatisfyExpectations.class);
        listener.assertTestFailedWith(AssertionError.class);
    }
    
    public void testTheJUnit4TestRunnerLooksForTheMockeryInBaseClasses() {
        listener.runTestIn(DerivedJUnit4TestThatDoesNotSatisfyExpectations.class);
        listener.assertTestFailedWith(AssertionError.class);
    }
    
    public void testTheJUnit4TestRunnerReportsAHelpfulErrorIfTheMockeryIsNull() {
        listener.runTestIn(JUnit4TestThatDoesNotCreateAMockery.class);
        listener.assertTestFailedWith(IllegalStateException.class);
    }
    
    // See issue JMOCK-156
    public void testReportsMocksAreNotSatisfiedWhenExpectedExceptionIsThrown() {
        listener.runTestIn(JUnit4TestThatThrowsExpectedException.class);
        listener.assertTestFailedWith(AssertionError.class);
    }

    // See issue JMOCK-219
    public void testTheJUnit4TestRunnerReportsIfNoMockeryIsFound() {
        listener.runTestIn(JUnit4TestThatCreatesNoMockery.class);
        listener.assertTestFailedWithInitializationError();
    }

    // See issue JMOCK-219
    public void testTheJUnit4TestRunnerReportsIfMoreThanOneMockeryIsFound() {
        listener.runTestIn(JUnit4TestThatCreatesTwoMockeries.class);
        listener.assertTestFailedWithInitializationError();
    }
    
    public void testDetectsNonPublicBeforeMethodsCorrectly() {
        listener.runTestIn(JUnit4TestWithNonPublicBeforeMethod.class);
        listener.assertTestFailedWith(Throwable.class);
        assertEquals("should have detected non-public before method",
                "Method before() should be public",
                       listener.failure.getMessage());
    }
    
    public void testAutoInstantiatesMocks() {
        listener.runTestIn(JUnit4TestThatAutoInstantiatesMocks.class);
        listener.assertTestSucceeded();
    }
}
