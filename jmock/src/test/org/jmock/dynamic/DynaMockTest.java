/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import junit.framework.TestCase;
import org.jmock.dynamic.support.MockDynamicMock;

public class DynaMockTest extends TestCase {

    private MockDynamicMock mockCoreMock = new MockDynamicMock();
    private DynaMock mock = new DynaMock(mockCoreMock);

    public void testToStringComesFromUnderlyingDynamicMock() {
        mockCoreMock.toStringResult = "some string here";
        assertEquals("Should be same string", "some string here", mock.toString());
    }

    public void testMethodAddsInvocationMockerAndReturnsMethodExpectation() {
        mockCoreMock.addCalls.setExpected(1);

        assertNotNull("Should be method expectation", mock.method("methodname", "param1", "param2"));
        mockCoreMock.verifyExpectations();
    }

    public void testVerifyCallsUnderlyingMock() {
        mockCoreMock.verifyCalls.setExpected(1);

        mock.verify();

        mockCoreMock.verifyExpectations();
    }
}
