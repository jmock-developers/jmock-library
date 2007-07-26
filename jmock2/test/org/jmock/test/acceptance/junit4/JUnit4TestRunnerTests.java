package org.jmock.test.acceptance.junit4;

import junit.framework.TestCase;

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
    public void testTheJUnit4IntegrationReportsPassingTestsAsSuccessful() {
        Runner runner = Request.aClass(JUnit4TestThatDoesSatisfyExpectations.class).getRunner();
        RunNotifier notifier = new RunNotifier();
        FailureRecordingRunListener listener = new FailureRecordingRunListener();
        
        notifier.addListener(listener);
        
        runner.run(notifier);
        
        if (listener.failure != null) {
            fail("test should have passed but reported failure: " + listener.failure.getMessage());
        }
    }
    
    public void testTheJUnit4IntegrationTestRunnerAutomaticallyAssertsThatAllExpectationsHaveBeenSatisfied() {
        Runner runner = Request.aClass(JUnit4TestThatDoesNotSatisfyExpectations.class).getRunner();
        RunNotifier notifier = new RunNotifier();
        
        FailureRecordingRunListener listener = new FailureRecordingRunListener();
        notifier.addListener(listener);
        
        runner.run(notifier);
        
        assertNotNull("test should have failed", listener.failure);
        assertTrue("should have failed with AssertionError but threw " + listener.failure.getException(), 
                   listener.failure.getException() instanceof AssertionError);
    }
    
    public void testDetectsNonPublicBeforeMethodsCorrectly() {
        Runner runner = Request.aClass(JUnit4TestWithNonPublicBeforeMethod.class).getRunner();
        RunNotifier notifier = new RunNotifier();
        FailingRunListener listener = new FailingRunListener();
        notifier.addListener(listener);
        
        runner.run(notifier);
        
        AssertThat.stringIncludes("should have detected non-public before method", 
                                  "Method before should be public", listener.failure.getMessage());
    }
    
    static class FailingRunListener extends RunListener {
        public Failure failure;
        
        @Override
        public void testFailure(Failure failure) throws Exception {
            this.failure = failure;
        }
    }
    
    static class FailureRecordingRunListener extends RunListener {
        public Failure failure = null;
        
        @Override
        public void testFailure(Failure failure) throws Exception {
            this.failure = failure;
        }
    }
}
