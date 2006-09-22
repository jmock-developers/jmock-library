package org.jmock.test.unit.integration.junit3;

import org.jmock.integration.junit3.MockObjectTestCase;

public class MockObjectTestCaseTests extends MockObjectTestCase {
    public void testDoesNotNeedToHaveExpectationsSpecified() {
        // no expectations: the test should not fail
    }
}
