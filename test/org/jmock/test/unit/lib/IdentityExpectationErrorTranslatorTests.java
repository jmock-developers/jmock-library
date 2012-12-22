package org.jmock.test.unit.lib;

import junit.framework.TestCase;

import org.jmock.api.ExpectationError;
import org.jmock.lib.IdentityExpectationErrorTranslator;


public class IdentityExpectationErrorTranslatorTests extends TestCase{
    public void testReturnsTheErrorAsItsOwnTranslation() {
        ExpectationError e = ExpectationError.unexpected(null, null);
        
        assertSame(e, IdentityExpectationErrorTranslator.INSTANCE.translate(e));
    }
}
