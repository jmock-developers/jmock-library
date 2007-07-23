package org.jmock.lib;

import org.jmock.api.ExpectationError;
import org.jmock.api.ExpectationErrorTranslator;

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
