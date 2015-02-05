/*  Copyright (c) 2000-2006 jMock.org
 */
package org.jmock.integration.junit3;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;


/**
 * A {@link junit.framework.TestCase} that verifies postconditions after the
 * test has run and before the fixture has been torn down.
 * 
 * @since 1.0
 */
public abstract class VerifyingTestCase extends TestCase {
    private List<Runnable> verifiers = new ArrayList<Runnable>();
    
    public VerifyingTestCase() {
        super();
    }

    public VerifyingTestCase( String name ) {
        super(name);
    }

    /* This is virtually a copy/paste of the same invokedMethod in the TestCase class to allow
     * overriding of runTest in the normal manner.
     *
     * @see junit.framework.TestCase#runBare()
     */
    @Override
    public void runBare() throws Throwable {
        Throwable exception= null;
        setUp();
        try {
            runTest();
            verify();
        } catch (Throwable running) {
            exception= running;
        }
        finally {
            try {
                tearDown();
            } catch (Throwable tearingDown) {
                if (exception == null) exception= tearingDown;
            }
        }
        if (exception != null) throw exception;
    }

    public void verify() {
        for (Runnable verifier : verifiers) {
            verifier.run();
        }
    }
    
    public void addVerifier(Runnable verifier) {
        verifiers.add(verifier);
    }
}
