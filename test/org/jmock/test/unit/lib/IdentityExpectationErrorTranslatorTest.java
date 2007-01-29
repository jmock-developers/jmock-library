package org.jmock.test.unit.lib;

import junit.framework.TestCase;

import org.jmock.api.ExpectationError;
import org.jmock.lib.IdentityExpectationErrorTranslator;


public class IdentityExpectationErrorTranslatorTest extends TestCase{
    public void testReturnsTheErrorAsItsOwnTranslation() {
        ExpectationError e = new ExpectationError(null, null);
        
        assertSame(e, IdentityExpectationErrorTranslator.INSTANCE.translate(e));
    }
}
