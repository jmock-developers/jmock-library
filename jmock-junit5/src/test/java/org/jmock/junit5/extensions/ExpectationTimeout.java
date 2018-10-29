package org.jmock.junit5.extensions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.jupiter.api.Test;

/**
 * Aim to replace the old Junt4.@Test(timeout) functionality
 * 
 * @author oliverbye
 */
@Retention(RetentionPolicy.RUNTIME)
@Test
public @interface ExpectationTimeout {
    /**
     * Optionally specify <code>timeout</code> in milliseconds to cause a test
     * method to fail if execution time exceeds <code>timeout</code> milliseconds.
     */
    long timeout() default 0L;
}