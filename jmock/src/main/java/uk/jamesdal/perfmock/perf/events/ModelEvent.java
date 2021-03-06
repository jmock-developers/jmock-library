package uk.jamesdal.perfmock.perf.events;

import uk.jamesdal.perfmock.perf.SimEvent;

public class ModelEvent extends SimEvent {
    public ModelEvent(double simTime) {
        super(simTime);
    }

    @Override
    public EventTypes getType() {
        return EventTypes.MODEL;
    }
}
