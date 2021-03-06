package uk.jamesdal.perfmock.perf.events;

import uk.jamesdal.perfmock.perf.SimEvent;

public class ForkEvent extends SimEvent {
    private final long child;

    public ForkEvent(double simTime, long child) {
        super(simTime);
        this.child = child;
    }

    @Override
    public EventTypes getType() {
        return EventTypes.FORK;
    }

    public long getChild() {
        return child;
    }
}
