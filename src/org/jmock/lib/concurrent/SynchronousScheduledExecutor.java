package org.jmock.lib.concurrent;

import java.util.ArrayList;
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
 * An {@link Executor} that executes commands on the thread that calls any of
 * its run methods.
 * 
 * @author nat
 */
public class SynchronousScheduledExecutor implements ScheduledExecutorService {
    private List<Runnable> commands = new ArrayList<Runnable>();
    private final DeltaQueue<Runnable> deltaQueue = new DeltaQueue<Runnable>();

    /**
     * Runs all commands that are currently pending. If those commands also
     * schedule commands for execution, the scheduled commands will <em>not</em>
     * be executed until the next call to {@link #runPendingCommands()} or
     * {@link #runUntilIdle()}.
     */
    public void runPendingCommands() {
        List<Runnable> commandsToRun = commands;
        commands = new ArrayList<Runnable>();

        for (Runnable command: commandsToRun) {
            command.run();
        }
    }

    /**
     * Runs executed commands until there are no commands pending execution, but
     * does not tick time forward.
     */
    public void runUntilIdle() {
        while (!commands.isEmpty()) {
            runPendingCommands();
        }
    }

    public void tick(long duration, TimeUnit timeUnit) {
        long remaining = toTicks(duration, timeUnit);
        
        do {
            remaining = deltaQueue.tick(remaining);
            
            while (deltaQueue.isNotEmpty() && deltaQueue.delay() == 0) {
                commands.add(deltaQueue.pop());
            }
            
            runUntilIdle();
            
        } while (deltaQueue.isNotEmpty() && remaining > 0);
    }
    
    private long toTicks(long duration, TimeUnit timeUnit) {
        return TimeUnit.MILLISECONDS.convert(duration, timeUnit);
    }
    
    public void execute(Runnable command) {
        commands.add(command);
    }

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        deltaQueue.add(toTicks(delay, unit), command);
        return new SynchronousScheduledFuture<Object>();
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        throw new UnsupportedOperationException("not supported");
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        throw new UnsupportedOperationException("not supported");
    }
    
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        throw new UnsupportedOperationException("not supported");
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("not supported");
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        throw new UnsupportedOperationException("not supported");
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("not supported");
    }
    
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
        throws InterruptedException, ExecutionException {
        throw new UnsupportedOperationException("not supported");
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
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
