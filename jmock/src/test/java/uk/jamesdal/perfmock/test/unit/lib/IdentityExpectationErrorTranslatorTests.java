package uk.jamesdal.perfmock.test.unit.lib;

import junit.framework.TestCase;

import uk.jamesdal.perfmock.api.ExpectationError;
import uk.jamesdal.perfmock.lib.IdentityExpectationErrorTranslator;


public class IdentityExpectationErrorTranslatorTests extends TestCase{
    public void testReturnsTheErrorAsItsOwnTranslation() {
        ExpectationError e = ExpectationError.unexpected(null, null);
        
        assertSame(e, IdentityExpectationErrorTranslator.INSTANCE.translate(e));
    }
}
