package org.jmock.lib.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * An {@link ScheduledExecutorService} that executes commands on the thread that calls any of
 * the {@link #runPendingCommands()}, {@link #runUntilIdle()} or 
 * {@link #tick(long, TimeUnit) tick} methods.  Objects of this class can also be used
 * as synchronous {@link Executor}s if you just want to control background execution and 
 * don't need to schedule commands.
 * 
 * @author nat
 */
public class SynchronousScheduler extends SynchronousExecutor implements ScheduledExecutorService {
    private final DeltaQueue<ScheduledTask> deltaQueue = new DeltaQueue<ScheduledTask>();
    
    /**
     * Runs time forwards by a given duration, executing any commands scheduled for
     * execution during that time period, and any background tasks spawned by the 
     * scheduled tasks.  Therefore, when a call to tick returns, the executor 
     * will be idle.
     * 
     * @param duration
     * @param timeUnit
     */
    public void tick(long duration, TimeUnit timeUnit) {
        long remaining = toTicks(duration, timeUnit);
        
        do {
            remaining = deltaQueue.tick(remaining);
            
            while (deltaQueue.isNotEmpty() && deltaQueue.delay() == 0) {
                ScheduledTask scheduledTask = deltaQueue.pop();
                
                execute(scheduledTask.command);
                if (scheduledTask.repeats()) {
                    deltaQueue.add(scheduledTask.repeatDelay, scheduledTask);
                }
            }
            
            runUntilIdle();
            
        } while (deltaQueue.isNotEmpty() && remaining > 0);
    }
    
    private long toTicks(long duration, TimeUnit timeUnit) {
        return TimeUnit.MILLISECONDS.convert(duration, timeUnit);
    }
    
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        deltaQueue.add(toTicks(delay, unit), new ScheduledTask(command));
        return new SynchronousScheduledFuture<Object>();
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        throw new UnsupportedOperationException("not supported");
    }
    
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return scheduleWithFixedDelay(command, initialDelay, period, unit);
    }
    
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        deltaQueue.add(toTicks(initialDelay, unit), new ScheduledTask(toTicks(delay, unit), command));
        return new SynchronousScheduledFuture<Object>();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("not supported");
    }

    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks) throws InterruptedException {
        throw new UnsupportedOperationException("not supported");
    }

    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("not supported");
    }
    
    public <T> T invokeAny(Collection<Callable<T>> tasks)
        throws InterruptedException, ExecutionException {
        throw new UnsupportedOperationException("not supported");
    }

    public <T> T invokeAny(Collection<Callable<T>> tasks,
        long timeout, TimeUnit unit) throws InterruptedException,
        ExecutionException, TimeoutException {
        throw new UnsupportedOperationException("not supported");
    }

    public boolean isShutdown() {
        throw new UnsupportedOperationException("not supported");
    }

    public boolean isTerminated() {
        throw new UnsupportedOperationException("not supported");
    }

    public void shutdown() {
        throw new UnsupportedOperationException("not supported");
    }

    public List<Runnable> shutdownNow() {
        throw new UnsupportedOperationException("not supported");
    }

    public <T> Future<T> submit(Callable<T> task) {
        throw new UnsupportedOperationException("not supported");
    }

    public Future<?> submit(Runnable task) {
        throw new UnsupportedOperationException("not supported");
    }

    public <T> Future<T> submit(Runnable task, T result) {
        throw new UnsupportedOperationException("not supported");
    }
    
    private final class ScheduledTask {
        public final long repeatDelay;
        public final Runnable command;
        
        public ScheduledTask(Runnable command) {
            this(-1, command);
        }

        public ScheduledTask(long repeatDelay, Runnable command) {
            this.repeatDelay = repeatDelay;
            this.command = command; 
        }
        
        public boolean repeats() {
            return repeatDelay >= 0;
        }
    }

    private final class SynchronousScheduledFuture<T> implements
        ScheduledFuture<T>
    {
        public long getDelay(TimeUnit unit) {
            throw new UnsupportedOperationException("not supported");
        }

        public int compareTo(Delayed o) {
            throw new UnsupportedOperationException("not supported");
        }

        public boolean cancel(boolean mayInterruptIfRunning) {
            throw new UnsupportedOperationException("not supported");
        }

        public T get() throws InterruptedException, ExecutionException {
            throw new UnsupportedOperationException("not supported");
        }

        public T get(long timeout, TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException {
            throw new UnsupportedOperationException("not supported");
        }

        public boolean isCancelled() {
            throw new UnsupportedOperationException("not supported");
        }

        public boolean isDone() {
            throw new UnsupportedOperationException("not supported");
        }
    }
}
