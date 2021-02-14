package uk.jamesdal.perfmock.perf;

public class Simulation {
    private double simTime;
    private double runTime;
    private long startTime;
    private boolean paused;

    public Simulation() {
        paused = true;
    }

    public void reset() {
        simTime = 0L;
        runTime = 0L;
        paused = true;
    }

    public void play() {
        assert paused : "Simulation is not paused";
        paused = false;

        startTime = System.currentTimeMillis();
    }

    public void pause() {
        assert !paused : "Simulation is already paused";
        paused = true;

        long curTime = System.currentTimeMillis();
        long diff = curTime - startTime;
        runTime += diff;
        simTime += diff;
    }

    public void add(double time) {
        this.simTime += time;
    }

    public double getSimTime() {
        return simTime;
    }

    public double getRuntime() {
        return runTime;
    }
}
