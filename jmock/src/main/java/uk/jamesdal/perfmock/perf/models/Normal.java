package uk.jamesdal.perfmock.perf.models;

import org.apache.commons.math3.distribution.NormalDistribution;
import uk.jamesdal.perfmock.perf.PerfModel;

public class Normal extends PerfModel {
    private final NormalDistribution normal;

    public Normal(Double mean, Double var) {
        this.normal = new NormalDistribution(mean, var);
    }

    @Override
    public double sample() {
        return normal.sample();
    }
}
