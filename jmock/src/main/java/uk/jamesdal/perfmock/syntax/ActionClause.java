package uk.jamesdal.perfmock.syntax;

import uk.jamesdal.perfmock.api.Action;


public interface ActionClause {
    public abstract void will(Action action);
}
