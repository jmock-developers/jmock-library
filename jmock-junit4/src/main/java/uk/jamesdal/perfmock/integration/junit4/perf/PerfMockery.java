package uk.jamesdal.perfmock.integration.junit4.perf;

import uk.jamesdal.perfmock.AbstractExpectations;
import uk.jamesdal.perfmock.integration.junit4.JUnitRuleMockery;
import uk.jamesdal.perfmock.perf.Simulation;
import uk.jamesdal.perfmock.perf.postproc.IterResult;
import uk.jamesdal.perfmock.perf.postproc.PerfStatistics;
import uk.jamesdal.perfmock.perf.postproc.ReportGenerator;
import uk.jamesdal.perfmock.perf.postproc.reportgenerators.ConsoleReportGenerator;

import java.util.ArrayList;
import java.util.List;

public class PerfMockery extends JUnitRuleMockery {

    private Simulation simulation;
    private final ReportGenerator reportGenerator;

    public PerfMockery() {
        this(new ConsoleReportGenerator(), new Simulation());
    }

    public PerfMockery(Simulation simulation) {
        this(new ConsoleReportGenerator(), simulation);
    }

    public PerfMockery(ReportGenerator reportGenerator) {
        this(reportGenerator, new Simulation());
    }

    public PerfMockery(ReportGenerator reportGenerator, Simulation simulation) {
        this.simulation = simulation;
        this.reportGenerator = reportGenerator;
    }

    public void repeat(int iterations, Runnable task) {
        List<IterResult> results = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            simulation.reset();

            simulation.play();
            task.run();
            simulation.pause();

            IterResult result = new IterResult(0.0, simulation.getSimTime());
            results.add(result);
        }

        PerfStatistics stats = new PerfStatistics(results);
        reportGenerator.setStats(stats);
        reportGenerator.generateReport();
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
}
