package org.jmock.test.unit.lib.concurrent;

import junit.framework.TestCase;
import org.jmock.lib.concurrent.Blitzer;
import org.junit.After;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class BlitzerTests extends TestCase {
    int threadCount = 3;
    int actionCount = 12;

    ThreadFactory quietThreadFactory = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    // ignore it
                }
            });
            return thread;
        }
    };

    Blitzer blitzer = new Blitzer(actionCount, threadCount, quietThreadFactory);

    
    @After
    public void cleanUp() {
        blitzer.shutdown();
    }
    
    public void testRunsTheActionMultipleTimesOnMultipleThreads() throws InterruptedException {
        final AtomicInteger actualActionCount = new AtomicInteger();
        
        blitzer.blitz(new Runnable() {
            public void run() {
                actualActionCount.incrementAndGet();
            }
        });
        
        assertThat(actualActionCount.get(), equalTo(actionCount));
    }
    
    public void testActionsCanFailWithoutDeadlockingTheTestThread() throws InterruptedException, TimeoutException {
        blitzer.blitz(100, new Runnable() {
            public void run() {
                throw new RuntimeException("boom!");
            }
        });
        
        // thread reaches here and does not time out
    }
    
    public void testReportsTheTotalNumberOfActions() {
        assertThat(blitzer.totalActionCount(), equalTo(actionCount));
    }
}
