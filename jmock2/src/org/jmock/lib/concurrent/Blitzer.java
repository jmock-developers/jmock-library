package org.jmock.lib.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A class that "blitzes" an object by calling it many times, from 
 * multiple threads.  Used for stress-testing synchronisation.
 *   
 * @author nat
 */
public class Blitzer {
    /**
     * The default number of threads to run concurrently.
     */
    public static final int DEFAULT_THREAD_COUNT = 2;
    
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final int threadCount;
    private final int iterationCount;

    
    public Blitzer(int iterationCount) {
      this(DEFAULT_THREAD_COUNT, iterationCount);
    }
    
    public Blitzer(int threadCount, int iterationCount) {
        this.threadCount = threadCount;
        this.iterationCount = iterationCount;
    }
    
    public int totalActionCount() {
        return threadCount * iterationCount;
    }
    
    public void blitz(final Runnable action) throws InterruptedException {
        final CountDownLatch finished = new CountDownLatch(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    repeat(action);
                    finished.countDown();
                }
            });
        }
        
        finished.await();
    }
    
    private void repeat(Runnable action) {
        for (int i = 0; i < iterationCount; i++) {
            action.run();
        }
    }
    
    public void shutdown() {
        executor.shutdown();
    }
}