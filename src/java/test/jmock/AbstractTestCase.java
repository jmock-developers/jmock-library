/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock;

import junit.framework.TestCase;
import org.jmock.expectation.AssertMo;
import org.jmock.expectation.Verifiable;

/**
 * Provides a level of indirection from TestCase so you can accomodate
 * JUnit interface changes (like the change from 2.x to 3.1)
 */
public abstract class AbstractTestCase extends TestCase {

    public void assertVerifyFails(Verifiable aVerifiable) {
        AssertMo.assertVerifyFails(aVerifiable);
    }

}
