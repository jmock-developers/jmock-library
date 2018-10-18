package org.jmock.junit5.services;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Test;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Test
public @interface ExpectationThrows {

    /**
     * Optionally specify <code>expected</code>, a Throwable, to cause a test method
     * to succeed if and only if an exception of the specified class is thrown by
     * the method. If the Throwable's message or one of its properties should be
     * verified, the {@link org.junit.rules.ExpectedException ExpectedException}
     * rule can be used instead.
     */
    Class<? extends Throwable> expected();
}