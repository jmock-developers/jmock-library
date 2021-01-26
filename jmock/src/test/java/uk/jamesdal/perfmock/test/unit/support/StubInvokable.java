/**
 * 
 */
package uk.jamesdal.perfmock.test.unit.support;

import uk.jamesdal.perfmock.api.Invocation;
import uk.jamesdal.perfmock.api.Invokable;

public class StubInvokable implements Invokable {
    public boolean wasInvoked = false;
    
    public Object invoke(Invocation invocation) throws Throwable {
        wasInvoked = true;
        return null;
    }
    
    
    public String toStringResult;
    
    @Override
    public String toString() {
        return toStringResult;
    }
}
