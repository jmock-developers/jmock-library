/**
 * 
 */
package org.jmock.test.unit.support;

import org.jmock.core.Invocation;
import org.jmock.core.Invokable;

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
