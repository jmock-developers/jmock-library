package uk.jamesdal.perfmock.syntax;

import uk.jamesdal.perfmock.internal.State;
import uk.jamesdal.perfmock.internal.StatePredicate;


public interface StatesClause {

    State is(String name);

    StatePredicate isNot(String name);

}
