package org.jmock.test.unit.lib.concurrent;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.concurrent.atomic.AtomicInteger;

import org.jmock.lib.concurrent.Blitzer;
import org.junit.After;
import org.junit.Test;

public class BlitzerTests {
    int threadCount = 3;
    int iterationCount = 4;
    
    int expectedActionCount = threadCount * iterationCount;
    
    Blitzer blitzer = new Blitzer(threadCount, iterationCount);

    @After
    public void cleanUp() {
        blitzer.shutdown();
    }
    
    @Test
    public void runsTheActionMultipleTimesOnMultipleThreads() throws InterruptedException {
        final AtomicInteger actualActionCount = new AtomicInteger();
        
        blitzer.blitz(new Runnable() {
            public void run() {
                actualActionCount.incrementAndGet();
            }
        });
        
        assertThat(actualActionCount.get(), equalTo(expectedActionCount));
    }
    
    
    @Test
    public void reportsTheTotalNumberOfIterations() {
        assertThat(blitzer.totalActionCount(), equalTo(expectedActionCount));
    }
}
