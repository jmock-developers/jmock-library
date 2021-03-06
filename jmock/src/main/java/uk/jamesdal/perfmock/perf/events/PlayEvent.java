package uk.jamesdal.perfmock.perf.events;

import uk.jamesdal.perfmock.perf.SimEvent;

public class PlayEvent extends SimEvent {
    private final long runTime;

    public PlayEvent(double simTime, long runTime) {
        super(simTime);
        this.runTime = runTime;
    }

    public long getRunTime() {
        return runTime;
    }

    @Override
    public EventTypes getType() {
        return EventTypes.PLAY;
    }
}
