package uk.jamesdal.perfmock.perf.postproc;

public class IterResult {
    private final double runtime;
    private final double measuredTime;

    public IterResult(double runtime, double measuredTime) {
        this.runtime = runtime;
        this.measuredTime = measuredTime;
    }

    public double getRuntime() {
        return runtime;
    }

    public double getMeasuredTime() {
        return measuredTime;
    }
}
