package test.jmock.dynamic.testsupport;

import org.jmock.Constraint;

public class AlwaysTrue implements Constraint {
    public static AlwaysTrue INSTANCE = new AlwaysTrue();
    
    public boolean eval( Object o ) { return true; }
    public String toString() { return "always true"; }
}