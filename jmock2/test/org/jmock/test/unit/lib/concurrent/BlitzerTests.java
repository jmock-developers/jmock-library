package org.jmock.test.unit.lib.concurrent;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

import org.jmock.lib.concurrent.Blitzer;
import org.junit.After;

public class BlitzerTests extends TestCase {
    int threadCount = 3;
    int iterationCount = 4;
    
    int expectedActionCount = threadCount * iterationCount;
    
    Blitzer blitzer = new Blitzer(iterationCount, threadCount);
    
    
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
        
        assertThat(actualActionCount.get(), equalTo(expectedActionCount));
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
        assertThat(blitzer.totalActionCount(), equalTo(expectedActionCount));
    }
}
