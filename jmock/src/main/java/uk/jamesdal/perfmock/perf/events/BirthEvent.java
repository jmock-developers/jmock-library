package uk.jamesdal.perfmock.perf.events;

import uk.jamesdal.perfmock.perf.SimEvent;

public class BirthEvent extends SimEvent {
    private final long parent;

    public BirthEvent(double simTime, long parent) {
        super(simTime);
        this.parent = parent;
    }

    @Override
    public EventTypes getType() {
        return EventTypes.BIRTH;
    }

    public long getParent() {
        return parent;
    }
}
