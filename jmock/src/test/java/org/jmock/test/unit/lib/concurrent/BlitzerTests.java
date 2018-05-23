package org.jmock.test.unit.lib.concurrent;

import static java.lang.System.getenv;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.jmock.lib.concurrent.Blitzer;
import org.junit.After;

import junit.framework.TestCase;

public class BlitzerTests extends TestCase {
    private static final String BLITZER_THREADS = "blitzerThreads";
    private static final String BLITZER_ACTIONS = "blitzerActions";
    private static final String BLITZER_TIMEOUT = "blitzerTimeout";
    int threadCount = getIntEnvVar(BLITZER_THREADS, "3");
    int actionCount = getIntEnvVar(BLITZER_ACTIONS, "12");
    int timeout = getIntEnvVar(BLITZER_TIMEOUT, "100");

    private int getIntEnvVar(String envVarName, String defaultValue) {
        return Integer.parseInt(getenv().containsKey(envVarName) ? getenv(envVarName) : defaultValue);
    }

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
        blitzer.blitz(timeout, new Runnable() {
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
