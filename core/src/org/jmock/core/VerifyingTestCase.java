package org.jmock.core;

import junit.framework.TestCase;
import org.jmock.util.Verifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A {@link junit.framework.TestCase} that verifies {@link org.jmock.core.Verifiable}
 * fields and registered Verifiable objects after the test has run and before the fixture
 * has been torn down.
 */
public abstract class VerifyingTestCase extends TestCase 
{
    private List objectsThatRequireVerification = new ArrayList();

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

    public void registerToVerify(Verifiable verifiable) {
        objectsThatRequireVerification.add(verifiable);
    }

    public void unregisterToVerify(Verifiable verifiable) {
        objectsThatRequireVerification.remove(verifiable);
    }

    public void verify() {
        for (Iterator iterator = objectsThatRequireVerification.iterator(); iterator.hasNext();) {
            Verifiable verifiable = (Verifiable) iterator.next();
            verifiable.verify();
        }
    	Verifier.verifyObject(this);
    }

}

