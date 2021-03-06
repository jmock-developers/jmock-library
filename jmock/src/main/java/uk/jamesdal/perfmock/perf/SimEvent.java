package uk.jamesdal.perfmock.perf;

import uk.jamesdal.perfmock.perf.events.EventTypes;

public abstract class SimEvent {
    protected final double simTime;

    protected SimEvent(double simTime) {
        this.simTime = simTime;
    }

    public double getSimTime() {
        return simTime;
    }

    public abstract EventTypes getType();
}
