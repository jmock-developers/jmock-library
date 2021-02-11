package uk.jamesdal.perfmock.perf;

public class Simulation {
    private double simTime;
    private long startTime;

    public void reset() {
        simTime = 0L;
    }

    public void play() {
        startTime = System.currentTimeMillis();
    }

    public void pause() {
        long curTime = System.currentTimeMillis();
        long diff = curTime - startTime;
        simTime += diff;
    }

    public void add(double time) {
        this.simTime += time;
    }

    public double getSimTime() {
        return simTime;
    }
}
