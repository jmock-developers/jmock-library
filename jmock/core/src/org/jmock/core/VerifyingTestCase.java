package org.jmock.core;

import org.jmock.util.Verifier;

import junit.framework.TestCase;

/**
 * A {@link junit.framework.TestCase} that verifies any {@link org.jmock.core.Verifiable}
 * fields after the test has run and before the fixture has been torn down.
 */
public abstract class VerifyingTestCase extends TestCase 
{
    /* This is virtually a copy/paste of the same method in the TestCase class to allow
     * overriding of runTest in the normal manner. 
     * 
     * @see junit.framework.TestCase#runBare()
     */
    public void runBare() throws Throwable {
        setUp();
        try {
            runTest();
            verify();
        }
        finally {
            tearDown();
        }
    }
    
    public void verify() {
    	Verifier.verifyObject(this);
    }
}

