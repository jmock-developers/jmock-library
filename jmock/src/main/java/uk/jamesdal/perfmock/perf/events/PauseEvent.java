package uk.jamesdal.perfmock.perf.events;

import uk.jamesdal.perfmock.perf.SimEvent;

public class PauseEvent extends SimEvent {
    public PauseEvent(double simTime) {
        super(simTime);
    }

    @Override
    public EventTypes getType() {
        return EventTypes.PAUSE;
    }
}
