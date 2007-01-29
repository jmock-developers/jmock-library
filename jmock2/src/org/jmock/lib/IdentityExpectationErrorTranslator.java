package org.jmock.lib;

import org.jmock.api.ExpectationError;
import org.jmock.api.ExpectationErrorTranslator;

public class IdentityExpectationErrorTranslator implements
    ExpectationErrorTranslator
{
    public static final IdentityExpectationErrorTranslator INSTANCE = new IdentityExpectationErrorTranslator();
    
    private IdentityExpectationErrorTranslator() {}
    
    public Error translate(ExpectationError e) {
        return e;
    }
}
