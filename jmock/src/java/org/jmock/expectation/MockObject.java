/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.expectation;


public abstract class MockObject implements Verifiable {

    protected void assertEquals(String message, int o1, int o2) {
        AssertMo.assertEquals(message, o1, o2);
    }

    protected void assertEquals(String message, Object o1, Object o2) {
        AssertMo.assertEquals(message, o1, o2);
    }

    protected void assertTrue(String message, boolean condition) {
        AssertMo.assertTrue(message, condition);
    }

    protected void fail(String message) {
        AssertMo.fail(message);
    }

    public void notImplemented() {
        AssertMo.notImplemented(this.getClass().getName());
    }

    /**
     * @deprecated
     */
    protected void notYetImplemented() {
        notYetImplemented(this.getClass().getName());
    }

    /**
     * @deprecated
     */
    public static void notYetImplemented(String mockName) {
        AssertMo.notImplemented(mockName);
    }

    public void verify() {
        Verifier.verifyObject(this);
    }
}
