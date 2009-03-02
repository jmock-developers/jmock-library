package org.jmock.test.unit.lib;

import java.util.ConcurrentModificationException;

import org.jmock.api.Imposteriser;
import org.jmock.api.Invocation;
import org.jmock.api.Invokable;
import org.jmock.lib.DecoratingImposteriser;
import org.jmock.lib.concurrent.ThreadSafeImposteriser;

public class SingleThreadedImposteriser extends DecoratingImposteriser {
    private final Thread testThread;
    
    public SingleThreadedImposteriser(Imposteriser imposteriser) {
        super(imposteriser);
        this.testThread = Thread.currentThread();
    }
    
    @Override
    protected Object performDecoration(Invokable imposter, Invocation invocation)
        throws Throwable 
    {
        checkRunningOnTestThread();
        return imposter.invoke(invocation);
    }
    
    private void checkRunningOnTestThread() {
        if (Thread.currentThread() != testThread) {
            reportError("the Mockery is not thread-safe: wrap an Imposteriser in a " + 
                        ThreadSafeImposteriser.class.getSimpleName() + " to ensure thread safety");
        }
    }
    
    private void reportError(String error) {
        System.err.println(error);
        throw new ConcurrentModificationException(error);
    }
}
