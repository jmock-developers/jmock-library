package org.jmock.junit5.acceptance;

import org.jmock.junit5.testdata.jmock.acceptance.JUnit5WithRulesExamples;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;

public class JUnit5WithRulesTestRunnerTests {
    FailureRecordingTestExecutionListener listener = new FailureRecordingTestExecutionListener();
    
    @Test
    public void testTheJUnit4TestRunnerReportsPassingTestsAsSuccessful() {
        listener.runTestIn(JUnit5WithRulesExamples.SatisfiesExpectations.class);
        listener.assertTestSucceeded();
    }
    
    @Test
    public void testTheJUnit4TestRunnerAutomaticallyAssertsThatAllExpectationsHaveBeenSatisfied() {
        listener.runTestIn(JUnit5WithRulesExamples.DoesNotSatisfyExpectations.class);
        listener.assertTestFailedWith(AssertionError.class);
    }
    
    @Test
    public void testTheJUnit4TestRunnerLooksForTheMockeryInBaseClasses() {
        listener.runTestIn(JUnit5WithRulesExamples.DerivedAndDoesNotSatisfyExpectations.class);
        listener.assertTestFailedWith(AssertionError.class);
    }

    
    // See issue JMOCK-156
    @Test
    public void testReportsMocksAreNotSatisfiedWhenExpectedExceptionIsThrown() {
        listener.runTestIn(JUnit5WithRulesExamples.ThrowsExpectedException.class);
        listener.assertTestFailedWith(AssertionError.class);
    }
    
    @Test
    public void testFailsWhenMoreThanOneJMockContextField() {
        listener.runTestIn(JUnit5WithRulesExamples.CreatesTwoMockeries.class);
        listener.assertTestFailedWith(ExtensionConfigurationException.class);
    }

    @Test
    public void testAutoInstantiatesMocks() {
        listener.runTestIn(JUnit5WithRulesExamples.AutoInstantiatesMocks.class);
        listener.assertTestSucceeded();
    }
}
