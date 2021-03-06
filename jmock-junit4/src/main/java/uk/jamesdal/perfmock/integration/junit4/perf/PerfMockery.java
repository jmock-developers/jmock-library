package uk.jamesdal.perfmock.integration.junit4.perf;

import uk.jamesdal.perfmock.AbstractExpectations;
import uk.jamesdal.perfmock.integration.junit4.JUnitRuleMockery;
import uk.jamesdal.perfmock.perf.PerfRule;
import uk.jamesdal.perfmock.perf.Simulation;
import uk.jamesdal.perfmock.perf.postproc.PerfStatistics;
import uk.jamesdal.perfmock.perf.postproc.ReportGenerator;

public class PerfMockery extends JUnitRuleMockery {

    private final Simulation simulation;

    public PerfMockery(PerfRule perfRule) {
        this.simulation = perfRule.getSimulation();
    }

    public void repeat(int iterations, Runnable task) {
        for (int i = 0; i < iterations; i++) {
            simulation.reset();

            simulation.play();
            task.run();
            simulation.pause();

            simulation.save();
        }

        simulation.genReport();
    }

    public void repeat(int iterations, int warmups, Runnable task) {
        for (int i = 0; i < warmups; i++) {
            simulation.reset();

            simulation.play();
            task.run();
            simulation.pause();
        }

        repeat(iterations, task);
    }

    public void checking(AbstractExpectations expectations) {
        expectations.setSimulation(simulation);
        super.checking(expectations);
    }

    public PerfStatistics getPerfStats() {
        return simulation.getStats();
    }
}
