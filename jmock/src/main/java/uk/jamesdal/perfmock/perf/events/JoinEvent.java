package uk.jamesdal.perfmock.perf.events;

import uk.jamesdal.perfmock.perf.SimEvent;

public class JoinEvent extends SimEvent {
    public JoinEvent(double simTime) {
        super(simTime);
    }

    @Override
    public EventTypes getType() {
        return EventTypes.JOIN;
    }
}
