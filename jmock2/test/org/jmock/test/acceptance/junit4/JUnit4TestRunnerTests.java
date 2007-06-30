package org.jmock.test.acceptance.junit4;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.jmock.test.unit.support.AssertThat;
import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class JUnit4TestRunnerTests extends TestCase {
    public void testTheJUnit4IntegrationTestRunnerAutomaticallyAssertsThatAllExpectationsHaveBeenSatisfied() {
        Runner runner = Request.aClass(JUnit4TestThatDoesNotSatisfyExpectations.class).getRunner();
        RunNotifier notifier = new RunNotifier();
        notifier.addListener(new FailureExpectingRunListener());
        
        runner.run(notifier);
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
    
    static class FailureExpectingRunListener extends RunListener {
        private boolean failed = false;
        
        @Override
        public void testFailure(Failure failure) throws Exception {
            failed = true;
        }
        
        @Override
        public void testFinished(Description description) throws Exception {
            if (!failed) {
                Assert.fail("test should have failed");
            }
        }
    }
}
