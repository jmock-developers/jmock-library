package uk.jamesdal.perfmock.perf;

import uk.jamesdal.perfmock.perf.events.EventTypes;

public abstract class SimEvent {
    protected final double simTime;

    protected long threadId;

    protected SimEvent(double simTime, long threadId) {
        this.simTime = simTime;
        this.threadId = threadId;
    }

    protected SimEvent(double simTime) {
        this.simTime = simTime;
        this.threadId = Thread.currentThread().getId();
    }

    public double getSimTime() {
        return simTime;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public abstract EventTypes getType();
}
