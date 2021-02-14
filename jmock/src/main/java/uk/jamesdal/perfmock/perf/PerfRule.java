package uk.jamesdal.perfmock.perf;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import uk.jamesdal.perfmock.perf.postproc.IterResult;
import uk.jamesdal.perfmock.perf.postproc.PerfStatistics;
import uk.jamesdal.perfmock.perf.postproc.ReportGenerator;
import uk.jamesdal.perfmock.perf.postproc.reportgenerators.ConsoleReportGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PerfRule implements TestRule {
    private final Simulation simulation = new Simulation();
    private final ReportGenerator reportGenerator;

    public PerfRule() {
        this(new ConsoleReportGenerator());
    }

    public PerfRule(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        PerfTest perfTestAnnotation = description.getAnnotation(PerfTest.class);

        Statement statement = base;
        if (Objects.nonNull(perfTestAnnotation)) {
            Statement perfStatement = new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    long iterations = perfTestAnnotation.iterations();
                    long warmups = perfTestAnnotation.warmups();

                    for (int i = 0; i < warmups; i++) {
                        simulation.reset();

                        simulation.play();
                        base.evaluate();
                        simulation.pause();
                    }

                    List<IterResult> results = new ArrayList<>();

                    for (int i = 0; i < iterations; i++) {
                        simulation.reset();

                        simulation.play();
                        base.evaluate();
                        simulation.pause();

                        IterResult result = new IterResult(simulation.getRuntime(), simulation.getSimTime());
                        results.add(result);
                    }

                    PerfStatistics stats = new PerfStatistics(results);
                    reportGenerator.setStats(stats);
                    reportGenerator.generateReport(description.getDisplayName());
                }
            };

            statement = perfStatement;
        }
        return statement;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public ReportGenerator getReportGenerator() {
        return reportGenerator;
    }
}
