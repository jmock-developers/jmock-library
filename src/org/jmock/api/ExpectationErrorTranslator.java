package org.jmock.api;

/**
 * Translates expectation errors into error type used by a specific 
 * testing framework.
 * 
 * @author npryce
 */
public interface ExpectationErrorTranslator {
    /**
     * Translates the given {@link ExpectationError} into an error
     * type compatible with another testing framework.
     * 
     * @param e
     *   The {@link ExpectationError} to translate.
     *   
     * @return
     *   An error that is compatible with another testing framework
     *   and contains the same message and stack trace as <var>e</var>.
     */
    Error translate(ExpectationError e);
}
