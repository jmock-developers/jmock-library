package uk.jamesdal.perfmock.perf.events;

import uk.jamesdal.perfmock.perf.SimEvent;

import java.util.HashMap;
import java.util.List;

public class PauseEvent extends SimEvent {
    public PauseEvent(double simTime) {
        super(simTime);
    }

    @Override
    public EventTypes getType() {
        return EventTypes.PAUSE;
    }
}
