package uk.jamesdal.perfmock.internal;

import org.hamcrest.SelfDescribing;
import uk.jamesdal.perfmock.api.Action;

/**
 * An expectation has one {@link Action} but can have zero or more SideEffects
 * that are triggered before the Action.
 * 
 * @author nat
 *
 */
public interface SideEffect extends SelfDescribing {
    void perform();
}
