package org.jmock.test.acceptance.junit4;

import junit.framework.TestCase;

import org.jmock.test.acceptance.junit4.testdata.DerivedJUnit4TestThatDoesNotSatisfyExpectations;
import org.jmock.test.acceptance.junit4.testdata.JUnit4TestThatDoesNotCreateAMockery;
import org.jmock.test.acceptance.junit4.testdata.JUnit4TestThatDoesNotSatisfyExpectations;
import org.jmock.test.acceptance.junit4.testdata.JUnit4TestThatDoesSatisfyExpectations;
import org.jmock.test.acceptance.junit4.testdata.JUnit4TestWithNonPublicBeforeMethod;
import org.jmock.test.unit.support.AssertThat;
import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class JUnit4TestRunnerTests extends TestCase {
    FailureRecordingRunListener listener = new FailureRecordingRunListener();
    
    public void testTheJUnit4TestRunnerReportsPassingTestsAsSuccessful() {
        runTest(JUnit4TestThatDoesSatisfyExpectations.class);
        
        if (listener.failure != null) {
            fail("test should have passed but reported failure: " + listener.failure.getMessage());
        }
    }
    
    public void testTheJUnit4TestRunnerAutomaticallyAssertsThatAllExpectationsHaveBeenSatisfied() {
        runTest(JUnit4TestThatDoesNotSatisfyExpectations.class);
        assertTestFailedWith(AssertionError.class);
    }
    
    public void testTheJUnit4TestRunnerLooksForTheMockeryInBaseClasses() {
        runTest(DerivedJUnit4TestThatDoesNotSatisfyExpectations.class);
        assertTestFailedWith(AssertionError.class);
    }
    
    public void testTheJUnit4TestRunnerReportsAHelpfulErrorIfTheMockeryIsNull() {
        runTest(JUnit4TestThatDoesNotCreateAMockery.class);
        assertTestFailedWith(IllegalStateException.class);
    }
    
    public void testDetectsNonPublicBeforeMethodsCorrectly() {
        runTest(JUnit4TestWithNonPublicBeforeMethod.class);
        
        AssertThat.stringIncludes("should have detected non-public before method", 
                                  "Method before should be public", listener.failure.getMessage());
    }
    
    private void assertTestFailedWith(Class<? extends Throwable> exceptionType) {
        assertNotNull("test should have failed", listener.failure);
        assertTrue("should have failed with " + exceptionType.getName() + " but threw " + listener.failure.getException(), 
                   exceptionType.isInstance(listener.failure.getException()));
    }
    
    private void runTest(Class<?> testClass) {
        Runner runner = Request.aClass(testClass).getRunner();
        RunNotifier notifier = new RunNotifier();
        
        notifier.addListener(listener);       
        runner.run(notifier);
    }
    
    static class FailureRecordingRunListener extends RunListener {
        public Failure failure = null;
        
        @Override
        public void testFailure(Failure failure) throws Exception {
            this.failure = failure;
        }
    }
}
