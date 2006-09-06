package org.jmock.internal;

import org.jmock.core.ExpectationError;
import org.jmock.core.ExpectationErrorTranslator;

public class IdentityExpectationErrorTranslator implements
    ExpectationErrorTranslator
{
    public static final IdentityExpectationErrorTranslator INSTANCE = new IdentityExpectationErrorTranslator();
    
    private IdentityExpectationErrorTranslator() {}
    
    public Error translate(ExpectationError e) {
        return e;
    }
}
