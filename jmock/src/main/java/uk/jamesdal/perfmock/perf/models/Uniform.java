package uk.jamesdal.perfmock.perf.models;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import uk.jamesdal.perfmock.perf.PerfModel;


public class Uniform extends PerfModel {
    private final UniformRealDistribution uniform;

    public Uniform(Double lower, Double upper) {
        this.uniform = new UniformRealDistribution(lower, upper);
    }

    @Override
    public double sample() {
        return uniform.sample();
    }
}
