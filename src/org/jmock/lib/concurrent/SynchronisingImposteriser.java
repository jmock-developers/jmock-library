package org.jmock.lib.concurrent;

import static org.hamcrest.StringDescription.asString;

import java.util.concurrent.TimeoutException;

import org.jmock.api.Imposteriser;
import org.jmock.api.Invocation;
import org.jmock.api.Invokable;
import org.jmock.internal.StatePredicate;
import org.jmock.lib.DecoratingImposteriser;
import org.jmock.lib.JavaReflectionImposteriser;
import org.jmock.lib.concurrent.internal.FixedTimeout;
import org.jmock.lib.concurrent.internal.InfiniteTimeout;
import org.jmock.lib.concurrent.internal.Timeout;
import org.junit.Assert;


/**
 * A DecoratingImposteriser that makes the Mockery thread-safe and
 * helps tests synchronise with background threads.
 * 
 * @author Nat Pryce
 */
public class SynchronisingImposteriser extends DecoratingImposteriser {
    private final Object sync = new Object();
    private Error firstError = null;
    
    public SynchronisingImposteriser() {
        this(JavaReflectionImposteriser.INSTANCE);
    }
    
    public SynchronisingImposteriser(Imposteriser imposteriser) {
        super(imposteriser);
    }
    
    /** 
     * Waits for a StatePredicate to become active.  
     * 
     * Warning: this will wait forever unless the test itself has a timeout.
     *   
     * @param p the StatePredicate to wait for
     * @throws InterruptedException
     */
    public void waitUntil(StatePredicate p) throws InterruptedException {
        waitUntil(p, new InfiniteTimeout());
    }
    
    /** 
     * Waits up to a timeout for a StatePredicate to become active.  Fails the
     * test if the timeout expires.
     *   
     * @param p the StatePredicate to wait for
     * @param timeoutMs the timeout in milliseconds
     * @throws InterruptedException
     */
    public void waitUntil(StatePredicate p, long timeoutMs) throws InterruptedException {
        waitUntil(p, new FixedTimeout(timeoutMs));
    }
    
    private void waitUntil(StatePredicate p, Timeout timeout) throws InterruptedException {
        synchronized(sync) {
            while (!p.isActive()) {
                try {
                    sync.wait(timeout.timeRemaining());
                }
                catch (TimeoutException e) {
                    if (firstError != null) {
                        throw firstError;
                    }
                    else {
                        Assert.fail("timed out waiting for " + asString(p));
                    }
                }
            }
        }
        
    }
    
    @Override
    protected Object applyInvocation(Invokable imposter, Invocation invocation) throws Throwable {
        synchronized (sync) {
            try {
                return imposter.invoke(invocation);
            }
            catch (Error e) {
                if (firstError == null) {
                    firstError = e;
                }
                throw e;
            }
            finally {
                sync.notifyAll();
            }
        }
    }
}
