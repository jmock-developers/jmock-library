package uk.jamesdal.perfmock.integration.junit4.perf;

import uk.jamesdal.perfmock.AbstractExpectations;
import uk.jamesdal.perfmock.integration.junit4.JUnitRuleMockery;
import uk.jamesdal.perfmock.perf.Simulation;

public class PerfMockery extends JUnitRuleMockery {

    private Simulation simulation = new Simulation();

    public PerfMockery(Simulation simulation) {
        this.simulation = simulation;
    }

    public PerfMockery() {}

    public void repeat(int iterations, Runnable task) {
        long totalTimeElapsed = 0;

        for (int i = 0; i < iterations; i++) {
            simulation.reset();

            simulation.play();
            task.run();
            simulation.pause();

            double simulationTime = simulation.getSimTime();
            totalTimeElapsed += simulationTime;
        }

        double averangeTimeElapsed = totalTimeElapsed / iterations;

        System.out.println("Average Measured Time: " + averangeTimeElapsed + "ms over " + iterations + " iterations");
    }

    public void repeat(int iterations, int warmups, Runnable task) {
        for (int i = 0; i < warmups; i++) {
            task.run();
        }

        repeat(iterations, task);
    }

    public void checking(AbstractExpectations expectations) {
        expectations.setSimulation(simulation);
        super.checking(expectations);
    }
}
