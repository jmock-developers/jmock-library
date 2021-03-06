package uk.jamesdal.perfmock.perf.concurrent;

import uk.jamesdal.perfmock.perf.Simulation;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class PerfThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private final Simulation simulation;

    public PerfThreadFactory(Simulation simulation) {
        SecurityManager s = System.getSecurityManager();
        this.group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        this.namePrefix = "pool-" +
                poolNumber.getAndIncrement() +
                "-thread-";
        this.simulation = simulation;
    }

    public Thread newThread(Runnable r) {
        Thread t = new PerfThread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0, simulation);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }

    public Simulation getSimulation() {
        return simulation;
    }
}
