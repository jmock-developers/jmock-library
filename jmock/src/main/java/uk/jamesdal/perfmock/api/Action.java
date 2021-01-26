/*  Copyright (c) 2000-2004 jMock.org
 */
package uk.jamesdal.perfmock.api;

import org.hamcrest.SelfDescribing;
import uk.jamesdal.perfmock.internal.InvocationExpectation;


/**
 * An object that fakes the behaviour of an {@link InvocationExpectation}.
 */
public interface Action extends SelfDescribing, Invokable {
}
