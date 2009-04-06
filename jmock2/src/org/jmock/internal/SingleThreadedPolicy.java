package org.jmock.internal;

import java.util.ConcurrentModificationException;

import org.jmock.api.Invocation;
import org.jmock.api.Invokable;
import org.jmock.api.ThreadingPolicy;
import org.jmock.lib.concurrent.Synchroniser;

public class SingleThreadedPolicy implements ThreadingPolicy {
    private final Thread testThread;
    
    public SingleThreadedPolicy() {
        this.testThread = Thread.currentThread();
    }

    public Invokable synchroniseAccessTo(final Invokable mockObject) {
        return new Invokable() {
            public Object invoke(Invocation invocation) throws Throwable {
                checkRunningOnTestThread();
                return mockObject.invoke(invocation);
            }
        };
    }
    
    private void checkRunningOnTestThread() {
        if (Thread.currentThread() != testThread) {
            reportError("the Mockery is not thread-safe: use a " + 
                        Synchroniser.class.getSimpleName() + " to ensure thread safety");
        }
    }
    
    private void reportError(String error) {
        System.err.println(error);
        throw new ConcurrentModificationException(error);
    }
}
