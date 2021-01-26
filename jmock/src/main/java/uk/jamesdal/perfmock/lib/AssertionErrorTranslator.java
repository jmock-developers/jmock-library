package uk.jamesdal.perfmock.lib;

import org.hamcrest.StringDescription;
import uk.jamesdal.perfmock.api.ExpectationError;
import uk.jamesdal.perfmock.api.ExpectationErrorTranslator;

/**
 * Translates {@link ExpectationError}s into
 * {@link java.lang.AssertionError}s that several
 * test frameworks, including JUnit 4 and TestNG, use to report
 * errors.
 * 
 * @author npryce
 *
 */
public class AssertionErrorTranslator implements ExpectationErrorTranslator {
    public static final AssertionErrorTranslator INSTANCE = new AssertionErrorTranslator();
    
    public Error translate(ExpectationError e) {
        return new AssertionError(StringDescription.toString(e));
    }
    
    private AssertionErrorTranslator() {}
}
