package uk.jamesdal.perfmock.perf.models;

import uk.jamesdal.perfmock.perf.PerfModel;

public class Constant extends PerfModel {
    private final double value;

    public Constant(double value) {
        this.value = value;
    }

    @Override
    public double sample() {
        return value;
    }
}
