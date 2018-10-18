package org.jmock.junit5.acceptance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import com.google.auto.service.AutoService;

@AutoService(TestExecutionListener.class)
public class FailureRecordingTestExecutionListener implements TestExecutionListener {

    TestExecutionResult testExecutionResult;

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        this.testExecutionResult = testExecutionResult;
    }

    public void assertTestSucceeded() {
        if (testExecutionResult.getStatus().equals(Status.FAILED)) {
            fail("test should have passed but reported failure: " + testExecutionResult.toString());
        }
    }

    public void assertTestFailedWith(Class<? extends Throwable> exceptionType) {
        assertEquals(Status.FAILED, testExecutionResult.getStatus(), "test should have failed");
        Throwable cause = testExecutionResult.getThrowable().get();
        assertTrue(exceptionType.isInstance(cause),
                "should have failed with " + exceptionType.getName() + " but threw " + cause);
    }

    public void assertTestFailedWithInitializationError() {
        assertEquals(Status.FAILED, testExecutionResult.getStatus(), "test should have failed");
        assertTrue(testExecutionResult.toString().contains("initializationError"),
                "should have failed with initialization error, but failure was " + testExecutionResult.toString());
    }

    public void runTestIn(Class<?> testClass) {
        Launcher launcher = LauncherFactory.create();
        launcher.registerTestExecutionListeners(this);
        launcher.execute(LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(testClass))
                .build());
    }
}