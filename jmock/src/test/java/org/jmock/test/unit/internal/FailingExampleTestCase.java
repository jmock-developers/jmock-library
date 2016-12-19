package org.jmock.test.unit.internal;


/**
* @author Steve Freeman 2012 http://www.jmock.org
*/
public abstract class FailingExampleTestCase extends VerifyingTestCase {
    public static final Exception tearDownException = new Exception("tear down");
    public static final Exception testException = new Exception("test");

    public FailingExampleTestCase(String testName) {
        super(testName);
    }

    @Override public void tearDown() throws Exception {
        throw tearDownException;

    }
    public void testDoesNotThrowException() throws Exception {
        // no op
    }

    public void testThrowsExpectedException() throws Exception {
        throw testException;
    }
}
