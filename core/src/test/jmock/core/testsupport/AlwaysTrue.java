package test.jmock.core.testsupport;

import org.jmock.core.Constraint;

public class AlwaysTrue implements Constraint {
    public static AlwaysTrue INSTANCE = new AlwaysTrue();
    
    public boolean eval( Object o ) { return true; }
    public String toString() { return "always true"; }
}