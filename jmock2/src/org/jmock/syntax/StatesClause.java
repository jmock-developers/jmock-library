package org.jmock.syntax;

import org.jmock.internal.State;
import org.jmock.internal.StatePredicate;


public interface StatesClause {

    State is(String name);

    StatePredicate isNot(String name);

}
