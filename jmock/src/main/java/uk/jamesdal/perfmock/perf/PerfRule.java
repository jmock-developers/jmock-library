package uk.jamesdal.perfmock.perf;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Objects;

public class PerfRule implements TestRule {
    private final Simulation simulation = new Simulation();

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

                    long totalTimeElapsed = 0;
                    for (int i = 0; i < warmups; i++) {
                        base.evaluate();
                    }

                    for (int i = 0; i < iterations; i++) {
                        simulation.reset();

                        simulation.play();
                        base.evaluate();
                        simulation.pause();

                        double simulationTime = simulation.getSimTime();
                        totalTimeElapsed += simulationTime;
                    }

                    double averangeTimeElapsed = totalTimeElapsed / iterations;

                    System.out.println("Average Measured Time: " + averangeTimeElapsed + "ms over " + iterations + " iterations");
                }
            };

            statement = perfStatement;
        }
        return statement;
    }

    public Simulation getSimulation() {
        return simulation;
    }
}
