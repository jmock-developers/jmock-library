package org.jmock.lib.concurrent;

import org.hamcrest.StringDescription;
import org.jmock.api.Imposteriser;
import org.jmock.api.Invocation;
import org.jmock.api.Invokable;
import org.jmock.internal.StatePredicate;
import org.junit.Assert;


/**
 * A Decorator that wraps an Imposteriser and makes the Mockery thread-safe.
 * 
 * @author Nat Pryce
 */
public class ThreadSafeImposteriser implements Imposteriser {
    private final Object sync = new Object();
    private final Imposteriser imposteriser;

    public ThreadSafeImposteriser(Imposteriser imposteriser) {
        this.imposteriser = imposteriser;
    }
    
    public boolean canImposterise(Class<?> type) {
        return imposteriser.canImposterise(type);
    }
    
    public void waitUntil(StatePredicate p, long timeoutMs) throws InterruptedException {
        long start = System.currentTimeMillis();
        
        synchronized(sync) {
            while (!p.isActive()) {
                long waitTime = System.currentTimeMillis() - start;
                if (waitTime > timeoutMs) {
                    Assert.fail("timeout waiting for " + StringDescription.asString(p));
                }
                
                sync.wait(timeoutMs - waitTime);
            }
        }
    }
    
    public <T> T imposterise(Invokable mockObject, Class<T> mockedType, Class<?>... ancilliaryTypes) {
        return imposteriser.imposterise(wrapSynchronizationAround(mockObject),
                                        mockedType, ancilliaryTypes);
    }
    
    private Invokable wrapSynchronizationAround(final Invokable mockObject) {
        return new Invokable() {
            public Object invoke(Invocation invocation) throws Throwable {
                synchronized (sync) {
                    try {
                        return mockObject.invoke(invocation);
                    }
                    finally {
                        sync.notifyAll();
                    }
                }
            }
        };
    }
}
