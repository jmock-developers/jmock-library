package uk.jamesdal.perfmock.perf.concurrent;

import uk.jamesdal.perfmock.perf.Simulation;

public class PerfThread extends Thread {

    private final Simulation simulation;

    public PerfThread(ThreadGroup group, Runnable target, String name, int stackSize, Simulation simulation) {
        super(group, target, name, stackSize);
        this.simulation = simulation;
    }

    @Override
    public synchronized void start() {
        simulation.setUpNewThreads(Thread.currentThread().getId(), this.getId());
        super.start();
    }

    public final void perfJoin() throws InterruptedException {
        perfJoin(0);
    }

    public synchronized final void perfJoin(final long millis) throws InterruptedException {
        simulation.pause();
        join(millis);
        simulation.pause(this.getId());
        simulation.createJoinEvent(this.getId());
        simulation.play();
    }
}
