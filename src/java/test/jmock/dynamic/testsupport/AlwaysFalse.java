package test.jmock.dynamic.testsupport;

import org.jmock.Constraint;

public class AlwaysFalse implements Constraint {
    public static AlwaysFalse INSTANCE = new AlwaysFalse();
    
    public boolean eval( Object o ) { return false; }
    public String toString() { return "always false"; }
}