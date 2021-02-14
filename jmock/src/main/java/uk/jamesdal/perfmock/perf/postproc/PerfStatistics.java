package uk.jamesdal.perfmock.perf.postproc;

import java.util.ArrayList;
import java.util.List;

public class PerfStatistics {

    private final List<IterResult> results;

    public PerfStatistics(List<IterResult> results) {
        this.results = results;
    }

    public List<Double> getRuntimes() {
        List<Double> runtimes = new ArrayList<>();
        for (IterResult result : results) {
            runtimes.add(result.getRuntime());
        }

        return runtimes;
    }

    public List<Double> getMeasuredTimes() {
        List<Double> measuredTimes = new ArrayList<>();
        for (IterResult result : results) {
            measuredTimes.add(result.getMeasuredTime());
        }

        return measuredTimes;
    }

    private double max(List<Double> list) {
        double max = 0.0;
        for (Double x : list) {
            max = Math.max(max, x);
        }

        return max;
    }

    private double min(List<Double> list) {
        double minRuntime = Double.MAX_VALUE;
        for (Double x : list) {
            minRuntime = Math.min(minRuntime, x);
        }

        return minRuntime;
    }


    private double var(List<Double> list) {
        double total = 0.0;
        double mean = mean(list);

        for (Double x : list) {
            total += Math.pow(x - mean, 2);
        }

        return total / list.size();
    }

    private double total(List<Double> list) {
        double totalRuntime = 0.0;
        for (Double x : list) {
            totalRuntime += x;
        }

        return totalRuntime;
    }

    private double mean(List<Double> list) {
        return total(list) / list.size();
    }

    public int count() {
        return results.size();
    }

    // Runtime
    public double meanRuntime() {
       return mean(getRuntimes());
    }

    public double maxRuntime() {
        return max(getRuntimes());
    }

    public double minRuntime() {
        return min(getRuntimes());
    }

    public double varRuntime() {
        return var(getRuntimes());
    }

    public double totalRuntime() {
        return total(getRuntimes());
    }

    // Measured Time
    public double meanMeasuredTime() {
        return mean(getMeasuredTimes());
    }

    public double maxMeasuredTime() {
        return max(getMeasuredTimes());
    }

    public double minMeasuredTime() {
        return min(getMeasuredTimes());
    }

    public double varMeasuredTime() {
        return var(getMeasuredTimes());
    }

    public double totalMeasuredTime() {
        return total(getMeasuredTimes());
    }
}
