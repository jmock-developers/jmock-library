package uk.jamesdal.perfmock.lib;

import uk.jamesdal.perfmock.api.ExpectationError;
import uk.jamesdal.perfmock.api.ExpectationErrorTranslator;

/**
 * An {@link ExpectationErrorTranslator} that doesn't do any translation.  
 * It returns the {@link ExpectationError} it is given.
 * 
 * @author nat
 * 
 */
public class IdentityExpectationErrorTranslator implements
    ExpectationErrorTranslator
{
    public static final IdentityExpectationErrorTranslator INSTANCE = new IdentityExpectationErrorTranslator();
    
    private IdentityExpectationErrorTranslator() {}
    
    public Error translate(ExpectationError e) {
        return e;
    }
}
