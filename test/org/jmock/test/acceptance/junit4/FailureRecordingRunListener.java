package org.jmock.test.acceptance.junit4;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

class FailureRecordingRunListener extends RunListener {
    public Failure failure = null;
    
    @Override
    public void testFailure(Failure failure) throws Exception {
        this.failure = failure;
    }

    public void assertTestSucceeded() {
        if (failure != null) {
            fail("test should have passed but reported failure: " + failure.getMessage());
        }
    }

    public void assertTestFailedWith(Class<? extends Throwable> exceptionType) {
        assertNotNull("test should have failed", failure);
        assertTrue("should have failed with " + exceptionType.getName() + " but threw " + failure.getException(), 
                   exceptionType.isInstance(failure.getException()));
    }

    public void assertTestFailedWithInitializationError() {
        assertNotNull("test should have failed", failure);
        assertTrue("should have failed with initialization error, but failure was " + failure.toString(),
                   failure.getDescription().toString().contains("initializationError"));
    }

    public void runTestIn(Class<?> testClass) {
        Runner runner = Request.aClass(testClass).getRunner();
        RunNotifier notifier = new RunNotifier();
        
        notifier.addListener(this);       
        runner.run(notifier);
    }
}