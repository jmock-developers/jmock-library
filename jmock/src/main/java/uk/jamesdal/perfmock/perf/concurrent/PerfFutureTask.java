package uk.jamesdal.perfmock.perf.concurrent;

import uk.jamesdal.perfmock.perf.Simulation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class PerfFutureTask<T> extends FutureTask<T> {
    private final Simulation simulation;
    private final PerfCallable task;
    private Thread runner;


    public PerfFutureTask(Callable<T> callable, Simulation simulation) {
        super(new PerfCallable<>(callable, simulation));
        this.simulation = simulation;
        this.task = simulation.getLastPerfCallable(Thread.currentThread().getId());
    }

    @Override
    public T get() throws ExecutionException, InterruptedException {
        simulation.pause();
        T res = super.get();
        simulation.pause(this.runner.getId());
        simulation.createTaskJoinEvent(this.runner.getId(), task);
        simulation.play();
        return res;
    }

    @Override
    public void run() {
        this.runner = Thread.currentThread();
        super.run();
    }
}
