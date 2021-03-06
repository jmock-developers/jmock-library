package uk.jamesdal.perfmock.perf.concurrent;

import uk.jamesdal.perfmock.perf.Simulation;

import java.util.concurrent.*;

public class PerfThreadPoolExecutor extends ThreadPoolExecutor {

    private final Simulation simulation;

    public static ExecutorService newFixedThreadPool(int nThreads, PerfThreadFactory threadFactory) {
        return new PerfThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>(), threadFactory);
    }

    public PerfThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, PerfThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.simulation = threadFactory.getSimulation();
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new PerfFutureTask<T>(callable, simulation);
    }
}
